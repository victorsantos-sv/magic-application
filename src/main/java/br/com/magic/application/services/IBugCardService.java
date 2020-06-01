package br.com.magic.application.services;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import java.util.List;

public interface IBugCardService {

    List<BugCardDTO> getCardsWithoutBug();

    List<BugCardDTO> getCards();

    BugWithCardsDTO setCardsOnBug(BugDTO bugDTO);

    BugCardDTO selectRandomCard(Long bugId);

    BugCardDTO selectRandomCardWithoutBug();

    void removeCardFromBug(BugCardDTO bugCardDTO);

    BugCardDTO findById(Long id);

    void saveCardOnBug(BugCardDTO bugCardDTO, BugDTO bugDTO);

    void removeAllCards(Long bugId);
}
