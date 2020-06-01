package br.com.magic.application.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerWithCardDTO {
    private Long id;
    private String nickName;
    private Integer life;
    private Integer mana;
    private JuniorCardDTO card;
}
