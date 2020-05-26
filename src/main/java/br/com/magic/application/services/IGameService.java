package br.com.magic.application.services;

import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;

public interface IGameService {

    GameDTO loadBoard(Long id);

    StackCardsDTO getStackCards();
}
