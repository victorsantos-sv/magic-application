package br.com.magic.application.entity.mapper;

import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.model.JuniorCard;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JuniorCardMapper {
    List<JuniorCardDTO> toDto(List<JuniorCard> cards);

    List<JuniorCard> toEntity(List<JuniorCardDTO> cards);

    JuniorCardDTO toDto(JuniorCard card);

    JuniorCard toEntity(JuniorCardDTO juniorCardDTO);
}
