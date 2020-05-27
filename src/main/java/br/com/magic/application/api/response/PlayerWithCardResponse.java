package br.com.magic.application.api.response;

import java.util.List;

public class PlayerWithCardResponse {
    private Long id;
    private String nickName;
    private Integer life;
    private Integer mana;
    private List<JuniorCardResponse> cards;

    public PlayerWithCardResponse() {
    }

    public PlayerWithCardResponse(Long id, String nickName, Integer life, Integer mana, List<JuniorCardResponse> cards) {
        this.id = id;
        this.nickName = nickName;
        this.life = life;
        this.mana = mana;
        this.cards = cards;
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

    public List<JuniorCardResponse> getCards() {
        return cards;
    }

    public void setCards(List<JuniorCardResponse> cards) {
        this.cards = cards;
    }
}
