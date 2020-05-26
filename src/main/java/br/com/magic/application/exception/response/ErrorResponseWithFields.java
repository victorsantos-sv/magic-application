package br.com.magic.application.exception.response;

import java.util.List;
import java.util.Map;

public class ErrorResponseWithFields {
    private String code;
    private String message;
    private List<Map<String, String>> fields;

    public ErrorResponseWithFields() {
    }

    public ErrorResponseWithFields(String code, String message, List<Map<String, String>> fields) {
        this.code = code;
        this.message = message;
        this.fields = fields;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, String>> getFields() {
        return fields;
    }

    public void setFields(List<Map<String, String>> fields) {
        this.fields = fields;
    }
}
