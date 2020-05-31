package br.com.magic.application.services;

import br.com.magic.application.entity.dto.LoginDTO;
import br.com.magic.application.entity.dto.PlayerDTO;

public interface IPlayerService {

    LoginDTO create(PlayerDTO playerDTO);

    PlayerDTO findById(Long id);

    PlayerDTO update(PlayerDTO playerDTO);

    void deleteById(Long playerId);
}
