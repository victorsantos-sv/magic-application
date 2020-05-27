package br.com.magic.application.services.impl;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.mapper.BugCardMapper;
import br.com.magic.application.entity.mapper.BugMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.entity.model.BugCard;
import br.com.magic.application.repositories.BugCardRepositorie;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    @Override
    public List<BugCardDTO> setCartsOnBug() {
        List<BugCardDTO> bugCards = getCardsWithoutBug();
        List<BugCardDTO> sortedCarts = RandomUtils.sortCards(bugCards);

        List<BugCard> cardsToSave = bugCardMapper.toEntity(sortedCarts);

        cardsToSave.forEach(bugCard -> {
            bugCard.setInUse(true);
        });

        bugCardRepositorie.saveAll(cardsToSave);

        return bugCardMapper.toDto(cardsToSave);
    }
}
