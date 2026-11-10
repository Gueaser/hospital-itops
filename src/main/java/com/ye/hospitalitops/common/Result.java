package com.ye.hospitalitops.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Result<T> {
    private  Integer code;
    private  String  message;
    private T data;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime timestamp;
    public static <T> Result<T>ok(T data){
        Result <T> result = new Result<>();
        result.code=0;
        result.message="成功";
        result.data=data;
        result.timestamp=LocalDateTime.now();
        return result;
    }
    public static <T> Result<T> ok(){
        return ok(null);
    }
    public static <T> Result<T> fail(int code,String message){
        Result <T> result = new Result<>();
        result.code=code;
        result.message=message;
        result.timestamp=LocalDateTime.now();
        return result;
    }

}
