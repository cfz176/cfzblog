package com.cfz.handler.exception;

import com.cfz.ResponseResult;
import com.cfz.enums.AppHttpCodeEnum;
import com.cfz.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

//@ControllerAdvice
//@ResponseBody
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({SystemException.class})
    public ResponseResult systemExceptionHandler(SystemException e) {
        //打印异常信息
        log.error("出现异常！{}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult hanleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors){
            sb.append(fieldError.getDefaultMessage()).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,sb.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        //打印异常信息
        log.error("出现异常！{}",e);
        //从异常对象中获取提示下信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,e.getMessage());
    }

}
