package br.com.magic.application.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndTurnDTO {
    private PlayerDTO playerDTO;
    private BugDTO bugDTO;
    private BugCardDTO bugCardDTO;
}
