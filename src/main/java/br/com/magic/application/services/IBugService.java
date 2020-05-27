package br.com.magic.application.services;

import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;

public interface IBugService {

    BugWithCardsDTO getInitialCards();

    BugDTO findById(Long id);

    BugDTO updateBug(BugDTO bugDTO);
}
