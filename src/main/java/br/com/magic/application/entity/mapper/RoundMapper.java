package br.com.magic.application.entity.mapper;

import br.com.magic.application.api.response.BugResponse;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.api.response.RoundResponse;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.RoundDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RoundMapper {

    RoundDTO toDto(PlayerDTO playerDTO, BugDTO bugDTO);

    @Mappings({
        @Mapping(source = "roundDTO.playerDTO", target = "player", qualifiedByName = "toPlayerResponse"),
        @Mapping(source = "roundDTO.bugDTO", target = "bug", qualifiedByName = "toBugResponse")
    })
    RoundResponse toResponse(RoundDTO roundDTO);

    @Named("toPlayerResponse")
    public static PlayerResponse personalizedResponse(PlayerDTO playerDTO) {
        return new PlayerResponse(playerDTO.getId(), playerDTO.getNickName(), playerDTO.getLife(), playerDTO.getMana());
    }

    @Named("toBugResponse")
    public static BugResponse personalizedResponse(BugDTO bugDTO) {
        return new BugResponse(bugDTO.getId(), bugDTO.getLife(), bugDTO.getMana());
    }
}
