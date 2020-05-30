package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Card {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "life_damage")
    private Integer lifeDamage;

    public Card() {
    }

    public Card(Long id, String title, String description, Integer cost, Integer lifeDamage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.lifeDamage = lifeDamage;
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
}
