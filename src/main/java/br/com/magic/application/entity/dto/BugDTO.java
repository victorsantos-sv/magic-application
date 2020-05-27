package br.com.magic.application.entity.dto;

public class BugDTO {
    private Long id;
    private Integer life = 20;
    private Integer mana = 20;

    public BugDTO() {
    }

    public BugDTO(Long id, Integer life, Integer mana) {
        this.id = id;
        this.life = life;
        this.mana = mana;
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
}
