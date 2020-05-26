package br.com.magic.application.services.impl;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.mapper.BugCardMapper;
import br.com.magic.application.entity.model.BugCard;
import br.com.magic.application.repositories.BugCardRepositorie;
import br.com.magic.application.services.IBugCardService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BugCardService implements IBugCardService {

    private final BugCardRepositorie bugCardRepositorie;
    private final BugCardMapper bugCardMapper;

    @Autowired
    public BugCardService(BugCardRepositorie bugCardRepositorie, BugCardMapper bugCardMapper) {
        this.bugCardRepositorie = bugCardRepositorie;
        this.bugCardMapper = bugCardMapper;
    }

    @Override
    public List<BugCardDTO> getCardsWithoutBug() {
        List<BugCard> bugCards = bugCardRepositorie.findAllByIsInUseFalse();

        return bugCardMapper.toDto(bugCards);
    }
}
