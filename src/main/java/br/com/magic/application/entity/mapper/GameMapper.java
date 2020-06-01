package br.com.magic.application.entity.mapper;

import br.com.magic.application.api.response.BugResponse;
import br.com.magic.application.api.response.EndTurnResponse;
import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.api.response.RoundResponse;
import br.com.magic.application.api.response.StackCardsResponse;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.dto.EndTurnDTO;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.PlayerWithCardsDTO;
import br.com.magic.application.entity.dto.RoundDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

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

    RoundDTO toDto(PlayerDTO playerDTO, BugDTO bugDTO, Long cardId);

    @Mappings({
        @Mapping(source = "roundDTO.playerDTO", target = "player", qualifiedByName = "toPlayerResponse"),
        @Mapping(source = "roundDTO.bugDTO", target = "bug", qualifiedByName = "toBugResponse"),
        @Mapping(source = "roundDTO.cardId", target = "cardId")
    })
    RoundResponse toResponse(RoundDTO roundDTO);

    @Mappings({
        @Mapping(target = "player", source = "endTurnDTO.playerDTO"),
        @Mapping(target = "bug", source = "endTurnDTO.bugDTO"),
        @Mapping(target = "bug.card", source = "endTurnDTO.bugCardDTO"),
    })
    EndTurnResponse toResponse(EndTurnDTO endTurnDTO);

    @Named("toPlayerResponse")
    static PlayerResponse personalizedResponse(PlayerDTO playerDTO) {
        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.setId(playerDTO.getId());
        playerResponse.setNickName(playerDTO.getNickName());
        playerResponse.setLife(playerDTO.getLife());
        playerResponse.setMana(playerDTO.getMana());

        return playerResponse;
    }

    @Named("toBugResponse")
    static BugResponse personalizedResponse(BugDTO bugDTO) {
        BugResponse bugResponse = new BugResponse();
        bugResponse.setId(bugDTO.getId());
        bugResponse.setLife(bugDTO.getLife());
        bugResponse.setMana(bugDTO.getMana());

        return bugResponse;
    }
}
