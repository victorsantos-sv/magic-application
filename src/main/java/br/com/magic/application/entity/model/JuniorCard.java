package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "junior_card")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class JuniorCard extends Card {
    @Column(name = "passive")
    private Integer passive;
}