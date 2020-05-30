package br.com.magic.application.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackCardsDTO {
    private List<JuniorCardDTO> juniorCards;
    private List<BugCardDTO> bugCards;
}
