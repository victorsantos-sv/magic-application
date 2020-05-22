package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "junior_card")
public class JuniorCard extends Card {
    @Column(name = "passive")
    private Integer passive;

    @ManyToOne(targetEntity = Player.class)
    @JoinColumn(name = "player_id", nullable = true)
    private Player player;

    public JuniorCard() {
    }

    public JuniorCard(Long id, String title, String description, Integer cost, Integer lifeDamage, Integer passive, Player player) {
        super(id, title, description, cost, lifeDamage);
        this.passive = passive;
        this.player = player;
    }

    public Integer getPassive() {
        return passive;
    }

    public void setPassive(Integer passive) {
        this.passive = passive;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
