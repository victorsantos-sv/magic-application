package br.com.magic.application.services;

import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;

public interface IBugService {

    BugDTO createBug();

    BugWithCardsDTO getInitialCards(Long bugId);

    BugDTO findById(Long id);

    BugDTO updateBug(BugDTO bugDTO);

    void deleteAllBugs();
}
