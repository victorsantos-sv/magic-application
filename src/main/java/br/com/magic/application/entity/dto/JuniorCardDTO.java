package br.com.magic.application.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuniorCardDTO {
    private Long id;
    private String title;
    private String description;
    private Integer cost;
    private Integer lifeDamage;
    private Integer passive;
}
