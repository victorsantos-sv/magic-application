package br.com.magic.application.entity.mapper;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.model.BugCard;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BugCardMapper {

    List<BugCardDTO> toDto(List<BugCard> bugCards);
}
