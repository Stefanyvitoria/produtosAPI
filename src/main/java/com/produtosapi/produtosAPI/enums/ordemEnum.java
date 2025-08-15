package com.produtosapi.produtosAPI.enums;

public enum ordemEnum {
    ASC,  // Ascendente
    DESC; // Descendente

    public static ordemEnum fromString(String value) {
        if (value == null) {
            return ASC; // padrão
        }
        
        try {
            return ordemEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ASC; // padrão se valor inválido
        }
    }
}
