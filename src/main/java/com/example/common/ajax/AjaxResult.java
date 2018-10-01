package com.example.common.ajax;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AjaxResult<T> implements Serializable {

    private Integer status;

    private Integer rst;

    private String msg;

    private T data;

    private String exception;

    private String error;

    private String path;

    private Date timestamp;

    private Integer total;

    public AjaxResult() {
    }

    public AjaxResult(Integer status, Integer rst) {
        this.status = status;
        this.rst = rst;
        this.setMsg("SUCCESS");
        this.timestamp = new Date();
    }

}
