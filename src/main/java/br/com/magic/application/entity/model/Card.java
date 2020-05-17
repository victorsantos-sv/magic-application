package br.com.magic.application.entity.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor
@Data
public abstract class Card {
    @Id
    @Column(name = "card_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "title", nullable = false)
    protected String title;

    @Column(name = "description", nullable = false)
    protected String description;

    @Column(name = "cost")
    protected Integer cost;

    @Column(name = "life_damage")
    protected Integer lifeDamage;
}
