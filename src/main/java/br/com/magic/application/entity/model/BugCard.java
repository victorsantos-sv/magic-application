package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bug_card")
public class BugCard extends Card {
    @Column(name = "mana_damage")
    private Integer manaDamage;

    @ManyToOne(targetEntity = Bug.class)
    @JoinColumn(name = "bug_id", nullable = true)
    private Bug bug;

    public BugCard() {
    }

    public BugCard(Long id, String title, String description, Integer cost, Integer lifeDamage, Integer manaDamage, Bug bug) {
        super(id, title, description, cost, lifeDamage);
        this.manaDamage = manaDamage;
        this.bug = bug;
    }

    public Integer getManaDamage() {
        return manaDamage;
    }

    public void setManaDamage(Integer manaDamage) {
        this.manaDamage = manaDamage;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
}
