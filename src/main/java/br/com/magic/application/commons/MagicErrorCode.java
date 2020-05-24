package br.com.magic.application.commons;

public class MagicErrorCode {
    private String code;
    private String key;

    public MagicErrorCode(String code, String key) {
        this.code = code;
        this.key = key;
    }

    public static final MagicErrorCode MEC001 = new MagicErrorCode("MEC001", "player.not.found");
}
