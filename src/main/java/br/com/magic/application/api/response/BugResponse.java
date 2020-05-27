package br.com.magic.application.api.response;

public class BugResponse {
    private Long id;
    private Integer life;
    private Integer mana;
    private BugCardResponse card;

    public BugResponse() {
    }

    public BugResponse(Long id, Integer life, Integer mana) {
        this.id = id;
        this.life = life;
        this.mana = mana;
    }

    public BugResponse(Long id, Integer life, Integer mana, BugCardResponse card) {
        this.id = id;
        this.life = life;
        this.mana = mana;
        this.card = card;
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

    public BugCardResponse getCard() {
        return card;
    }

    public void setCard(BugCardResponse card) {
        this.card = card;
    }
}
