package br.com.magic.application.services;

import br.com.magic.application.entity.dto.EndTurnDTO;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.RoundDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;

public interface IGameService {

    GameDTO loadBoard(Long id);

    StackCardsDTO getStackCards();

    RoundDTO scoreboardBug(Long bugId, Long playerId);

    RoundDTO scoreboardPlayer(Long playerId, Long cardId);

    EndTurnDTO endTurn(Long playerId, Long bugId);

}
