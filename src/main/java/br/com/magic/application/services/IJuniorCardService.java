package br.com.magic.application.services;

import br.com.magic.application.entity.dto.JuniorCardDTO;
import java.util.List;

public interface IJuniorCardService {

    List<JuniorCardDTO> getCards();

    void saveCardsIntoPlayer(List<JuniorCardDTO> cardsDto, Long id) throws Exception;
}
