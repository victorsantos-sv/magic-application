package br.com.magic.application.services;

import br.com.magic.application.entity.dto.JuniorCardDTO;
import java.util.List;

public interface IJuniorCardService {

    List<JuniorCardDTO> getCards();

    List<JuniorCardDTO> getCardsWithoutPlayer();

    JuniorCardDTO findById(Long id);

    JuniorCardDTO findByPlayerId(Long playerId);

    void saveCardsIntoPlayer(List<JuniorCardDTO> cardsDto, Long id);

    void removeCardFromJunior(JuniorCardDTO juniorCardDTO);
}
