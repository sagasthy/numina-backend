package com.kar.numina.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuccessResponse<T> {
    private T data;
    private String message;
    private int status;
}

