package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.dto.EndTurnDTO;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.PlayerWithCardsDTO;
import br.com.magic.application.entity.dto.RoundDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;
import br.com.magic.application.entity.mapper.GameMapper;
import br.com.magic.application.exception.InsufficientMana;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.services.IBugService;
import br.com.magic.application.services.IJuniorCardService;
import br.com.magic.application.services.IPlayerService;
import br.com.magic.application.services.impl.GameService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GameServiceTest {

    private final IPlayerService playerService = Mockito.mock(IPlayerService.class);
    private final IJuniorCardService juniorCardService = Mockito.mock(IJuniorCardService.class);
    private final IBugCardService bugCardService = Mockito.mock(IBugCardService.class);
    private final IBugService bugService = Mockito.mock(IBugService.class);
    private final GameMapper mapper = Mockito.mock(GameMapper.class);
    private final GameService gameService = new GameService(playerService, juniorCardService, bugCardService, bugService, mapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldLoadBoardGame() throws IOException {
        Long id = 1L;
        PlayerDTO playerDTO = buildPlayerDTO(id);
        List<JuniorCardDTO> juniorCards = buildJuniorCardsList();
        PlayerWithCardsDTO playerWithCardsDTO = new PlayerWithCardsDTO(
            playerDTO.getId(),
            playerDTO.getNickName(),
            playerDTO.getLife(),
            playerDTO.getMana(),
            juniorCards
        );
        BugWithCardsDTO bugWithCardsDTO = new BugWithCardsDTO(1L, 20, 20, Collections.emptyList());
        GameDTO gameDTO = buildGameDTO(playerWithCardsDTO, bugWithCardsDTO);
        ArgumentCaptor<List<JuniorCardDTO>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        Mockito.when(playerService.findById(id)).thenReturn(playerDTO);
        Mockito.when(juniorCardService.getCards()).thenReturn(juniorCards);
        Mockito.doNothing().when(juniorCardService).saveCardsIntoPlayer(Mockito.anyList(), Mockito.eq(id));
        Mockito.when(bugService.getInitialCards()).thenReturn(bugWithCardsDTO);
        Mockito.when(mapper.toDto(Mockito.any(), Mockito.any())).thenReturn(gameDTO);

        GameDTO gameDTOCreated = gameService.loadBoard(id);

        Mockito.verify(playerService, Mockito.times(1)).findById(id);
        Mockito.verify(juniorCardService, Mockito.times(1)).getCards();
        Mockito.verify(juniorCardService, Mockito.times(1)).saveCardsIntoPlayer(argumentCaptor.capture(), Mockito.eq(id));
        Mockito.verify(playerService, Mockito.times(1)).findById(id);

        Assert.assertSame(argumentCaptor.getValue().size(), 4);
        Assert.assertSame(gameDTOCreated.getBugWithCardsDTO().getId(), gameDTO.getBugWithCardsDTO().getId());
        Assert.assertSame(gameDTOCreated.getPlayerWithCardsDTO().getId(), gameDTO.getPlayerWithCardsDTO().getId());
    }

    @Test
    public void shouldLoadAllStackCards() throws IOException {
        List<JuniorCardDTO> juniorCards = buildStackJuniorCards();
        List<BugCardDTO> bugCards = buildStackBugCards();

        Mockito.when(juniorCardService.getCardsWithoutPlayer()).thenReturn(juniorCards);
        Mockito.when(bugCardService.getCardsWithoutBug()).thenReturn(bugCards);

        StackCardsDTO stackCardsDTO = gameService.getStackCards();

        Assert.assertSame(stackCardsDTO.getBugCards(), bugCards);
        Assert.assertSame(stackCardsDTO.getJuniorCards(), juniorCards);

        Mockito.verify(juniorCardService, Mockito.times(1)).getCardsWithoutPlayer();
        Mockito.verify(bugCardService, Mockito.times(1)).getCardsWithoutBug();
    }

    @Test
    public void shouldScoreForBugWithSuccess() {
        BugCardDTO bugCardDTO = new BugCardDTO(5L, "title", "description", 3, 4, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 20, 20);
        RoundDTO roundDTO = new RoundDTO(playerDTO, bugDTO, 5L);

        Mockito.when(bugCardService.selectRandomCard()).thenReturn(bugCardDTO);
        Mockito.when(bugService.findById(1L)).thenReturn(bugDTO);
        Mockito.when(playerService.findById(1L)).thenReturn(playerDTO);
        Mockito.doNothing().when(bugCardService).removeCardFromBug(bugCardDTO);

        bugDTO.setMana(17);
        playerDTO.setLife(16);

        Mockito.when(bugService.updateBug(bugDTO)).thenReturn(bugDTO);
        Mockito.when(playerService.update(playerDTO)).thenReturn(playerDTO);
        Mockito.when(mapper.toDto(playerDTO, bugDTO, 5L)).thenReturn(roundDTO);

        RoundDTO roundDTOScored = gameService.scoreboardBug(1L, 1L);

        Assert.assertSame(roundDTOScored, roundDTO);

        Mockito.verify(bugCardService, Mockito.times(1)).selectRandomCard();
        Mockito.verify(bugService, Mockito.times(1)).findById(1L);
        Mockito.verify(playerService, Mockito.times(1)).findById(1L);
        Mockito.verify(bugCardService, Mockito.times(1)).removeCardFromBug(bugCardDTO);
        Mockito.verify(bugService, Mockito.times(1)).updateBug(bugDTO);
        Mockito.verify(playerService, Mockito.times(1)).update(playerDTO);
        Mockito.verify(mapper, Mockito.times(1)).toDto(playerDTO, bugDTO, 5L);
    }

    @Test
    public void shouldThrowAnExceptionWhenBugHasInsufficientMana() {
        BugCardDTO bugCardDTO = new BugCardDTO(5L, "title", "description", 3, 4, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 1);
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 20, 20);

        Mockito.when(bugCardService.selectRandomCard()).thenReturn(bugCardDTO);
        Mockito.when(bugService.findById(1L)).thenReturn(bugDTO);
        Mockito.when(playerService.findById(1L)).thenReturn(playerDTO);


        try {
            gameService.scoreboardBug(1L, 1L);
        } catch (InsufficientMana ex) {
            Assert.assertSame(ex.getCode(), MagicErrorCode.MEC006);
        } finally {
            Mockito.verify(bugCardService, Mockito.times(1)).selectRandomCard();
            Mockito.verify(bugService, Mockito.times(1)).findById(1L);
            Mockito.verify(playerService, Mockito.times(1)).findById(1L);
        }
    }

    @Test
    public void shouldScoreForPlayerWithSuccess() {
        Long cardId = 5L;
        Long playerId = 1L;
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(cardId, "title", "description", 3, 4, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        PlayerDTO playerDTO = new PlayerDTO(playerId, "player", 20, 20);
        RoundDTO roundDTO = new RoundDTO(playerDTO, bugDTO, cardId);

        Mockito.when(juniorCardService.findByPlayerId(cardId, playerId)).thenReturn(juniorCardDTO);
        Mockito.when(playerService.findById(1L)).thenReturn(playerDTO);
        Mockito.when(bugService.findById(1L)).thenReturn(bugDTO);
        Mockito.doNothing().when(juniorCardService).removeCardFromJunior(juniorCardDTO);

        playerDTO.setMana(17);
        bugDTO.setLife(16);

        Mockito.when(bugService.updateBug(bugDTO)).thenReturn(bugDTO);
        Mockito.when(playerService.update(playerDTO)).thenReturn(playerDTO);
        Mockito.when(mapper.toDto(playerDTO, bugDTO, cardId)).thenReturn(roundDTO);

        RoundDTO roundDTOScored = gameService.scoreboardPlayer(playerId, cardId);

        Assert.assertSame(roundDTOScored, roundDTO);

        Mockito.verify(bugService, Mockito.times(1)).findById(1L);
        Mockito.verify(playerService, Mockito.times(1)).findById(1L);
        Mockito.verify(juniorCardService, Mockito.times(1)).removeCardFromJunior(juniorCardDTO);
        Mockito.verify(bugService, Mockito.times(1)).updateBug(bugDTO);
        Mockito.verify(playerService, Mockito.times(1)).update(playerDTO);
        Mockito.verify(mapper, Mockito.times(1)).toDto(playerDTO, bugDTO, cardId);
    }

    @Test
    public void shouldThrowAnExceptionWhenPlayerHasInsufficientMana() {
        Long cardId = 5L;
        Long playerId = 1L;
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(cardId, "title", "description", 3, 4, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        PlayerDTO playerDTO = new PlayerDTO(playerId, "player", 2, 20);

        Mockito.when(juniorCardService.findByPlayerId(cardId, playerId)).thenReturn(juniorCardDTO);
        Mockito.when(playerService.findById(1L)).thenReturn(playerDTO);
        Mockito.when(bugService.findById(1L)).thenReturn(bugDTO);

        try {
            gameService.scoreboardPlayer(playerId, cardId);
        } catch (InsufficientMana ex) {
            Assert.assertSame(ex.getCode(), MagicErrorCode.MEC006);
        } finally {
            Mockito.verify(bugService, Mockito.times(1)).findById(1L);
            Mockito.verify(playerService, Mockito.times(1)).findById(1L);
        }
    }

    @Test
    public void shouldEndTurn() {
        Long playerId = 1L;
        Long bugId = 1L;
        BugDTO bugDTO = new BugDTO(1L, 20, 15);
        PlayerDTO playerDTO = new PlayerDTO(playerId, "player", 2, 17);
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(3L, "title", "description", 3, 4, null);
        BugCardDTO bugCardDTO = new BugCardDTO(5L, "title", "description", 3, 4, null);

        Mockito.when(playerService.findById(playerId)).thenReturn(playerDTO);
        Mockito.when(bugService.findById(bugId)).thenReturn(bugDTO);
        Mockito.when(juniorCardService.getRandomCard()).thenReturn(juniorCardDTO);
        Mockito.when(bugCardService.selectRandomCard()).thenReturn(bugCardDTO);
        Mockito.doNothing().when(juniorCardService).saveCardsIntoPlayer(Collections.singletonList(juniorCardDTO), playerId);
        Mockito.doNothing().when(bugCardService).saveCardOnBug(bugCardDTO);

        EndTurnDTO endTurnDTO = gameService.endTurn(playerId, bugId);

        Assert.assertSame(endTurnDTO.getBugCardDTO(), bugCardDTO);
        Assert.assertSame(endTurnDTO.getBugDTO(), bugDTO);
        Assert.assertSame(endTurnDTO.getJuniorCardDTO(), juniorCardDTO);
        Assert.assertSame(endTurnDTO.getPlayerDTO(), playerDTO);

        Mockito.verify(playerService, Mockito.times(1)).findById(playerId);
        Mockito.verify(bugService, Mockito.times(1)).findById(bugId);
        Mockito.verify(juniorCardService, Mockito.times(1)).getRandomCard();
        Mockito.verify(bugCardService, Mockito.times(1)).selectRandomCard();
        Mockito.verify(juniorCardService, Mockito.times(1))
            .saveCardsIntoPlayer(Collections.singletonList(juniorCardDTO), playerId);
        Mockito.verify(bugCardService, Mockito.times(1)).saveCardOnBug(bugCardDTO);
    }

    private PlayerDTO buildPlayerDTO(Long id) {
        return new PlayerDTO(id, "player", 20, 20);
    }

    private GameDTO buildGameDTO(PlayerWithCardsDTO playerWithCardsDTO, BugWithCardsDTO bugWithCardsDTO) {
        return new GameDTO(
            playerWithCardsDTO,
            bugWithCardsDTO
        );
    }

    private List<JuniorCardDTO> buildStackJuniorCards() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/junior-cards-on-stack.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<JuniorCardDTO>>() {
        });
    }

    private List<BugCardDTO> buildStackBugCards() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/bug-cards-on-stack.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<BugCardDTO>>() {
        });
    }

    private List<JuniorCardDTO> buildJuniorCardsList() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader().
            getResourceAsStream("payloads/junior-cards-dto-payload.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<JuniorCardDTO>>() {
        });
    }
}
