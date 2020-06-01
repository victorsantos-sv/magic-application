package br.com.magic.application.services;

import br.com.magic.application.entity.dto.EndTurnDTO;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.PlayerWithCardDTO;
import br.com.magic.application.entity.dto.RoundDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;

public interface IGameService {

    GameDTO loadBoard(Long bugId, Long playerId);

    StackCardsDTO getStackCards();

    RoundDTO scoreboardBug(Long bugId, Long playerId);

    RoundDTO scoreboardPlayer(Long playerId, Long cardId, Long bugId);

    EndTurnDTO endTurn(Long playerId, Long bugId);

    void logoff(Long bugId, Long playerId);

    PlayerWithCardDTO buyCard(Long playerId, Long cardId);
}
