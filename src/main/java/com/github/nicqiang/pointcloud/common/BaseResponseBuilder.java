package com.github.nicqiang.pointcloud.common;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
public class BaseResponseBuilder {

    public static BaseResponse success(){
        return new BaseResponse();
    }

    public static BaseResponse success(Object data){
        return new BaseResponse(data);
    }

    public static BaseResponse exception(int code, String errMsg){
        return new BaseResponse(code, errMsg);
    }
}
