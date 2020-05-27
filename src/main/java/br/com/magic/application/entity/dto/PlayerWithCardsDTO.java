package br.com.magic.application.entity.dto;

import java.util.List;

public class PlayerWithCardsDTO {
    private Long id;
    private String nickName;
    private Integer life;
    private Integer mana;
    private List<JuniorCardDTO> cards;

    public PlayerWithCardsDTO() {
    }

    public PlayerWithCardsDTO(Long id, String nickName, Integer life, Integer mana, List<JuniorCardDTO> cards) {
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

    public List<JuniorCardDTO> getCards() {
        return cards;
    }

    public void setCards(List<JuniorCardDTO> cards) {
        this.cards = cards;
    }
}
