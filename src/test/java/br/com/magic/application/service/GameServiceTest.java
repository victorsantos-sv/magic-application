package br.com.magic.application.service;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.StackCardsDTO;
import br.com.magic.application.entity.mapper.GameMapper;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.services.IJuniorCardService;
import br.com.magic.application.services.IPlayerService;
import br.com.magic.application.services.impl.GameService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GameServiceTest {

    private IPlayerService playerService = Mockito.mock(IPlayerService.class);
    private IJuniorCardService juniorCardService = Mockito.mock(IJuniorCardService.class);
    private IBugCardService bugCardService = Mockito.mock(IBugCardService.class);
    private GameMapper mapper = Mockito.mock(GameMapper.class);
    private GameService gameService = new GameService(playerService, juniorCardService, bugCardService, mapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldLoadBoardGame() throws IOException {
        Long id = 1L;
        PlayerDTO playerDTO = buildPlayerDTO(id);
        List<JuniorCardDTO> juniorCards = buildJuniorCardsList();
        GameDTO gameDTO = buildGameDTO(playerDTO, juniorCards);
        ArgumentCaptor<List<JuniorCardDTO>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        Mockito.when(playerService.findById(id)).thenReturn(playerDTO);
        Mockito.when(juniorCardService.getCards()).thenReturn(juniorCards);
        Mockito.doNothing().when(juniorCardService).saveCardsIntoPlayer(Mockito.anyList(), Mockito.eq(id));
        Mockito.when(mapper.toDto(Mockito.eq(playerDTO), Mockito.anyList())).thenReturn(gameDTO);

        GameDTO gameDTOCreated = gameService.loadBoard(id);

        Mockito.verify(playerService, Mockito.times(1)).findById(id);
        Mockito.verify(juniorCardService, Mockito.times(1)).getCards();
        Mockito.verify(juniorCardService, Mockito.times(1)).saveCardsIntoPlayer(argumentCaptor.capture(), Mockito.eq(id));
        Mockito.verify(playerService, Mockito.times(1)).findById(id);

        Assert.assertSame(argumentCaptor.getValue().size(), 4);
        Assert.assertSame(gameDTOCreated.getId(), playerDTO.getId());
    }

    @Test
    void shouldLoadAllStackCards() throws IOException {
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

    private PlayerDTO buildPlayerDTO(Long id) {
        return new PlayerDTO(id, "player", 20, 20);
    }

    private GameDTO buildGameDTO(PlayerDTO playerDTO, List<JuniorCardDTO> juniorCardDTOS) {
        List<JuniorCardDTO> juniorCardDTOList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            juniorCardDTOList.add(juniorCardDTOS.get(i));
        }

        return new GameDTO(
            playerDTO.getId(),
            playerDTO.getNickName(),
            playerDTO.getLife(),
            playerDTO.getMana(),
            juniorCardDTOList
        );
    }

    private List<JuniorCardDTO> buildStackJuniorCards() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/junior-cards-on-stack.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<JuniorCardDTO>>() {});
    }

    private List<BugCardDTO> buildStackBugCards() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/bug-cards-on-stack.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<BugCardDTO>>() {});
    }

    private List<JuniorCardDTO> buildJuniorCardsList() throws IOException {
        String juniorCardsJson = IOUtils.toString(getClass().getClassLoader().
            getResourceAsStream("payloads/junior-cards-dto-payload.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(juniorCardsJson, new TypeReference<List<JuniorCardDTO>>(){});
    }
}
