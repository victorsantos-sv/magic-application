package br.com.magic.application.entity.dto;

public class PlayerDTO {
    private Long id;
    private String nickName;
    private Integer mana = 20;
    private Integer life = 20;

    public PlayerDTO() {
    }

    public PlayerDTO(Long id, String nickName, Integer mana, Integer life) {
        this.id = id;
        this.nickName = nickName;
        this.mana = mana;
        this.life = life;
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

    public Integer getMana() {
        return mana;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }
}
