package br.com.magic.application.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerWithCardsDTO {
    private Long id;
    private String nickName;
    private Integer life;
    private Integer mana;
    private List<JuniorCardDTO> cards;
}
