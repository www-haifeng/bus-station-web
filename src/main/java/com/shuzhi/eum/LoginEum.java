package com.shuzhi.eum;

/**
 * @author zgk
 * @description
 * @date 2019-07-04 15:31
 */
public enum  LoginEum {

    /**
     * 注册失败
     */
    REGISTERED_ERROR_1(10001, "注册失败,用户名不能为空"),

    REGISTERED_ERROR_2(10002, "注册失败,密码不能为空"),

    REGISTERED_ERROR_3(10003, "注册失败,该用户名已存在"),

    REGISTERED_ERROR_4(10004, "注册失败,登录名不能为空"),

    REGISTERED_ERROR_5(10005, "注册失败,该登录名已存在"),


    ROLE_ERROR_1(20001, "保存失败,角色名不能个为空"),

    ROLE_ERROR_2(10002, "保存失败,角色名已存在"),

    ROLE_ERROR_3(10003, "保存失败,角色编号不能为空"),

    ROLE_ERROR_4(10004, "保存失败,角色编号已存在"),

            ;

    /**
     * The Type.
     */
    int code;
    /**
     * The Name.
     */
    String msg;

    LoginEum(int code, String msg) {
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
