package com.shuzhi.eum;

/**
 * @author zgk
 * @description
 * @date 2019-07-04 15:31
 */
public enum WebEum {

    /**
     * 注册失败
     */
    REGISTERED_ERROR_1(10001, "注册失败,用户名不能为空"),

    REGISTERED_ERROR_2(10002, "注册失败,密码不能为空"),

    REGISTERED_ERROR_3(10003, "注册失败,该用户名已存在"),

    REGISTERED_ERROR_4(10004, "注册失败,登录名不能为空"),

    REGISTERED_ERROR_5(10005, "注册失败,该登录名已存在"),

    REGISTERED_ERROR_6(10006, "更新失败,该用户不存在"),

    /**
     * 角色失败
     */
    ROLE_ERROR_1(20001, "保存失败,角色名不能个为空"),

    ROLE_ERROR_2(20002, "保存失败,角色名已存在"),

    ROLE_ERROR_3(20003, "保存失败,角色编号不能为空"),

    ROLE_ERROR_4(20004, "保存失败,角色编号已存在"),

    ROLE_ERROR_5(20005, "删除失败,该角色已被用户或目录使用"),





    /**
     * 目录失败
     */
    MENU_ERROR_1(30001, "保存失败,url不能为空"),

    MENU_ERROR_2(30002, "保存失败,该url已配置"),

    MENU_ERROR_3(30003, "保存失败,目录名称不能为空"),

    MENU_ERROR_4(30004, "保存失败,该目录名称已存在"),

    MENU_ERROR_5(30005, "目录列表为空"),

    MENU_ERROR_6(30006, "更新失败,要更新的目录不存在"),

    /**
     * 资源失败
     */
    MATERIAL_ERROR_1(40001, "保存失败,目录名不能为空"),

    MATERIAL_ERROR_2(40002, "保存失败,目录名已存在"),

    MATERIAL_ERROR_3(40003, "保存失败,目录名类型不能为空"),

    MATERIAL_ERROR_4(40004, "更新失败,要更新的目录不存在"),

    MATERIAL_ERROR_5(40005, "上传失败,请选择文件"),

    MATERIAL_ERROR_6(40006, "上传失败,保存路径不能为空"),

    MATERIAL_ERROR_7(40007, "上传失败,请重试"),

    MATERIAL_ERROR_8(40008, "上传失败,文件夹创建失败"),

    MATERIAL_ERROR_9(40009, "上传失败,文件名已存在"),

    MATERIAL_ERROR_10(40010, "删除失败,请重试"),

    MATERIAL_ERROR_11(40011, "查询失败,目录id不能为空"),

    MATERIAL_ERROR_12(40012, "查询失败,资源类型id不能为空"),

    MATERIAL_ERROR_13(40013, "更新失败,id不能为空"),

    MATERIAL_ERROR_14(40014, "上传失败,类型id不能为空"),


    /**
     * 节目发布
     */
    SHOW_RECORDS_ERROR_01(50001, "设备类型不能为空"),

    SHOW_RECORDS_ERROR_02(50002, "节目名称不能为空"),

    SHOW_RECORDS_ERROR_03(50003, "节目类型不能为空"),

    SHOW_RECORDS_ERROR_04(50004, "节目类型错误"),

    SHOW_RECORDS_ERROR_05(50005, "发布节目失败, 分组id不能为空"),

    SHOW_RECORDS_ERROR_06(50006, "发布节目失败, 节目资源不能为空"),

    SHOW_RECORDS_ERROR_07(50007, "节目名称重复"),

    SHOW_RECORDS_ERROR_08(50008, "节目分组不能为空"),

    /**
     * 分组
     */

    GROUP_ERROR_01(60001, "分组名称不能为空"),

    GROUP_ERROR_02(60002, "分组名称重复"),

    GROUP_ERROR_03(60003, "要修改的信息不存在"),

    GROUP_ERROR_04(60004, "获取分组目录失败, 设备类型不能为空"),

    GROUP_ERROR_05(60005, "请选择设备"),

    GROUP_ERROR_06(60006, "分组id不能为空"),

    ;

    /**
     * The Type.
     */
    int code;
    /**
     * The Name.
     */
    String msg;

    WebEum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getMsg() {
        return msg;
    }

}
