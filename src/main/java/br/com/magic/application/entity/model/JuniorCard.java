package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "junior_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuniorCard extends Card {
    @Column(name = "passive")
    private Integer passive;

    @ManyToOne(targetEntity = Player.class)
    @JoinColumn(name = "player_id", nullable = true)
    private Player player;
}
