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
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    private final IPlayerService playerService = mock(IPlayerService.class);
    private final IJuniorCardService juniorCardService = mock(IJuniorCardService.class);
    private final IBugCardService bugCardService = mock(IBugCardService.class);
    private final IBugService bugService = mock(IBugService.class);
    private final GameMapper mapper = mock(GameMapper.class);
    private final GameService gameService = new GameService(playerService, juniorCardService, bugCardService, bugService, mapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldLoadBoardGame() throws IOException {
        Long playerId = 1L;
        Long bugId = 1L;
        List<JuniorCardDTO> juniorCardDTOList = buildJuniorCardsList();
        List<BugCardDTO> bugCardDTOList = buildBugCardsInUse();
        PlayerWithCardsDTO playerWithCardsDTO = new PlayerWithCardsDTO(playerId, "player", 20, 20, juniorCardDTOList);
        BugWithCardsDTO bugWithCardsDTO = new BugWithCardsDTO(bugId, 20, 20, bugCardDTOList);
        GameDTO gameDTO = buildGameDTO(playerWithCardsDTO, bugWithCardsDTO);

        when(juniorCardService.saveCardsIntoPlayer(playerId)).thenReturn(playerWithCardsDTO);
        when(bugService.getInitialCards(bugId)).thenReturn(bugWithCardsDTO);
        when(mapper.toDto(playerWithCardsDTO, bugWithCardsDTO)).thenReturn(gameDTO);

        GameDTO gameDTOLoaded = gameService.loadBoard(bugId, playerId);

        assertSame(gameDTOLoaded, gameDTO);

        verify(juniorCardService, times(1)).saveCardsIntoPlayer(playerId);
        verify(bugService, times(1)).getInitialCards(bugId);
        verify(mapper, times(1)).toDto(playerWithCardsDTO, bugWithCardsDTO);

    }

    @Test
    public void shouldLoadAllStackCards() throws IOException {
        List<JuniorCardDTO> juniorCards = buildStackJuniorCards();
        List<BugCardDTO> bugCards = buildStackBugCards();

        when(juniorCardService.getCardsWithoutPlayer()).thenReturn(juniorCards);
        when(bugCardService.getCardsWithoutBug()).thenReturn(bugCards);

        StackCardsDTO stackCardsDTO = gameService.getStackCards();

        assertSame(stackCardsDTO.getBugCards(), bugCards);
        assertSame(stackCardsDTO.getJuniorCards(), juniorCards);

        verify(juniorCardService, times(1)).getCardsWithoutPlayer();
        verify(bugCardService, times(1)).getCardsWithoutBug();
    }

    @Test
    public void shouldScoreForBugWithSuccess() {
        BugCardDTO bugCardDTO = new BugCardDTO(5L, "title", "description", 3, 4, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 20, 20);
        RoundDTO roundDTO = new RoundDTO(playerDTO, bugDTO, 5L);

        when(bugCardService.selectRandomCard(1L)).thenReturn(bugCardDTO);
        when(bugService.findById(1L)).thenReturn(bugDTO);
        when(playerService.findById(1L)).thenReturn(playerDTO);
        doNothing().when(bugCardService).removeCardFromBug(bugCardDTO);

        bugDTO.setMana(17);
        playerDTO.setLife(16);

        when(bugService.updateBug(bugDTO)).thenReturn(bugDTO);
        when(playerService.update(playerDTO)).thenReturn(playerDTO);
        when(mapper.toDto(playerDTO, bugDTO, 5L)).thenReturn(roundDTO);

        RoundDTO roundDTOScored = gameService.scoreboardBug(1L, 1L);

        assertSame(roundDTOScored, roundDTO);

        verify(bugCardService, times(1)).selectRandomCard(1L);
        verify(bugService, times(1)).findById(1L);
        verify(playerService, times(1)).findById(1L);
        verify(bugCardService, times(1)).removeCardFromBug(bugCardDTO);
        verify(bugService, times(1)).updateBug(bugDTO);
        verify(playerService, times(1)).update(playerDTO);
        verify(mapper, times(1)).toDto(playerDTO, bugDTO, 5L);
    }

    @Test
    public void shouldThrowAnExceptionWhenBugHasInsufficientMana() {
        BugCardDTO bugCardDTO = new BugCardDTO(5L, "title", "description", 3, 4, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 1);
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 20, 20);

        when(bugCardService.selectRandomCard(1L)).thenReturn(bugCardDTO);
        when(bugService.findById(1L)).thenReturn(bugDTO);
        when(playerService.findById(1L)).thenReturn(playerDTO);

        InsufficientMana insufficientMana = assertThrows(InsufficientMana.class, () ->
            gameService.scoreboardBug(1L, 1L)
        );

        assertSame(insufficientMana.getCode(), MagicErrorCode.MEC006);

        verify(bugCardService, times(1)).selectRandomCard(1L);
        verify(bugService, times(1)).findById(1L);
        verify(playerService, times(1)).findById(1L);
    }

    @Test
    public void shouldScoreForPlayerWithSuccess() {
        Long cardId = 5L;
        Long playerId = 1L;
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(cardId, "title", "description", 3, 4, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        PlayerDTO playerDTO = new PlayerDTO(playerId, "player", 20, 20);
        RoundDTO roundDTO = new RoundDTO(playerDTO, bugDTO, cardId);

        when(juniorCardService.findByPlayerId(cardId, playerId)).thenReturn(juniorCardDTO);
        when(playerService.findById(1L)).thenReturn(playerDTO);
        when(bugService.findById(1L)).thenReturn(bugDTO);
        doNothing().when(juniorCardService).removeCardFromJunior(juniorCardDTO);

        playerDTO.setMana(17);
        bugDTO.setLife(16);

        when(bugService.updateBug(bugDTO)).thenReturn(bugDTO);
        when(playerService.update(playerDTO)).thenReturn(playerDTO);
        when(mapper.toDto(playerDTO, bugDTO, cardId)).thenReturn(roundDTO);

        RoundDTO roundDTOScored = gameService.scoreboardPlayer(playerId, cardId, 1L);

        assertSame(roundDTOScored, roundDTO);

        verify(bugService, times(1)).findById(1L);
        verify(playerService, times(1)).findById(1L);
        verify(juniorCardService, times(1)).removeCardFromJunior(juniorCardDTO);
        verify(bugService, times(1)).updateBug(bugDTO);
        verify(playerService, times(1)).update(playerDTO);
        verify(mapper, times(1)).toDto(playerDTO, bugDTO, cardId);
    }

    @Test
    public void shouldThrowAnExceptionWhenPlayerHasInsufficientMana() {
        Long cardId = 5L;
        Long playerId = 1L;
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(cardId, "title", "description", 3, 4, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        PlayerDTO playerDTO = new PlayerDTO(playerId, "player", 2, 20);

        when(juniorCardService.findByPlayerId(cardId, playerId)).thenReturn(juniorCardDTO);
        when(playerService.findById(1L)).thenReturn(playerDTO);
        when(bugService.findById(1L)).thenReturn(bugDTO);

        InsufficientMana insufficientMana = assertThrows(InsufficientMana.class, () ->
            gameService.scoreboardPlayer(playerId, cardId, 1L)
        );

        assertSame(insufficientMana.getCode(), MagicErrorCode.MEC006);

        verify(bugService, times(1)).findById(1L);
        verify(playerService, times(1)).findById(1L);
    }

    @Test
    public void shouldEndTurn() {
        Long playerId = 1L;
        Long bugId = 1L;
        BugDTO bugDTO = new BugDTO(1L, 20, 15);
        PlayerDTO playerDTO = new PlayerDTO(playerId, "player", 2, 17);
        BugCardDTO bugCardDTO = new BugCardDTO(5L, "title", "description", 3, 4, null);

        when(playerService.findById(playerId)).thenReturn(playerDTO);
        when(bugService.findById(bugId)).thenReturn(bugDTO);
        when(bugCardService.selectRandomCard(1L)).thenReturn(bugCardDTO);
        doNothing().when(bugCardService).saveCardOnBug(bugCardDTO, bugDTO);

        EndTurnDTO endTurnDTO = gameService.endTurn(playerId, bugId);

        assertSame(endTurnDTO.getBugCardDTO(), bugCardDTO);
        assertSame(endTurnDTO.getBugDTO(), bugDTO);
        assertSame(endTurnDTO.getPlayerDTO(), playerDTO);

        verify(playerService, times(1)).findById(playerId);
        verify(bugService, times(1)).findById(bugId);
        verify(bugCardService, times(1)).selectRandomCard(1L);
        verify(bugCardService, times(1)).saveCardOnBug(bugCardDTO, bugDTO);
    }

    @Test
    public void shouldLogoff() {
        Long playerId = 1L;
        Long bugId = 1L;

        doNothing().when(juniorCardService).removeAllCards();
        doNothing().when(playerService).deleteById(playerId);
        doNothing().when(bugCardService).removeAllCards(bugId);
        doNothing().when(bugService).deleteAllBugs();

        gameService.logoff(bugId, playerId);

        verify(juniorCardService, times(1)).removeAllCards();
        verify(playerService, times(1)).deleteById(playerId);
        verify(bugCardService, times(1)).removeAllCards(bugId);
        verify(bugService, times(1)).deleteAllBugs();
    }

    private GameDTO buildGameDTO(PlayerWithCardsDTO playerWithCardsDTO, BugWithCardsDTO bugWithCardsDTO) {
        return new GameDTO(
            playerWithCardsDTO,
            bugWithCardsDTO
        );
    }

    private List<JuniorCardDTO> buildStackJuniorCards() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/junior-cards-on-stack.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<JuniorCardDTO>>() {
        });
    }

    private List<BugCardDTO> buildStackBugCards() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/bug-cards-on-stack.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<BugCardDTO>>() {
        });
    }

    private List<BugCardDTO> buildBugCardsInUse() throws IOException {
        String bugCardsJson = IOUtils.toString(getClass().getClassLoader().
            getResourceAsStream("payloads/bug-cards-dto-in-use.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(bugCardsJson, new TypeReference<List<BugCardDTO>>() {
        });
    }

    private List<JuniorCardDTO> buildJuniorCardsList() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader().
            getResourceAsStream("payloads/junior-cards-dto-payload.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<JuniorCardDTO>>() {
        });
    }
}
