package com.github.lazyboyl.websocket.server.channel.entity;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述： socket的请求返回response
 */
public class SocketResponse {

    public SocketResponse() {
        super();
    }

    public SocketResponse(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public SocketResponse(Integer code, String msg, Object obj) {
        this.msg = msg;
        this.code = code;
        this.obj = obj;
    }

    /**
     * 返回的错误码
     */
    private Integer code;

    /**
     * 返回的错误信息
     */
    private String msg;

    /**
     * 返回到前端的相关数据
     */
    private Object obj;


    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
