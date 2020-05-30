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
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
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

        when(playerService.findById(id)).thenReturn(playerDTO);
        when(juniorCardService.getCards()).thenReturn(juniorCards);
        doNothing().when(juniorCardService).saveCardsIntoPlayer(anyList(), eq(id));
        when(bugService.getInitialCards()).thenReturn(bugWithCardsDTO);
        when(mapper.toDto(any(), any())).thenReturn(gameDTO);

        GameDTO gameDTOCreated = gameService.loadBoard(id);

        verify(playerService, times(1)).findById(id);
        verify(juniorCardService, times(1)).getCards();
        verify(juniorCardService, times(1)).saveCardsIntoPlayer(argumentCaptor.capture(), eq(id));
        verify(playerService, times(1)).findById(id);

        assertSame(argumentCaptor.getValue().size(), 4);
        assertSame(gameDTOCreated.getBugWithCardsDTO().getId(), gameDTO.getBugWithCardsDTO().getId());
        assertSame(gameDTOCreated.getPlayerWithCardsDTO().getId(), gameDTO.getPlayerWithCardsDTO().getId());
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

        when(bugCardService.selectRandomCard()).thenReturn(bugCardDTO);
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

        verify(bugCardService, times(1)).selectRandomCard();
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

        when(bugCardService.selectRandomCard()).thenReturn(bugCardDTO);
        when(bugService.findById(1L)).thenReturn(bugDTO);
        when(playerService.findById(1L)).thenReturn(playerDTO);

        InsufficientMana insufficientMana = assertThrows(InsufficientMana.class, () ->
            gameService.scoreboardBug(1L, 1L)
        );

        assertSame(insufficientMana.getCode(), MagicErrorCode.MEC006);

        verify(bugCardService, times(1)).selectRandomCard();
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

        RoundDTO roundDTOScored = gameService.scoreboardPlayer(playerId, cardId);

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
            gameService.scoreboardPlayer(playerId, cardId)
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
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(3L, "title", "description", 3, 4, null);
        BugCardDTO bugCardDTO = new BugCardDTO(5L, "title", "description", 3, 4, null);

        when(playerService.findById(playerId)).thenReturn(playerDTO);
        when(bugService.findById(bugId)).thenReturn(bugDTO);
        when(juniorCardService.getRandomCard()).thenReturn(juniorCardDTO);
        when(bugCardService.selectRandomCard()).thenReturn(bugCardDTO);
        doNothing().when(juniorCardService).saveCardsIntoPlayer(Collections.singletonList(juniorCardDTO), playerId);
        doNothing().when(bugCardService).saveCardOnBug(bugCardDTO);

        EndTurnDTO endTurnDTO = gameService.endTurn(playerId, bugId);

        assertSame(endTurnDTO.getBugCardDTO(), bugCardDTO);
        assertSame(endTurnDTO.getBugDTO(), bugDTO);
        assertSame(endTurnDTO.getJuniorCardDTO(), juniorCardDTO);
        assertSame(endTurnDTO.getPlayerDTO(), playerDTO);

        verify(playerService, times(1)).findById(playerId);
        verify(bugService, times(1)).findById(bugId);
        verify(juniorCardService, times(1)).getRandomCard();
        verify(bugCardService, times(1)).selectRandomCard();
        verify(juniorCardService, times(1))
            .saveCardsIntoPlayer(Collections.singletonList(juniorCardDTO), playerId);
        verify(bugCardService, times(1)).saveCardOnBug(bugCardDTO);
    }

    @Test
    public void shouldLogoff() {
        Long playerId = 1L;

        doNothing().when(juniorCardService).removeAllCards();
        doNothing().when(playerService).deleteById(playerId);
        doNothing().when(bugCardService).removeAllCards();
        doNothing().when(bugService).deleteAllBugs();

        gameService.logoff(playerId);

        verify(juniorCardService, times(1)).removeAllCards();
        verify(playerService, times(1)).deleteById(playerId);
        verify(bugCardService, times(1)).removeAllCards();
        verify(bugService, times(1)).deleteAllBugs();
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

    private List<JuniorCardDTO> buildJuniorCardsList() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader().
            getResourceAsStream("payloads/junior-cards-dto-payload.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<JuniorCardDTO>>() {
        });
    }
}
