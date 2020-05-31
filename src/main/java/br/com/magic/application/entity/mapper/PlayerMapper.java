package br.com.magic.application.entity.mapper;

import br.com.magic.application.api.request.PlayerRequest;
import br.com.magic.application.api.response.LoginResponse;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.LoginDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerDTO toDto(PlayerRequest playerRequest);

    PlayerDTO toDto(Player player);

    Player toEntity(PlayerDTO playerDTO);

    PlayerResponse toResponse(PlayerDTO playerDTO);

    LoginDTO toDto(PlayerDTO playerDTO, BugDTO bugDTO);

    @Mappings({
        @Mapping(target = "player", source = "loginDTO.playerDTO"),
        @Mapping(target = "bug", source = "loginDTO.bugDTO")
    })
    LoginResponse toResponse(LoginDTO loginDTO);
}
