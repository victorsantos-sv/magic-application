package br.com.magic.application.commons;

public class MagicErrorCode {
    private final String code;
    private final String key;

    public MagicErrorCode(String code, String key) {
        this.code = code;
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    public static final MagicErrorCode MEC001 = new MagicErrorCode("MEC001", "player.not.found");
    public static final MagicErrorCode MEC002 = new MagicErrorCode("MEC002", "full.cards");
    public static final MagicErrorCode MEC003 = new MagicErrorCode("MEC003", "invalid.argument");
    public static final MagicErrorCode MEC004 = new MagicErrorCode("MEC004", "card.not.found");
    public static final MagicErrorCode MEC005 = new MagicErrorCode("MEC005", "bug.not.found");
    public static final MagicErrorCode MEC006 = new MagicErrorCode("MEC006", "insufficient.mana");
}
