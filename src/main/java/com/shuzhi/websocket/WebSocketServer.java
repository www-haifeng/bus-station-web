package com.shuzhi.websocket;

import com.alibaba.fastjson.JSON;
import com.shuzhi.entity.DeviceLoop;
import com.shuzhi.entity.DeviceStation;
import com.shuzhi.entity.MqMessage;
import com.shuzhi.entity.Station;
import com.shuzhi.lcd.entities.IotLcdStatusTwo;
import com.shuzhi.lcd.service.IotLcdsStatusService;
import com.shuzhi.led.entities.TStatusDto;
import com.shuzhi.led.service.TStatusService;
import com.shuzhi.light.entities.StatisticsVo;
import com.shuzhi.light.entities.TLoopStateDto;
import com.shuzhi.light.service.LoopStatusServiceApi;
import com.shuzhi.rabbitmq.Message;
import com.shuzhi.service.DeviceLoopService;
import com.shuzhi.service.DeviceStationService;
import com.shuzhi.service.MqMessageService;
import com.shuzhi.service.StationService;
import com.shuzhi.websocket.socketvo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zgk
 * @description websocket
 * @date 2019-07-07 16:13
 */
@Slf4j
@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {

    private StringRedisTemplate redisTemplate;

    private MqMessageService mqMessageService;

    private TStatusService tStatusService;

    private LoopStatusServiceApi loopStatusServiceApi;

    private IotLcdsStatusService iotLcdStatusService;

    private DeviceLoopService deviceLoopService;

    private DeviceStationService deviceStationService;

    private StationService stationService;

    private static Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * lcd设备状态
     */
    private List<IotLcdStatusTwo> allStatusByRedis;

    /**
     * led设备状态
     */
    private List<TStatusDto> allStatus;

    /**
     * 照明设备状态
     */
    private List<TLoopStateDto> loopStatus;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {

        //推送消息到页面
        Optional.ofNullable(session).ifPresent(session1 -> {
            try {
                session1.getBasicRemote().sendText("连接建立成功");
            } catch (IOException e) {
                e.printStackTrace();
                log.error("连接建立失败 sessionId : {}", session1.getId());
            }
        });
    }


    /**
     * 接收消息的方法
     *
     * @param message 收到的消息
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {

        //获取模块id
        Message message1 = JSON.parseObject(message, Message.class);
        String token = String.valueOf(message1.getModulecode());
        SESSION_MAP.put(token, session);
        log.info("当前的session数量 : {}", SESSION_MAP.size());
        SESSION_MAP.entrySet().stream().filter(stringSessionEntry -> session.getId().equals(stringSessionEntry.getValue().getId()))
                .forEach(stringSessionEntry -> log.info("{} 发来消息", stringSessionEntry.getKey()));
        //判断消息类型
        switch (message1.getMsgtype()) {
            //请求
            case "2":
                restHandle(message1);
                break;
            //回执
            case "3":
                receiptHandle(message1);
                break;
            default:
                throw new Exception("消息类型错误");
        }
    }

    /**
     * 处理回执消息
     *
     * @param message 消息
     */
    private void receiptHandle(Message message) {
        //

    }

    /**
     * 处理响应消息
     *
     * @param message 消息
     */
    private void restHandle(Message message) throws ParseException {

        Integer modulecode = message.getModulecode();
        //判断消息编码
        switch (message.getMsgcode()) {
            case 200001:
                break;
            //首次建立连接
            case 200000:
                //回执
                ReceiptHandleVo receiptHandleVo = new ReceiptHandleVo(message.getMsgid(), modulecode, new Date());
                send(String.valueOf(modulecode), JSON.toJSONString(receiptHandleVo));
                //拼装消息
                assemble(message);
            default:
        }
    }

    /**
     * 拼装并推送消息
     *
     * @param message 前端协议
     */
    private void assemble(Message message) throws ParseException {
        //判断是什么设备
        Optional.ofNullable(mqMessageService).orElseGet(() -> mqMessageService = ApplicationContextUtils.get(MqMessageService.class));
        MqMessage mqMessageSelect = new MqMessage();
        mqMessageSelect.setModulecode(message.getModulecode());
        MqMessage mqMessage = mqMessageService.selectOne(mqMessageSelect);
        switch (mqMessage.getExchange()) {
            case "lcd":
                //调用lcd设备的信息
                lcd();
                break;
            case "led":
                //调用led设备信息
                led();
                break;
            case "light":
                //调用照明设备信息
                light();
                break;
            case "platform":
                //调用照明设备信息
                platform();
                break;
            default:
        }
    }

    /**
     * 拼装并发送站台管理信息
     */
    @Scheduled(cron = "${send.platform-cron}")
    private void platform() throws ParseException {
        //查出led的 moduleCode
        Integer modulecode = getModuleCode("platform");
        if (SESSION_MAP.get(String.valueOf(modulecode)) != null) {
            MessageVo messageVo = setMessageVo(modulecode);
            messageVo.setMsgcode(202001);
            List<DevicesMsg> lights = new ArrayList<>();
            //判断是什么设备
            Optional.ofNullable(stationService).orElseGet(() -> stationService = ApplicationContextUtils.get(StationService.class));
            //查询所有的公交站
            DeviceStationService deviceLoopService = ApplicationContextUtils.get(DeviceStationService.class);
            List<DeviceStation> deviceLoops = deviceLoopService.selectAll();
            if (deviceLoops != null) {
                deviceLoops.stream().map(DeviceStation::getStationid).forEach(stationid -> {
                    DevicesMsg devicesMsg = new DevicesMsg();
                    devicesMsg.setStationid(stationid);
                    devicesMsg.setStationname(stationService.selectByPrimaryKey(stationid).getStationName());
                    //添加lcd设备
                    List<Devices> devices = new ArrayList<>();
                    try {
                        setLcdDevices(devices, stationid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("站台管理 获取lcd设备信息失败");
                    }
                    //添加led设备
                    try {
                        setLedDevices(devices, stationid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("站台管理 获取led设备信息失败");
                    }
                    //添加照明设备
                    try {
                        setLightDevices(devicesMsg, devices, stationid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("站台管理 获取照明设备信息失败");
                    }
                    lights.add(devicesMsg);
                });
                messageVo.setMsg(lights);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));
                //拼装并发送站台统计信息
                messageVo.setMsgcode(202001);
                send(String.valueOf(modulecode), platformStatis(messageVo));
                log.info("站台定时任务时间 : {}", messageVo.getTimestamp());
            }
        }
    }

    /**
     * 拼装站台统计信息
     *
     * @param messageVo 要拼装的消息
     * @return 拼装好的消息
     */
    @SuppressWarnings("Duplicates")
    private String platformStatis(MessageVo messageVo) throws ParseException {

        platformStatisVo platformStatisVo = new platformStatisVo();
        Optional.ofNullable(deviceLoopService).orElseGet(() -> deviceLoopService = ApplicationContextUtils.get(DeviceLoopService.class));
        DeviceLoop deviceLoopSelect = new DeviceLoop();
        StatisticsVo statisticsVo = new StatisticsVo();
        //本月
        float currentmonth = 0;
        //上月
        float lastmonth = 0;
        //本年
        float thisyear = 0;

        //获得lcd设备的统计信息
        if (allStatusByRedis != null) {
            platformStatisVo.setLcdtotal(allStatusByRedis.size());
            platformStatisVo.setLcdonline((int) allStatusByRedis.stream().filter(iotLcdStatusTwo -> "1".equals(iotLcdStatusTwo.getStatus())).count());
            platformStatisVo.setLcdoffline((int) allStatusByRedis.stream().filter(iotLcdStatusTwo -> "0".equals(iotLcdStatusTwo.getStatus())).count());
            //能耗信息
            //查出设备的回路号
            for (IotLcdStatusTwo iotLcdStatusTwo : allStatusByRedis) {
                deviceLoopSelect.setDeviceDid(Integer.valueOf(iotLcdStatusTwo.getId()));
                statisticsVo.setLoop(deviceLoopService.selectOne(deviceLoopSelect).getLoop());
                statisticsVo.setDid(iotLcdStatusTwo.getId());
                StatisticsMsgVo statistics = Statistics.findStatistics(statisticsVo);
                currentmonth = currentmonth + statistics.getCurrentmonth();
                lastmonth = lastmonth + statistics.getLastmonth();
                thisyear = thisyear + statistics.getThisyear();
            }
        }
        //获得led设备统计信息
        if (allStatus != null) {
            platformStatisVo.setLedtotal(allStatus.size());
            platformStatisVo.setLedtotal((int) allStatus.stream().filter(tStatusDto -> 1 == (tStatusDto.getState())).count());
            platformStatisVo.setLedtotal((int) allStatus.stream().filter(tStatusDto -> 0 == (tStatusDto.getState())).count());
            //能耗信息
            //查出设备的回路号
            for (TStatusDto status : allStatus) {
                deviceLoopSelect.setDeviceDid(Integer.valueOf(status.getId()));
                statisticsVo.setLoop(deviceLoopService.selectOne(deviceLoopSelect).getLoop());
                statisticsVo.setDid(status.getId());
                StatisticsMsgVo statistics = Statistics.findStatistics(statisticsVo);
                currentmonth = currentmonth + statistics.getCurrentmonth();
                lastmonth = lastmonth + statistics.getLastmonth();
                thisyear = thisyear + statistics.getThisyear();
            }
        }
        //获得照明设备统计信息
        if (loopStatus != null) {
            platformStatisVo.setLighttotal(loopStatus.size());
            platformStatisVo.setLightonline((int) loopStatus.stream().filter(loopStateDto -> 1 == (loopStateDto.getState())).count());
            platformStatisVo.setLightoffline((int) loopStatus.stream().filter(loopStateDto -> 0 == (loopStateDto.getState())).count());
            //能耗信息
            //查出设备的回路号
            for (TLoopStateDto status : loopStatus) {
                statisticsVo.setLoop(status.getLoop());
                statisticsVo.setDid(String.valueOf(status.getId()));
                StatisticsMsgVo statistics = Statistics.findStatistics(statisticsVo);
                currentmonth = currentmonth + statistics.getCurrentmonth();
                lastmonth = lastmonth + statistics.getLastmonth();
                thisyear = thisyear + statistics.getThisyear();
            }
        }
        platformStatisVo.setCurrentmonth(currentmonth);
        platformStatisVo.setLastmonth(lastmonth);
        platformStatisVo.setThisyear(thisyear);
        messageVo.setMsg(platformStatisVo);
        return JSON.toJSONString(messageVo);
    }

    /**
     * 封装照明设备信息
     *
     * @param devicesMsg 总设备信息
     * @param devices    照明设备信息
     * @param stationid  站台id
     */
    private void setLightDevices(DevicesMsg devicesMsg, List<Devices> devices, Integer stationid) {

        //判断照明设备是否为空
        Optional.ofNullable(loopStatus).orElseGet(() -> {
            Optional.ofNullable(loopStatusServiceApi).orElseGet(() -> loopStatusServiceApi = ApplicationContextUtils.get(LoopStatusServiceApi.class));
            loopStatus = loopStatusServiceApi.findLoopStatus();
            return loopStatus;
        });
        //判断该设备是否在该网关下
        DeviceStation deviceStationSelect = new DeviceStation();
        loopStatus.forEach(loopStateDto -> {
            //通过回路id查出设备id
            DeviceLoop deviceLoop = deviceLoopService.selectOne(new DeviceLoop(loopStateDto.getLoop()));
            deviceStationSelect.setDeviceDid(deviceLoop.getDeviceDid());
            DeviceStation deviceStation = deviceStationService.selectOne(deviceStationSelect);
            if (stationid.equals(deviceStation.getStationid())) {
                Devices device = new Devices(loopStateDto);
                devices.add(device);
            }
        });

        devicesMsg.setDevices(devices);
    }

    /**
     * 站台管理 添加led设备
     *
     * @param devices   所有设备信息
     * @param stationid 站台id
     */
    private void setLedDevices(List<Devices> devices, Integer stationid) {

        //判断led设备是否为空
        Optional.ofNullable(allStatus).orElseGet(() -> {
            Optional.ofNullable(tStatusService).orElseGet(() -> tStatusService = ApplicationContextUtils.get(TStatusService.class));
            return tStatusService.findAllStatusByRedis();
        });
        //判断该设备是否在该网关下
        allStatus.stream().filter(iotLedStatus -> stationid.equals(deviceStationService.selectOne(new DeviceStation(iotLedStatus.getDid())).getStationid()))
                .forEach(iotLcdStatus -> {
                    Devices device = new Devices(iotLcdStatus);
                    devices.add(device);
                });

    }

    /**
     * 站台管理添加lcd设备
     *
     * @param devices   所有设备信息
     * @param stationid 站台id
     */
    private void setLcdDevices(List<Devices> devices, Integer stationid) {

        Optional.ofNullable(deviceStationService).orElseGet(() -> deviceStationService = ApplicationContextUtils.get(DeviceStationService.class));
        Optional.ofNullable(iotLcdStatusService).orElseGet(() -> iotLcdStatusService = ApplicationContextUtils.get(IotLcdsStatusService.class));
        //判断lcd设备是否为空 如果为空就去查询
        Optional.ofNullable(allStatusByRedis).orElseGet(() -> {
            Optional.ofNullable(loopStatusServiceApi).orElseGet(() -> loopStatusServiceApi = ApplicationContextUtils.get(LoopStatusServiceApi.class));
            allStatusByRedis = iotLcdStatusService.findAllStatusByRedis();
            return allStatusByRedis;
        });
        //判断该设备是否在该站台下
        allStatusByRedis.stream().filter(iotLcdStatus -> stationid.equals(deviceStationService.selectOne(new DeviceStation(iotLcdStatus.getId())).getStationid()))
                .forEach(iotLcdStatus -> {
                    Devices device = new Devices(iotLcdStatus);
                    devices.add(device);
                });
    }

    /**
     * 照明首次连接 同时也定时推送
     */
    @Scheduled(cron = "${send.light-cron}")
    public void light() throws ParseException {
        Integer modulecode = getModuleCode("light");
        if (SESSION_MAP.get(String.valueOf(modulecode)) != null) {
            MessageVo messageVo = setMessageVo(modulecode);
            messageVo.setMsgcode(203001);
            //调用接口 获取当前照明状态
            Optional.ofNullable(loopStatusServiceApi).orElseGet(() -> loopStatusServiceApi = ApplicationContextUtils.get(LoopStatusServiceApi.class));
            loopStatus = loopStatusServiceApi.findLoopStatus();
            List<LightMsg> lightMsgList = new ArrayList<>();
            //保存设备状态信息
            LightMsgState lightMsgState = new LightMsgState();
            if (loopStatus != null && loopStatus.size() != 0) {
                //灯箱设备
                List<Lights> lamphouses = new ArrayList<>();
                //顶棚
                List<Lights> platfonds = new ArrayList<>();
                //log
                List<Lights> logos = new ArrayList<>();
                loopStatus.forEach(tLoopStateDto -> {
                    //判断这个回路下是什么设备
                    LightMsg lightMsg = new LightMsg(new Lights(tLoopStateDto));
                    lightMsgList.add(lightMsg);
                    //判断这是什么设备
                    if (lightMsg.getLights().getLamphouseid() != null) {
                        lamphouses.add(lightMsg.getLights());
                    }
                    if (lightMsg.getLights().getPlatfondid() != null) {
                        platfonds.add(lightMsg.getLights());
                    }
                    if (lightMsg.getLights().getLogoid() != null) {
                        logos.add(lightMsg.getLights());
                    }
                });
                messageVo.setMsg(lightMsgList);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));
                //推送设备状态信息
                lightMsgState.setLamphouses(lamphouses);
                lightMsgState.setPlatfonds(platfonds);
                lightMsgState.setLogos(logos);
                messageVo.setMsg(lightMsgState);
                messageVo.setMsgcode(203004);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));

                //推送统计信息
                StatisticsMsgVo statisticsMsgVo = lightStatis(loopStatus);
                messageVo.setMsg(statisticsMsgVo);
                messageVo.setMsgcode(205002);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));
                log.info("照明定时任务时间 : {}", messageVo.getTimestamp());
            }
        }
    }

    /**
     * 照明设备统计信息
     *
     * @param loopStatus 照明设备信息
     * @return 统计信息
     */
    private StatisticsMsgVo lightStatis(List<TLoopStateDto> loopStatus) throws ParseException {

        List<String> dids = new ArrayList<>();
        //取出所有的did
        loopStatus.forEach(loopStateDto -> {
            //通过回路号查询这个是什么设备
            DeviceLoopService deviceLoopService = ApplicationContextUtils.get(DeviceLoopService.class);
            DeviceLoop deviceLoopSelect = new DeviceLoop();
            deviceLoopSelect.setLoop(loopStateDto.getLoop());
            DeviceLoop deviceLoop = deviceLoopService.selectOne(deviceLoopSelect);
            dids.add(String.valueOf(deviceLoop.getDeviceDid()));

        });
        //统计
        return equipStatis(dids);
    }


    /**
     * lcd首次连接信息 也需要定时向前台推送
     */
    @Scheduled(cron = "${send.lcd-cron}")
    public void lcd() throws ParseException {
        //查出led的 moduleCode
        Integer modulecode = getModuleCode("lcd");
        if (SESSION_MAP.get(String.valueOf(modulecode)) != null) {
            MessageVo messageVo = setMessageVo(modulecode);
            //调用接口 获得所有站屏的设备状态
            messageVo.setMsgcode(204001);
            Optional.ofNullable(iotLcdStatusService).orElseGet(() -> iotLcdStatusService = ApplicationContextUtils.get(IotLcdsStatusService.class));
            this.allStatusByRedis = iotLcdStatusService.findAllStatusByRedis();
            if (!allStatusByRedis.isEmpty()) {
                Lcds lcds = new Lcds(allStatusByRedis);
                LcdMsg lcdMsg = new LcdMsg(lcds);
                messageVo.setMsg(lcdMsg);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));

                //推送设备状态 将多余字段设置为 null
                allStatusByRedis.forEach(iotLcdStatus -> iotLcdStatus.setVolume(null));
                Lcds lcds2 = new Lcds(allStatusByRedis);
                LcdMsg lcdMsg2 = new LcdMsg(lcds2);
                messageVo.setMsg(lcdMsg2);
                messageVo.setMsgcode(204004);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));

                //推送统计信息
                StatisticsMsgVo statisticsMsgVo = lcdStatis(allStatusByRedis);
                messageVo.setMsg(statisticsMsgVo);
                messageVo.setMsgcode(204002);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));

                //推送lcd设备统计信息
                log.info("lcd定时任务时间 : {}", messageVo.getTimestamp());
            }
        }
    }

    /**
     * lcd设备统计信息
     *
     * @param allStatusByRedis 设备did
     * @return 能耗信息
     */
    private StatisticsMsgVo lcdStatis(List<IotLcdStatusTwo> allStatusByRedis) throws ParseException {

        List<String> dids = new ArrayList<>();
        //取出所有的did
        allStatusByRedis.forEach(iotLcdStatus -> dids.add(iotLcdStatus.getId()));
        //统计
        return equipStatis(dids);
    }

    /**
     * led首次连接信息 也需要定时向前台推送
     */
    @Scheduled(cron = "${send.led-cron}")
    public void led() throws ParseException {
        //查出led的 moduleCode
        Integer modulecode = getModuleCode("led");
        if (SESSION_MAP.get(String.valueOf(modulecode)) != null) {
            MessageVo messageVo = setMessageVo(modulecode);
            messageVo.setMsgcode(205001);
            //调用接口
            Optional.ofNullable(tStatusService).orElseGet(() -> tStatusService = ApplicationContextUtils.get(TStatusService.class));
            allStatus = tStatusService.findAllStatusByRedis();
            if (!allStatus.isEmpty()) {
                Leds leds = new Leds(allStatus);
                LedMsg ledMsg = new LedMsg(leds);
                messageVo.setMsg(ledMsg);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));

                allStatus.forEach(tStatusDto -> {
                    tStatusDto.setVolume(null);
                    tStatusDto.setLighteness(null);
                });
                Leds leds2 = new Leds(allStatus);
                LedMsg ledMsg2 = new LedMsg(leds2);
                messageVo.setMsg(ledMsg2);
                messageVo.setMsgcode(205004);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));

                //推送统计信息
                StatisticsMsgVo statisticsMsgVo = ledStatis(allStatus);
                messageVo.setMsg(statisticsMsgVo);
                messageVo.setMsgcode(205002);
                send(String.valueOf(modulecode), JSON.toJSONString(messageVo));

                log.info("led定时任务时间 : {}", messageVo.getTimestamp());
            }
        }
    }

    /**
     * led设备统计信息
     *
     * @param allStatus led信息
     * @return 统计信息
     */
    private StatisticsMsgVo ledStatis(List<TStatusDto> allStatus) throws ParseException {

        List<String> dids = new ArrayList<>();
        //取出所有的did
        allStatus.forEach(tStatusDto -> dids.add(tStatusDto.getDid()));
        //统计
        return equipStatis(dids);

    }


    /**
     * 关闭连接的方法
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("有连接关闭");
        Map<String, Session> map = new HashMap<>(16);
        SESSION_MAP.forEach((k, v) -> {
            if (!session.getId().equals(v.getId())) {
                map.put(k, v);
            }
        });
        SESSION_MAP = map;
        //删除缓存
        Optional.ofNullable(redisTemplate).orElseGet(() -> redisTemplate = ApplicationContextUtils.get(StringRedisTemplate.class));
        redisTemplate.opsForHash().delete("web_socket_key");
        log.info("断开连接后session数量 : {}", SESSION_MAP.size());
    }

    /**
     * 发送消息的方法
     *
     * @param token   modulecode
     * @param message 要发送的信息
     */
    public void send(String token, String message) {

        if (StringUtils.isNotBlank(token)) {
            //推送消息到页面
            Optional.ofNullable(SESSION_MAP.get(token)).ifPresent(session -> {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("消息推送失败 : {}", e.getMessage());
                }
            });
            //删除缓存
            Optional.ofNullable(redisTemplate).orElseGet(() -> redisTemplate = ApplicationContextUtils.get(StringRedisTemplate.class));
            redisTemplate.opsForHash().delete("web_socket_key", JSON.parseObject(message, Message.class).getMsgid());
        }
    }

    /**
     * 发生错误调用的方法
     *
     * @param session session
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误 sessionId : {} ", session.getId());
        error.printStackTrace();
    }

    /**
     * 将时间格式化为 yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param date 要格式化的时间
     * @return 格式化的结果
     */
    private String dateFormat(Date date) {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
    }

    /**
     * 提取重复代码
     *
     * @param modulecode modulecode
     * @return MessageVo
     */
    private MessageVo setMessageVo(Integer modulecode) {
        //拼装消息
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgid(UUID.randomUUID().toString());
        messageVo.setModulecode(modulecode);
        messageVo.setMsgtype(1);
        messageVo.setTimestamp(dateFormat(new Date()));

        return messageVo;
    }

    /**
     * 提取重复代码 获取Modulecode
     *
     * @return Modulecode
     */
    private Integer getModuleCode(String equipment) {
        Optional.ofNullable(mqMessageService).orElseGet(() -> mqMessageService = ApplicationContextUtils.get(MqMessageService.class));
        MqMessage mqMessageSelect = new MqMessage();
        mqMessageSelect.setExchange(equipment);
        return mqMessageService.selectOne(mqMessageSelect).getModulecode();
    }

    /**
     * 提取重复代码
     *
     * @param dids 设备did
     * @return 统计信息
     * @throws ParseException 时间格式化异常
     */
    private StatisticsMsgVo equipStatis(List<String> dids) throws ParseException {

        Optional.ofNullable(deviceLoopService).orElseGet(() -> deviceLoopService = ApplicationContextUtils.get(DeviceLoopService.class));
        //遍历通过did查出回路
        DeviceLoop deviceLoopSelect = new DeviceLoop();
        //本月
        float currentmonth = 0;
        //上月
        float lastmonth = 0;
        //本年
        float thisyear = 0;
        for (String did : dids) {
            deviceLoopSelect.setDeviceDid(Integer.valueOf(did));
            DeviceLoop deviceLoop = deviceLoopService.selectOne(deviceLoopSelect);
            //查出单个设备的统计信息
            StatisticsVo statisticsVoSelect = new StatisticsVo();
            statisticsVoSelect.setDid(String.valueOf(deviceLoop.getDeviceDid()));
            statisticsVoSelect.setLoop(deviceLoop.getLoop());
            StatisticsMsgVo statistics = Statistics.findStatistics(statisticsVoSelect);
            currentmonth = currentmonth + statistics.getCurrentmonth();
            lastmonth = lastmonth + statistics.getLastmonth();
            thisyear = thisyear + statistics.getThisyear();
        }
        return new StatisticsMsgVo(currentmonth, lastmonth, thisyear);
    }
}
