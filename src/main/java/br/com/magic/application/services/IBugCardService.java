package br.com.magic.application.services;

import br.com.magic.application.entity.dto.BugCardDTO;
import java.util.List;

public interface IBugCardService {

    List<BugCardDTO> getCardsWithoutBug();

    List<BugCardDTO> setCardsOnBug();

    BugCardDTO selectRandomCard();

    void removeCardFromBug(BugCardDTO bugCardDTO);

    BugCardDTO findById(Long id);

    void saveCardOnBug(BugCardDTO bugCardDTO);
}
