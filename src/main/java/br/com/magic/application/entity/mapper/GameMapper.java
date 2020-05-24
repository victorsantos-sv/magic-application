package br.com.magic.application.entity.mapper;

import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameResponse toResponse(GameDTO gameDTO);

    @Mappings({
        @Mapping(target = "id", source = "playerDTO.id"),
        @Mapping(target = "nickName", source = "playerDTO.nickName"),
        @Mapping(target = "life", source = "playerDTO.life"),
        @Mapping(target = "mana", source = "playerDTO.mana"),
        @Mapping(target = "cards", source = "cards")
    })
    GameDTO toDto(PlayerDTO playerDTO, List<JuniorCardDTO> cards);
}
