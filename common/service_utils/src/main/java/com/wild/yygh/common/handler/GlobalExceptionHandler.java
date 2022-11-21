package com.wild.yygh.common.handler;


import com.wild.yygh.common.R;
import com.wild.yygh.common.exception.YyghException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
//@ControllerAdvice
@Slf4j
@RestControllerAdvice // @RestControllerAdvice = @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {


	@ExceptionHandler(Exception.class)
	//@ResponseBody
	public R error(Exception e){
		e.printStackTrace();
		log.error(e.getMessage());
		return R.error();
	}
	/**
	 * 特殊异常处理
	 */
	@ExceptionHandler(ArithmeticException.class)
	//@ResponseBody
	public R error(ArithmeticException e){
		e.printStackTrace();
		log.error(e.getMessage());
		return R.error().message("特殊异常处理");
	}
	/**
	 * 自定义异常处理
	 */
	@ExceptionHandler(YyghException.class)
	//@ResponseBody
	public R error(YyghException e){
		e.printStackTrace();
		log.error(e.getMessage());
		return R.error().code(e.getCode()).message(e.getMsg());
	}
}