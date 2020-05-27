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
import br.com.magic.application.services.IBugService;
import br.com.magic.application.services.IGameService;
import br.com.magic.application.services.IJuniorCardService;
import br.com.magic.application.services.IPlayerService;
import br.com.magic.application.utils.RandomUtils;
import java.util.List;
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
        List<JuniorCardDTO> sortedCards = RandomUtils.sortCards(cards);
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
}
