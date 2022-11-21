package com.wild.yygh.common.exception;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义全局异常类
 *
 * @author qy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "自定义全局异常类")
public class YyghException extends RuntimeException {

    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    private String msg;

}
