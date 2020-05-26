package br.com.magic.application.services.impl;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.PlayerWithCardsDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;
import br.com.magic.application.entity.mapper.GameMapper;
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
    private IBugCardService bugCardService;
    private IBugService bugService;
    private GameMapper mapper;

    @Autowired
    public GameService(
        IPlayerService playerService,
        IJuniorCardService juniorCardService,
        IBugCardService bugCardService,
        IBugService bugService,
        GameMapper mapper
    ) {
        this.playerService = playerService;
        this.juniorCardService = juniorCardService;
        this.bugCardService = bugCardService;
        this.bugService = bugService;
        this.mapper = mapper;
    }

    @Override
    public GameDTO loadBoard(Long id) {
        PlayerDTO playerDTO = playerService.findById(id);
        List<JuniorCardDTO> cards = juniorCardService.getCards();
        List<JuniorCardDTO> sortedCards = sortCards(cards);
        juniorCardService.saveCardsIntoPlayer(sortedCards, id);
        BugDTO bugDTO = bugService.getInitialCards();
        BugWithCardsDTO bugWithCardsDTO = new BugWithCardsDTO(bugDTO.getId(), bugDTO.getLife(), bugDTO.getMana(), bugDTO.getCards());
        PlayerWithCardsDTO playerWithCardsDTO = new PlayerWithCardsDTO(
            playerDTO.getId(), playerDTO.getNickName(), playerDTO.getLife(), playerDTO.getMana(), sortedCards);

        return mapper.toDto(playerWithCardsDTO, bugWithCardsDTO);
    }

    @Override
    public StackCardsDTO getStackCards() {
        List<JuniorCardDTO> juniorCards = juniorCardService.getCardsWithoutPlayer();
        List<BugCardDTO> bugCards = bugCardService.getCardsWithoutBug();

        return new StackCardsDTO(juniorCards, bugCards);
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
