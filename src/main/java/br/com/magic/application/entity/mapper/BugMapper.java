package br.com.magic.application.entity.mapper;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.model.Bug;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BugMapper {

    @Mappings({
        @Mapping(target = "cards", source = "cardDTOList")
    })
    BugWithCardsDTO toDto(Bug bug, List<BugCardDTO> cardDTOList);

    BugDTO toDto(Bug bug);

    Bug toEntity(BugDTO bugDTO);
}
