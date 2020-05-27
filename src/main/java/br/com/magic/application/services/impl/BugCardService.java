package br.com.magic.application.services.impl;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.mapper.BugCardMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.entity.model.BugCard;
import br.com.magic.application.exception.CardNotFound;
import br.com.magic.application.exception.FullCards;
import br.com.magic.application.repositories.BugCardRepositorie;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.utils.RandomUtils;
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

        return bugCardMapper.toDtoList(bugCards);
    }

    @Override
    public List<BugCardDTO> setCardsOnBug() {
        List<BugCardDTO> bugCards = getCardsWithoutBug();
        List<BugCardDTO> sortedCarts = RandomUtils.sortCards(bugCards);

        List<BugCard> cardsToSave = bugCardMapper.toEntityList(sortedCarts);

        cardsToSave.forEach(bugCard -> {
            bugCard.setInUse(true);
        });

        bugCardRepositorie.saveAll(cardsToSave);

        return bugCardMapper.toDtoList(cardsToSave);
    }

    @Override
    public BugCardDTO selectRandomCard() {
        List<BugCard> bugCards = bugCardRepositorie.findAllByIsInUseTrue();
        Random random = new Random();
        BugCard bugCard = bugCards.get(random.nextInt(bugCards.size()));

        return bugCardMapper.toDto(bugCard);
    }

    @Override
    public void removeCardFromBug(BugCardDTO bugCardDTO) {
        BugCard bugCard = bugCardMapper.toEntity(bugCardDTO);

        bugCard.setInUse(false);

        bugCardRepositorie.save(bugCard);
    }

    @Override
    public BugCardDTO findById(Long id) {
        BugCard bugCard = bugCardRepositorie.findById(id).orElseThrow(() -> new CardNotFound(MagicErrorCode.MEC004));

        return bugCardMapper.toDto(bugCard);
    }

    @Override
    public void saveCardOnBug(BugCardDTO bugCardDTO) {
        List<BugCardDTO> bugCards = getCardsWithoutBug();
        BugCard bugCard = bugCardMapper.toEntity(bugCardDTO);

        if (bugCards.size() <= 5) {
            throw new FullCards(MagicErrorCode.MEC002, Bug.class.getSimpleName());
        }

        bugCard.setInUse(true);

        bugCardRepositorie.save(bugCard);
    }
}
