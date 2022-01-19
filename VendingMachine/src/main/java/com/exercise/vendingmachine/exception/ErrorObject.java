package com.exercise.vendingmachine.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorObject {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

}
