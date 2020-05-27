package br.com.magic.application.exception;

import br.com.magic.application.commons.MagicErrorCode;

public class BugNotFound extends BaseNotFoundException {
    private MagicErrorCode code;

    public BugNotFound() {
    }

    public BugNotFound(MagicErrorCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public MagicErrorCode getCode() {
        return code;
    }

    @Override
    public void setCode(MagicErrorCode code) {
        this.code = code;
    }
}