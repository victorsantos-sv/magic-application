package br.com.magic.application.api.response;

import java.util.List;

public class BugWithCardResponse {
    private Long id;
    private Integer life;
    private Integer mana;
    private List<BugCardResponse> cards;

    public BugWithCardResponse() {
    }

    public BugWithCardResponse(Long id, Integer life, Integer mana, List<BugCardResponse> cards) {
        this.id = id;
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

    public List<BugCardResponse> getCards() {
        return cards;
    }

    public void setCards(List<BugCardResponse> cards) {
        this.cards = cards;
    }
}
