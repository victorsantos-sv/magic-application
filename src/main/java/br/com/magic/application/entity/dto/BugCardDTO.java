package br.com.magic.application.entity.dto;

public class BugCardDTO {

    private Long id;
    private String title;
    private String description;
    private Integer cost;
    private Integer lifeDamage;
    private Integer manaDamage;

    public BugCardDTO() {
    }

    public BugCardDTO(Long id, String title, String description, Integer cost, Integer lifeDamage, Integer manaDamage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.lifeDamage = lifeDamage;
        this.manaDamage = manaDamage;
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

    public Integer getManaDamage() {
        return manaDamage;
    }

    public void setManaDamage(Integer manaDamage) {
        this.manaDamage = manaDamage;
    }
}
