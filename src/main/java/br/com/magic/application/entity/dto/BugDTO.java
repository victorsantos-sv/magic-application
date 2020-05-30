package br.com.magic.application.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BugDTO {
    private Long id;
    private Integer life = 20;
    private Integer mana = 20;
}
