package br.com.magic.application.api.response;

public class PlayerResponse {
    private Long id;
    private String nickName;
    private Integer life;
    private Integer mana;
    private JuniorCardResponse juniorCard;

    public PlayerResponse() {
    }

    public PlayerResponse(Long id, String nickName, Integer life, Integer mana) {
        this.id = id;
        this.nickName = nickName;
        this.life = life;
        this.mana = mana;
    }

    public PlayerResponse(Long id, String nickName, Integer life, Integer mana, JuniorCardResponse juniorCard) {
        this.id = id;
        this.nickName = nickName;
        this.life = life;
        this.mana = mana;
        this.juniorCard = juniorCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Integer getMana() {
        return mana;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public JuniorCardResponse getJuniorCard() {
        return juniorCard;
    }

    public void setJuniorCard(JuniorCardResponse juniorCard) {
        this.juniorCard = juniorCard;
    }
}
