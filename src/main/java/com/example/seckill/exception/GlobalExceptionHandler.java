package com.example.seckill.exception;

import com.example.seckill.result.CodeMsg;
import com.example.seckill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//拦截处理全局异常
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
        e.printStackTrace();
        if(e instanceof BindException){
            BindException bindException = (BindException)e;

            List<ObjectError> allErrors = bindException.getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String msg = objectError.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }
        else if(e instanceof GlobalException){
            CodeMsg cm = ((GlobalException) e).getCm();
            return Result.error(cm);
        }
        else{
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
