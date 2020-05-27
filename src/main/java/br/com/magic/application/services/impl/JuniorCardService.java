package br.com.magic.application.services.impl;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.JuniorCardMapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.entity.model.BugCard;
import br.com.magic.application.entity.model.JuniorCard;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.CardNotFound;
import br.com.magic.application.exception.PlayerFullCards;
import br.com.magic.application.exception.PlayerNotFound;
import br.com.magic.application.repositories.JuniorCardRepositorie;
import br.com.magic.application.services.IJuniorCardService;
import br.com.magic.application.services.IPlayerService;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JuniorCardService implements IJuniorCardService {

    private JuniorCardRepositorie juniorCardRepositorie;
    private IPlayerService playerService;
    private JuniorCardMapper mapperJuniorCard;
    private PlayerMapper playerMapper;

    @Autowired
    public JuniorCardService(JuniorCardRepositorie juniorCardRepositorie, IPlayerService playerService, JuniorCardMapper juniorCardMapper,
                             PlayerMapper playerMapper) {
        this.juniorCardRepositorie = juniorCardRepositorie;
        this.playerService = playerService;
        this.mapperJuniorCard = juniorCardMapper;
        this.playerMapper = playerMapper;
    }

    @Override
    public List<JuniorCardDTO> getCards() {
        List<JuniorCard> juniorCards = juniorCardRepositorie.findAll();

        return mapperJuniorCard.toDto(juniorCards);
    }

    @Override
    public JuniorCardDTO findById(Long id) {
        JuniorCard juniorCard = juniorCardRepositorie.findById(id).orElseThrow(() -> new CardNotFound(MagicErrorCode.MEC001));

        return mapperJuniorCard.toDto(juniorCard);
    }

    @Override
    public void saveCardsIntoPlayer(List<JuniorCardDTO> cardsDto, Long id) {
        List<JuniorCard> cards = mapperJuniorCard.toEntity(cardsDto);
        PlayerDTO playerDTO = playerService.findById(id);
        Player player = playerMapper.toEntity(playerDTO);

        List<JuniorCard> cardsWithUser = juniorCardRepositorie.findAllByPlayer(player);

        if (cardsWithUser.size() == 4) {
            throw new PlayerFullCards(MagicErrorCode.MEC002);
        }

        cards.forEach(juniorCard -> {
            juniorCard.setPlayer(player);
        });

        juniorCardRepositorie.saveAll(cards);
    }

    @Override
    public JuniorCardDTO selectRandomCard() {
        List<JuniorCard> juniorCards = juniorCardRepositorie.findAllByIsInUseTrue();
        Random random = new Random();
        JuniorCard juniorCard = juniorCards.get(random.nextInt(juniorCards.size()));

        return mapperJuniorCard.toDto(juniorCard);
    }

    @Override
    public void removeCardJunior(JuniorCardDTO juniorCardDTO) {
        JuniorCard juniorCard = mapperJuniorCard.toEntity(juniorCardDTO);
        juniorCard.setInUse(false);

        juniorCardRepositorie.save(juniorCard);
    }



}
