package br.com.magic.application.services.impl;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.dto.EndTurnDTO;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.PlayerWithCardDTO;
import br.com.magic.application.entity.dto.PlayerWithCardsDTO;
import br.com.magic.application.entity.dto.RoundDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;
import br.com.magic.application.entity.mapper.GameMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.InsufficientMana;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.services.IBugService;
import br.com.magic.application.services.IGameService;
import br.com.magic.application.services.IJuniorCardService;
import br.com.magic.application.services.IPlayerService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService implements IGameService {

    private final IPlayerService playerService;
    private final IJuniorCardService juniorCardService;
    private final IBugCardService bugCardService;
    private final IBugService bugService;
    private final GameMapper mapper;

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
    public GameDTO loadBoard(Long bugId, Long playerId) {
        PlayerWithCardsDTO playerWithCardsDTO = juniorCardService.saveCardsIntoPlayer(playerId);
        BugWithCardsDTO bugWithCardsDTO = bugService.getInitialCards(bugId);

        return mapper.toDto(playerWithCardsDTO, bugWithCardsDTO);
    }

    @Override
    public StackCardsDTO getStackCards() {
        List<JuniorCardDTO> juniorCards = juniorCardService.getCardsWithoutPlayer();
        List<BugCardDTO> bugCards = bugCardService.getCardsWithoutBug();

        return new StackCardsDTO(juniorCards, bugCards);
    }

    @Override
    @Transactional
    public RoundDTO scoreboardBug(Long bugId, Long playerId) {
        BugDTO bugDTO = bugService.findById(bugId);
        PlayerDTO playerDTO = playerService.findById(playerId);
        BugCardDTO bugCardDTO = bugCardService.selectRandomCard(bugId);

        int manaCost = bugDTO.getMana() - bugCardDTO.getCost();

        if (manaCost < 0) {
            throw new InsufficientMana(MagicErrorCode.MEC006, Bug.class.getSimpleName());
        }

        bugDTO.setMana(manaCost);

        Integer lifeLost = Math.max(playerDTO.getLife() - bugCardDTO.getLifeDamage(), 0);
        Integer manaAmount = bugCardDTO.getManaDamage() != null ? playerDTO.getMana() - bugCardDTO.getManaDamage() : playerDTO.getMana();

        playerDTO.setMana(manaAmount);
        playerDTO.setLife(lifeLost);

        bugCardService.removeCardFromBug(bugCardDTO);
        BugDTO bugDTOUpdated = bugService.updateBug(bugDTO);
        PlayerDTO playerDTOUpdated = playerService.update(playerDTO);

        return mapper.toDto(playerDTOUpdated, bugDTOUpdated, bugCardDTO.getId());
    }

    @Override
    @Transactional
    public RoundDTO scoreboardPlayer(Long playerId, Long cardId, Long bugId) {
        PlayerDTO playerDTO = playerService.findById(playerId);
        BugDTO bugDTO = bugService.findById(bugId);
        JuniorCardDTO juniorCardDTO = juniorCardService.findByPlayerId(cardId, playerId);

        int manaAmount = juniorCardDTO.getPassive() == null ?
            playerDTO.getMana() - juniorCardDTO.getCost() : playerDTO.getMana() + juniorCardDTO.getPassive();

        if (manaAmount < 0) {
            throw new InsufficientMana(MagicErrorCode.MEC006, Player.class.getSimpleName());
        }

        manaAmount = Math.min(manaAmount, 20);

        Integer lifeAmount = juniorCardDTO.getLifeDamage() != null ? bugDTO.getLife() - juniorCardDTO.getLifeDamage() : bugDTO.getLife();

        lifeAmount = Math.max(lifeAmount, 0);

        bugDTO.setLife(lifeAmount);
        BugDTO bugDTOUpdated = bugService.updateBug(bugDTO);

        playerDTO.setMana(manaAmount);
        juniorCardService.removeCardFromJunior(juniorCardDTO);
        PlayerDTO playerDTOUpdated = playerService.update(playerDTO);

        return mapper.toDto(playerDTOUpdated, bugDTOUpdated, cardId);
    }

    @Override
    @Transactional
    public EndTurnDTO endTurn(Long playerId, Long bugId) {
        PlayerDTO playerDTO = playerService.findById(playerId);
        BugDTO bugDTO = bugService.findById(bugId);
        BugCardDTO bugCardDTO = bugCardService.selectRandomCardWithoutBug();

        Integer playerManaAmount = playerDTO.getMana() + 2 > 20 ? playerDTO.getMana() : playerDTO.getMana() + 2;
        Integer bugManaAmount = bugDTO.getMana() + 2 > 20 ? bugDTO.getMana() : bugDTO.getMana() + 2;

        playerDTO.setMana(playerManaAmount);
        bugDTO.setMana(bugManaAmount);

        bugCardService.saveCardOnBug(bugCardDTO, bugDTO);

        return new EndTurnDTO(playerDTO, bugDTO, bugCardDTO);
    }

    @Override
    public void logoff(Long bugId, Long playerId) {
        juniorCardService.removeAllCards();
        playerService.deleteById(playerId);
        bugCardService.removeAllCards(bugId);
        bugService.deleteAllBugs();

    }

    @Override
    public PlayerWithCardDTO buyCard(Long playerId, Long cardId) {
        PlayerDTO playerDTO = playerService.findById(playerId);
        JuniorCardDTO juniorCardDTO = juniorCardService.findById(cardId);

        juniorCardService.saveCardIntoPlayer(juniorCardDTO, playerId);

        return new PlayerWithCardDTO(playerId, playerDTO.getNickName(), playerDTO.getLife(), playerDTO.getMana(), juniorCardDTO);
    }
}
