package com.cloudinfo.hogwartsartifact.system;

import lombok.Data;

@Data

public class Response {
    private boolean flag;
    private Integer code;
    private String message;
    private Object data;

    public Response(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Response(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
