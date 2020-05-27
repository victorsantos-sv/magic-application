package br.com.magic.application.entity.mapper;

import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.StackCardsResponse;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.PlayerWithCardsDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mappings({
        @Mapping(target = "player", source = "gameDTO.playerWithCardsDTO"),
        @Mapping(target = "bug", source = "gameDTO.bugWithCardsDTO")
    })
    GameResponse toResponse(GameDTO gameDTO);

    GameDTO toDto(PlayerWithCardsDTO playerWithCardsDTO, BugWithCardsDTO bugWithCardsDTO);

    @Mappings({
        @Mapping(target = "juniorCards", source = "stackCardsDTO.juniorCards"),
        @Mapping(target = "bugCards", source = "stackCardsDTO.bugCards")
    })
    StackCardsResponse toResponse(StackCardsDTO stackCardsDTO);
}
