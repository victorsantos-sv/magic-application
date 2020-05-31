package br.com.magic.application.services.impl;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.PlayerWithCardsDTO;
import br.com.magic.application.entity.mapper.JuniorCardMapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.entity.model.JuniorCard;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.CardNotFound;
import br.com.magic.application.exception.FullCards;
import br.com.magic.application.repositories.JuniorCardRepositorie;
import br.com.magic.application.services.IJuniorCardService;
import br.com.magic.application.services.IPlayerService;
import br.com.magic.application.utils.RandomUtils;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class JuniorCardService implements IJuniorCardService {

    private final JuniorCardRepositorie juniorCardRepositorie;
    private final IPlayerService playerService;
    private final JuniorCardMapper juniorCardMapper;
    private final PlayerMapper playerMapper;

    public JuniorCardService(JuniorCardRepositorie juniorCardRepositorie, IPlayerService playerService, JuniorCardMapper juniorCardMapper,
                             PlayerMapper playerMapper) {
        this.juniorCardRepositorie = juniorCardRepositorie;
        this.playerService = playerService;
        this.juniorCardMapper = juniorCardMapper;
        this.playerMapper = playerMapper;
    }

    @Override
    public List<JuniorCardDTO> getCards() {
        List<JuniorCard> juniorCards = juniorCardRepositorie.findAll();

        return juniorCardMapper.toDto(juniorCards);
    }

    @Override
    public List<JuniorCardDTO> getCardsWithoutPlayer() {
        List<JuniorCard> juniorCards = juniorCardRepositorie.findAllByPlayerIsNullOrderById();

        return juniorCardMapper.toDto(juniorCards);
    }

    @Override
    public JuniorCardDTO findById(Long id) {
        JuniorCard juniorCard = juniorCardRepositorie.findById(id).orElseThrow(() -> new CardNotFound(MagicErrorCode.MEC004));

        return juniorCardMapper.toDto(juniorCard);
    }

    @Override
    public JuniorCardDTO findByPlayerId(Long cardId, Long playerId) {
        JuniorCard juniorCard = juniorCardRepositorie.findByIdAndPlayerId(cardId, playerId)
            .orElseThrow(() -> new CardNotFound(MagicErrorCode.MEC004));

        return juniorCardMapper.toDto(juniorCard);
    }

    @Override
    public JuniorCardDTO getRandomCard() {
        List<JuniorCardDTO> juniorCardDTOList = getCardsWithoutPlayer();
        Random random = new Random();

        return juniorCardDTOList.get(random.nextInt(juniorCardDTOList.size()));
    }

    @Override
    public PlayerWithCardsDTO saveCardsIntoPlayer(Long playerId) {
        List<JuniorCardDTO> sortedCards = RandomUtils.sortCards(getCards());
        List<JuniorCard> cards = juniorCardMapper.toEntity(sortedCards);
        PlayerDTO playerDTO = playerService.findById(playerId);
        Player player = playerMapper.toEntity(playerDTO);

        List<JuniorCard> cardsWithUser = juniorCardRepositorie.findAllByPlayerId(player.getId());
        List<JuniorCardDTO> cardsWithUserDTO = juniorCardMapper.toDto(cardsWithUser);

        if (cardsWithUser.size() <= 4) {
            return new PlayerWithCardsDTO(playerDTO.getId(), playerDTO.getNickName(), playerDTO.getLife(), playerDTO.getMana(), cardsWithUserDTO);
        }

        cards.forEach(juniorCard -> {
            juniorCard.setPlayer(player);
        });

        juniorCardRepositorie.saveAll(cards);

        List<JuniorCardDTO> cardDTOS = juniorCardMapper.toDto(cards);

        return new PlayerWithCardsDTO(playerDTO.getId(), playerDTO.getNickName(), playerDTO.getLife(), playerDTO.getMana(), cardDTOS);
    }

    @Override
    public void saveCardIntoPlayer(JuniorCardDTO juniorCardDTO, Long playerId) {
        List<JuniorCard> cardsWithUser = juniorCardRepositorie.findAllByPlayerId(playerId);
        PlayerDTO playerDTO = playerService.findById(playerId);

        if (cardsWithUser.size() == 4) {
            throw new FullCards(MagicErrorCode.MEC002, Player.class.getSimpleName());
        }

        JuniorCard juniorCard = juniorCardMapper.toEntity(juniorCardDTO);

        juniorCard.setPlayer(playerMapper.toEntity(playerDTO));

        juniorCardRepositorie.save(juniorCard);
    }

    @Override
    public void removeCardFromJunior(JuniorCardDTO juniorCardDTO) {
        JuniorCard juniorCard = juniorCardMapper.toEntity(juniorCardDTO);
        juniorCard.setPlayer(null);

        juniorCardRepositorie.save(juniorCard);
    }

    @Override
    public void removeAllCards() {
        List<JuniorCard> juniorCards = juniorCardRepositorie.findAll();

        juniorCards.forEach(juniorCard -> {
            juniorCard.setPlayer(null);
        });

        juniorCardRepositorie.saveAll(juniorCards);
    }
}
