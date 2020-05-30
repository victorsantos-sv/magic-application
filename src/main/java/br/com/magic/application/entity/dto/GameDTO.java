package br.com.magic.application.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private PlayerWithCardsDTO playerWithCardsDTO;
    private BugWithCardsDTO bugWithCardsDTO;
}
