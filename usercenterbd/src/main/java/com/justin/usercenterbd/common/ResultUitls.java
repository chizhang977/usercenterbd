package com.justin.usercenterbd.common;

/**
 * 返回工具类
 * @author justin
 *
 */
public class ResultUitls {
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data,"success");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }
    public static <T> BaseResponse<T> error(int code,String message,String description) {
        return new BaseResponse<>(code,null,message,description);

    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode,String message,String description) {
        return new BaseResponse(errorCode.getCode(),null,message,description);
    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode,String description) {
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),description);
    }
}
