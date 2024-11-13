package com.myappproducer.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseDto<T> {
    private Integer code;
    private String message;
    private T data;
    private LocalDateTime timestamp;


}
