package br.com.magic.application.services.impl;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.PlayerNotFound;
import br.com.magic.application.repositories.PlayerRepositorie;
import br.com.magic.application.services.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService implements IPlayerService {

    private final PlayerRepositorie playerRepositorie;
    private final PlayerMapper mapper;

    public PlayerService(PlayerRepositorie playerRepositorie, PlayerMapper mapper) {
        this.playerRepositorie = playerRepositorie;
        this.mapper = mapper;
    }

    @Override
    public PlayerDTO create(PlayerDTO playerDTO) {
        Player player = mapper.toEntity(playerDTO);
        player.setNickName(playerDTO.getNickName());

        player = playerRepositorie.save(player);

        return mapper.toDto(player);
    }

    @Override
    public PlayerDTO findById(Long id) {
        Player player = playerRepositorie.findById(id).orElseThrow(() -> new PlayerNotFound(MagicErrorCode.MEC001));

        return mapper.toDto(player);
    }

    @Override
    public PlayerDTO update(PlayerDTO playerDTO) {
        Player player = mapper.toEntity(playerDTO);

        return mapper.toDto(playerRepositorie.save(player));
    }

    @Override
    public void deleteById(Long playerId) {
        playerRepositorie.deleteById(playerId);
    }


}
