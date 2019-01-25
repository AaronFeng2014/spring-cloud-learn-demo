package com.aaron.springcloud.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获，用户捕获在业务中未捕获到的异常， 增强用户体验
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018/6/2
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler (Exception.class)
    public void handleException(Exception e)
    {
        LOGGER.error("捕获到全局异常，异常堆栈信息", e);

    }


    @ExceptionHandler (Exception.class)
    public void businessException(Exception e)
    {
        LOGGER.error("捕获到业务异常信息：{}", e.getMessage());
    }


    @ExceptionHandler ({MethodArgumentNotValidException.class, BindException.class})
    public void methodArgumentNotValidException(Exception e)
    {
        LOGGER.error("接口请求参数异常", e);

        BindingResult bindingResult = getBindingResult(e);

        StringBuilder stringBuilder = new StringBuilder();

        if (bindingResult.hasErrors())
        {
            for (ObjectError error : bindingResult.getAllErrors())
            {
                stringBuilder.append(error.getDefaultMessage()).append("\n");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
    }


    private BindingResult getBindingResult(Exception e)
    {
        BindingResult bindingResult = null;

        if (e instanceof MethodArgumentNotValidException)
        {

            bindingResult = ((MethodArgumentNotValidException)e).getBindingResult();
        }
        else if (e instanceof BindException)
        {
            bindingResult = ((BindException)e).getBindingResult();
        }

        return bindingResult;
    }

}
