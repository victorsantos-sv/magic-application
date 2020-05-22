package br.com.magic.application.entity.mapper;

import br.com.magic.application.api.request.PlayerRequest;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.model.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerDTO toDto(PlayerRequest playerRequest);
    PlayerDTO toDto(Player player);
    Player toEntity(PlayerDTO playerDTO);
    PlayerResponse toResponse(PlayerDTO playerDTO);
}
