package com.tyd.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * customaize expetion
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomaizeExpetion extends RuntimeException{

    private Integer code;

    private String message;

    public CustomaizeExpetion(ExceptionEnum exceptionEnum){
        this.code = exceptionEnum.code;
        this.message = exceptionEnum.message;
    }

}
