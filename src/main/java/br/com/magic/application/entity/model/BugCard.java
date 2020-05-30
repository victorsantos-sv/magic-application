package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bug_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BugCard extends Card {
    @Column(name = "mana_damage")
    private Integer manaDamage;

    @Column(name = "is_in_use")
    private Boolean isInUse;
}
