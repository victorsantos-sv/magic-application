package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bug_card")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class BugCard extends Card {
    @Column(name = "mana_damage")
    private Integer manaDamage;
}
