package br.com.magic.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {
    private PlayerWithCardResponse player;
    private BugWithCardResponse bug;
}
