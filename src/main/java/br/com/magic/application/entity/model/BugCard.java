package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bug_card")
public class BugCard extends Card {
    @Column(name = "mana_damage")
    private Integer manaDamage;

    @Column(name = "is_in_use")
    private Boolean isInUse;

    public BugCard() {
    }

    public BugCard(Long id, String title, String description, Integer cost, Integer lifeDamage, Integer manaDamage, Boolean isInUse) {
        super(id, title, description, cost, lifeDamage);
        this.manaDamage = manaDamage;
        this.isInUse = isInUse;
    }

    public Integer getManaDamage() {
        return manaDamage;
    }

    public void setManaDamage(Integer manaDamage) {
        this.manaDamage = manaDamage;
    }

    public Boolean getInUse() {
        return isInUse;
    }

    public void setInUse(Boolean inUse) {
        isInUse = inUse;
    }
}
