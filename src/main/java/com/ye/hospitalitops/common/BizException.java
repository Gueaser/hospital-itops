package com.ye.hospitalitops.common;

public class BizException extends  RuntimeException {//RuntimeException:运行时异常 是非受检 不用在每层 throws；Exception不是
    private  final int code;
    public BizException(int code,String message){
        super(message);
        this.code=code;
    }
    public int getCode(){
        return  code;
    }
}
