package br.com.magic.application.services;

import br.com.magic.application.entity.dto.GameDTO;

public interface IGameService {

    GameDTO loadBoard(Long id);
}
