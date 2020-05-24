package br.com.magic.application.exception.response;

public class ErroResponse {
    private String code;
    private String message;

    public ErroResponse() {
    }

    public ErroResponse(String code, String message) {
        this.code = code;
        this.message = message;
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
}
