package com.odnolap.model.undertow;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int errorCode;
    private UUID errorToken;
    private String errorMessage;
}
