package br.com.magic.application.services.impl;

import br.com.magic.application.entity.dto.*;
import br.com.magic.application.entity.mapper.GameMapper;
import br.com.magic.application.entity.mapper.JuniorCardMapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.services.IGameService;
import br.com.magic.application.services.IJuniorCardService;
import br.com.magic.application.services.IPlayerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IGameService {

    private IPlayerService playerService;
    private IJuniorCardService juniorCardService;
    private GameMapper mapper;
    private PlayerMapper playerMapper;

    @Autowired
    public GameService(IPlayerService playerService, IJuniorCardService juniorCardService, GameMapper mapper, PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.juniorCardService = juniorCardService;
        this.mapper = mapper;
        this.playerMapper = playerMapper;
    }

    @Override
    public GameDTO loadBoard(Long id) {
        PlayerDTO playerDTO = playerService.findById(id);
        List<JuniorCardDTO> cards = juniorCardService.getCards();
        List<JuniorCardDTO> sortedCards = sortCards(cards);
        juniorCardService.saveCardsIntoPlayer(sortedCards, id);

        return mapper.toDto(playerDTO, sortedCards);
    }

    @Override
    public PlayerDTO scoreboardPlayer(Long id) {
        JuniorCardDTO juniorCard= juniorCardService.selectRandomCard();
        PlayerDTO playerDTO = playerService.findById(id);

        Integer manaAmount = juniorCard.getPassive() == null ?
                playerDTO.getMana() - juniorCard.getCost(): playerDTO.getMana() +juniorCard.getPassive();

        playerDTO.setMana(manaAmount);
        juniorCardService.removeCardJunior(juniorCard);
        PlayerDTO playerDTOUpdated = playerService.update(playerDTO);

        return playerMapper.toDto(playerDTOUpdated);
    }

    private List<JuniorCardDTO> sortCards(List<JuniorCardDTO> cards) {
        Random random = new Random();
        List<JuniorCardDTO> randomCards = new ArrayList<>();

        for (int i = 0; i < 4;) {
            int randomNumber = random.nextInt(9);

            if (!randomCards.contains(cards.get(randomNumber))) {
                randomCards.add(cards.get(randomNumber));
                i++;
            }
        }

        return randomCards;
    }





}
