package br.com.magic.application.exception;

import br.com.magic.application.commons.MagicErrorCode;

public class InsufficientMana extends BaseException {
    private MagicErrorCode code;
    private String player;

    public InsufficientMana() {
    }

    public InsufficientMana(MagicErrorCode code, String player) {
        super(code);
        this.code = code;
        this.player = player;
    }

    @Override
    public MagicErrorCode getCode() {
        return code;
    }

    @Override
    public void setCode(MagicErrorCode code) {
        this.code = code;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
