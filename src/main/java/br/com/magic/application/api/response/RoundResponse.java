package br.com.magic.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundResponse {
    private PlayerResponse player;
    private BugResponse bug;
    private Long cardId;
}
