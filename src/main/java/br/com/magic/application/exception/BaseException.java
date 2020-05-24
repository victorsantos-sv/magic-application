package br.com.magic.application.exception;

import br.com.magic.application.commons.MagicErrorCode;

public class BaseException  extends RuntimeException {
    private MagicErrorCode code;

    public BaseException() {
    }

    public BaseException(MagicErrorCode code) {
        this.code = code;
    }

    public MagicErrorCode getCode() {
        return code;
    }

    public void setCode(MagicErrorCode code) {
        this.code = code;
    }
}
