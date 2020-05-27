package br.com.magic.application.entity.dto;

public class ScoreboardPlayerDTO {

    private Long id;
    private Long idCard;
    private Integer life;
    private Integer mana;

    public ScoreboardPlayerDTO() {
    }

    public ScoreboardPlayerDTO(Long id, Integer life, Integer mana, Long idCard) {
        this.id = id;
        this.idCard = idCard;
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

    public Long getIdCardBug() {
        return idCard;
    }

    public void setIdCardBug(Long idCard) {
        this.idCard = idCard;
    }
}
