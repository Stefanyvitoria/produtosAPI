package com.produtosapi.produtosAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Gera getters, setters, toString, equals, etc.
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAPI<T> {
    
    private int code;
    private String status;
    private String message;
    private T data; // Pode ser qualquer objeto, lista ou até null
}
