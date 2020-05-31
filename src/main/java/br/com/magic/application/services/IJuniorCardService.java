package br.com.magic.application.services;

import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerWithCardsDTO;
import java.util.List;

public interface IJuniorCardService {

    List<JuniorCardDTO> getCards();

    List<JuniorCardDTO> getCardsWithoutPlayer();

    JuniorCardDTO findById(Long id);

    JuniorCardDTO findByPlayerId(Long cardId, Long playerId);

    JuniorCardDTO getRandomCard();

    PlayerWithCardsDTO saveCardsIntoPlayer(Long playerId);

    void saveCardIntoPlayer(JuniorCardDTO juniorCardDTO, Long playerId);

    void removeCardFromJunior(JuniorCardDTO juniorCardDTO);

    void removeAllCards();
}
