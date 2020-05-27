package br.com.magic.application.services;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.model.JuniorCard;

import java.util.List;

public interface IJuniorCardService {

    List<JuniorCardDTO> getCards();

    JuniorCardDTO findById(Long id);

    void saveCardsIntoPlayer(List<JuniorCardDTO> cardsDto, Long id);

    void removeCardJunior(JuniorCardDTO juniorCardDTO);

    JuniorCardDTO selectRandomCard();


}
