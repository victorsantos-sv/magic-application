package br.com.magic.application.services;

import br.com.magic.application.entity.dto.PlayerDTO;

public interface IPlayerService {

    PlayerDTO create(PlayerDTO playerDTO);

    PlayerDTO findById(Long id);

    PlayerDTO update(PlayerDTO playerDTO);


}
