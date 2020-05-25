package br.com.magic.application.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude
public class JuniorCardResponse {
    private Long id;
    private String title;
    private String description;
    private Integer cost;
    private Integer lifeDamage;
    private Integer passive;

    public JuniorCardResponse() {
    }

    public JuniorCardResponse(Long id, String title, String description, Integer cost, Integer lifeDamage, Integer passive) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.lifeDamage = lifeDamage;
        this.passive = passive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getLifeDamage() {
        return lifeDamage;
    }

    public void setLifeDamage(Integer lifeDamage) {
        this.lifeDamage = lifeDamage;
    }

    public Integer getPassive() {
        return passive;
    }

    public void setPassive(Integer passive) {
        this.passive = passive;
    }
}
