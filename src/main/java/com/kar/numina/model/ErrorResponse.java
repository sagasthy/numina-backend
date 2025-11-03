package com.kar.numina.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse extends Response {
    private String message;
    private Integer statusCode;
}
