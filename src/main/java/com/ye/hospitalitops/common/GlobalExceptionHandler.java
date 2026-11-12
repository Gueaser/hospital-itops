package com.ye.hospitalitops.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValid(MethodArgumentNotValidException e){
        StringBuilder sb=new StringBuilder();
        for(FieldError error:e.getBindingResult().getFieldErrors()){//getBindingResult()从异常对象e中提取出包含错误信息的BindingResult对象，getFieldErrors()只获取具体字段类错误，返回一个List<FieldError>
            sb.append(error.getDefaultMessage());
            sb.append(";");
        }
        sb.setLength(sb.length()-1);
        String combinedMessage=sb.toString();
        return ResponseEntity.status(400).body(Result.fail(400,combinedMessage));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> handleUnreadable(HttpMessageNotReadableException e){//Void是void的包装类，代表这是一个类，因为这是返回错误，没有具体的data的字段
        String rawMessage=e.getMostSpecificCause().getMessage();
        String finalMessage="请求体解析失败:"+(rawMessage!=null&&rawMessage.length()>80?rawMessage.substring(0,80)+"...":(rawMessage!=null?rawMessage:"")) ;
        return ResponseEntity.status(400).body(Result.fail(400,finalMessage));
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Result<Void>> handleDuplicate(DuplicateKeyException e) {
        return ResponseEntity.status(409).body(Result.fail(409,"资产编号已存在"));
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    public  ResponseEntity<Result<Void>> handleControllerValidation(HandlerMethodValidationException e){
        StringBuilder sb=new StringBuilder();
        for(MessageSourceResolvable error:e.getAllErrors()){//getAllErrors()返回全局和字段错误 返回List<ObjectError>？这个版本好像是List<MessageSourceResolvable>
            sb.append(error.getDefaultMessage());
            sb.append(";");
        }
        sb.setLength(sb.length()-1);
        String combinedMessage=sb.toString();
        return ResponseEntity.status(400).body(Result.fail(400,combinedMessage));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleMismatch(MethodArgumentTypeMismatchException e){
        String exceptedType= e.getRequiredType()!=null?e.getRequiredType().getSimpleName():"正确类型";
        String combinedMessage= "参数" + e.getName() + "必须为" + exceptedType;
        return ResponseEntity.status(400).body(Result.fail(400,combinedMessage));
    }
    @ExceptionHandler(BizException.class)
    public ResponseEntity<Result<Void>> handleBiz(BizException e){
        return ResponseEntity.status(e.getCode()).body(Result.fail(e.getCode(),e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleOther(Exception e){
        log.error("未被捕获的异常",e);
        return ResponseEntity.status(500).body(Result.fail(500,"服务器内部错误"));
    }
}
