package com.github.nicqiang.pointcloud.common;

import lombok.Data;

import java.io.Serializable;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
@Data
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = -5732939305503307980L;

    /**
     * 返回码
     */
    private int code = 200;

    /**
     * 异常信息
     */
    private String errMsg;

    /**
     * 返回体
     */
    private Object data;

    public BaseResponse(Object data) {
        this.data = data;
    }

    public BaseResponse() {
    }

    public BaseResponse(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }
}
