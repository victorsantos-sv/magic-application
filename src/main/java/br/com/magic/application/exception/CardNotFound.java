package br.com.magic.application.exception;

import br.com.magic.application.commons.MagicErrorCode;

public class CardNotFound extends BaseNotFoundException {
    private MagicErrorCode code;

    public CardNotFound() {
    }

    public CardNotFound(MagicErrorCode code) {
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
