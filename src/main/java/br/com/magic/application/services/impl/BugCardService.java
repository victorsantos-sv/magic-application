package br.com.magic.application.services.impl;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.mapper.BugCardMapper;
import br.com.magic.application.entity.mapper.BugMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.entity.model.BugCard;
import br.com.magic.application.exception.CardNotFound;
import br.com.magic.application.exception.FullCards;
import br.com.magic.application.repositories.BugCardRepositorie;
import br.com.magic.application.services.IBugCardService;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

import static br.com.magic.application.utils.RandomUtils.sortCards;

@Service
public class BugCardService implements IBugCardService {

    private final BugCardRepositorie bugCardRepositorie;
    private final BugCardMapper bugCardMapper;
    private final BugMapper bugMapper;

    public BugCardService(BugCardRepositorie bugCardRepositorie, BugCardMapper bugCardMapper, BugMapper bugMapper) {
        this.bugCardRepositorie = bugCardRepositorie;
        this.bugCardMapper = bugCardMapper;
        this.bugMapper = bugMapper;
    }

    @Override
    public List<BugCardDTO> getCardsWithoutBug() {
        List<BugCard> bugCards = bugCardRepositorie.findAllByBugIsNull();

        return bugCardMapper.toDtoList(bugCards);
    }

    @Override
    public List<BugCardDTO> getCards() {
        List<BugCard> bugCards = bugCardRepositorie.findAll();

        return bugCardMapper.toDtoList(bugCards);
    }

    @Override
    public BugWithCardsDTO setCardsOnBug(BugDTO bugDTO) {
        List<BugCardDTO> sortedCards = sortCards(getCards());
        List<BugCard> cards = bugCardMapper.toEntityList(sortedCards);
        Bug bug = bugMapper.toEntity(bugDTO);

        List<BugCard> cardsWithBug = bugCardRepositorie.findAllByBugId(bug.getId());
        List<BugCardDTO> cardsWithBugDTO = bugCardMapper.toDtoList(cardsWithBug);

        if (cardsWithBug.size() <= 4 && cardsWithBug.size() > 0) {
            return new BugWithCardsDTO(bugDTO.getId(), bugDTO.getLife(), bugDTO.getMana(), cardsWithBugDTO);
        }

        cards.forEach(bugCard -> {
            bugCard.setBug(bug);
        });

        bugCardRepositorie.saveAll(cards);

        List<BugCardDTO> cardDTOS = bugCardMapper.toDtoList(cards);

        return new BugWithCardsDTO(bugDTO.getId(), bugDTO.getLife(), bugDTO.getMana(), cardDTOS);
    }

    @Override
    public BugCardDTO selectRandomCard(Long bugId) {
        List<BugCard> bugCards = bugCardRepositorie.findAllByBugId(bugId);
        Random random = new Random();
        BugCard bugCard = bugCards.get(random.nextInt(bugCards.size()));

        return bugCardMapper.toDto(bugCard);
    }

    @Override
    public BugCardDTO selectRandomCardWithoutBug() {
        List<BugCard> bugCards = bugCardRepositorie.findAllByBugIsNull();
        Random random = new Random();
        BugCard bugCard = bugCards.get(random.nextInt(bugCards.size()));

        return bugCardMapper.toDto(bugCard);
    }

    @Override
    public void removeCardFromBug(BugCardDTO bugCardDTO) {
        BugCard bugCard = bugCardMapper.toEntity(bugCardDTO);

        bugCard.setBug(null);

        bugCardRepositorie.save(bugCard);
    }

    @Override
    public BugCardDTO findById(Long id) {
        BugCard bugCard = bugCardRepositorie.findById(id).orElseThrow(() -> new CardNotFound(MagicErrorCode.MEC004));

        return bugCardMapper.toDto(bugCard);
    }

    @Override
    public void saveCardOnBug(BugCardDTO bugCardDTO, BugDTO bugDTO) {
        List<BugCardDTO> bugCards = getCardsWithoutBug();
        BugCard bugCard = bugCardMapper.toEntity(bugCardDTO);
        Bug bug = bugMapper.toEntity(bugDTO);

        if (bugCards.size() > 5) {
            bugCard.setBug(bug);

            bugCardRepositorie.save(bugCard);
        } else {
            throw new FullCards(MagicErrorCode.MEC002, Bug.class.getSimpleName());
        }
    }

    @Override
    public void removeAllCards(Long bugId) {
        List<BugCard> bugCards = bugCardRepositorie.findAllByBugId(bugId);

        bugCards.forEach(bugCard -> bugCard.setBug(null));

        bugCardRepositorie.saveAll(bugCards);
    }
}
