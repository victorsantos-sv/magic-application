package br.com.magic.application.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BugWithCardsDTO {
    private Long id;
    private Integer life;
    private Integer mana;
    private List<BugCardDTO> cards;
}
