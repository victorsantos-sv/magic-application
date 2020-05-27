package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.JuniorCardMapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.entity.model.JuniorCard;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.PlayerFullCards;
import br.com.magic.application.repositories.JuniorCardRepositorie;
import br.com.magic.application.services.IPlayerService;
import br.com.magic.application.services.impl.JuniorCardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JuniorCardServiceTest {

    private JuniorCardRepositorie juniorCardRepositorie = Mockito.mock(JuniorCardRepositorie.class);
    private IPlayerService playerService = Mockito.mock(IPlayerService.class);
    private JuniorCardMapper juniorCardMapper = Mockito.mock(JuniorCardMapper.class);
    private PlayerMapper playerMapper = Mockito.mock(PlayerMapper.class);
    private JuniorCardService juniorCardService = new JuniorCardService(juniorCardRepositorie, playerService, juniorCardMapper, playerMapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldFindAllTheCards() throws IOException {
        List<JuniorCard> juniorCards = buildJuniorCardList();
        List<JuniorCardDTO> juniorCardDTOList = buildJuniorCardDtoList();

        Mockito.when(juniorCardRepositorie.findAll()).thenReturn(juniorCards);
        Mockito.when(juniorCardMapper.toDto(juniorCards)).thenReturn(juniorCardDTOList);

        List<JuniorCardDTO> juniorCardDTOS = juniorCardService.getCards();

        Assert.assertSame(juniorCardDTOList, juniorCardDTOS);

        Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findAll();
        Mockito.verify(juniorCardMapper, Mockito.times(1)).toDto(juniorCards);
    }

    @Test
    void shouldFindAllCardsWithoutPlayer() {
        List<JuniorCard> juniorCards = buildStackJuniorCards();
        List<JuniorCardDTO> juniorCardDTOList = buildStackJuniorDtoCards();

        Mockito.when(juniorCardRepositorie.findAllByPlayerIsNullOrderById()).thenReturn(juniorCards);
        Mockito.when(juniorCardMapper.toDto(juniorCards)).thenReturn(juniorCardDTOList);

        List<JuniorCardDTO> juniorCardDTOS = juniorCardService.getCardsWithoutPlayer();

        Assert.assertSame(juniorCardDTOList, juniorCardDTOS);

        Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findAllByPlayerIsNullOrderById();
        Mockito.verify(juniorCardMapper, Mockito.times(1)).toDto(juniorCards);
    }

    @Test
    void shouldSaveCardsIntoPlayer() {
        List<JuniorCard> juniorCards = buildStackJuniorCards();
        List<JuniorCardDTO> juniorCardDTOList = buildStackJuniorDtoCards();
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 20, 20);
        Player player = new Player(1L, "player", 20, 20);

        juniorCards.get(0).setPlayer(player);

        Mockito.when(juniorCardMapper.toEntity(juniorCardDTOList)).thenReturn(juniorCards);
        Mockito.when(playerService.findById(1L)).thenReturn(playerDTO);
        Mockito.when(playerMapper.toEntity(playerDTO)).thenReturn(player);
        Mockito.when(juniorCardRepositorie.findAllByPlayer(player)).thenReturn(Collections.emptyList());
        Mockito.when(juniorCardRepositorie.saveAll(juniorCards)).thenReturn(null);

        juniorCardService.saveCardsIntoPlayer(juniorCardDTOList, 1L);

        Mockito.verify(juniorCardMapper, Mockito.times(1)).toEntity(juniorCardDTOList);
        Mockito.verify(playerService, Mockito.times(1)).findById(1L);
        Mockito.verify(playerMapper, Mockito.times(1)).toEntity(playerDTO);
        Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findAllByPlayer(player);
        Mockito.verify(juniorCardRepositorie, Mockito.times(1)).saveAll(juniorCards);
    }

    @Test
    void shouldThrowAnExceptionWhenUserIsFullCards() {
        List<JuniorCard> juniorCards = buildStackJuniorCards();
        List<JuniorCardDTO> juniorCardDTOList = buildStackJuniorDtoCards();
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 20, 20);
        Player player = new Player(1L, "player", 20, 20);
        List<JuniorCard> cardsWithUser = buildCardsWithUser(player);

        juniorCards.get(0).setPlayer(player);

        Mockito.when(juniorCardMapper.toEntity(juniorCardDTOList)).thenReturn(juniorCards);
        Mockito.when(playerService.findById(1L)).thenReturn(playerDTO);
        Mockito.when(playerMapper.toEntity(playerDTO)).thenReturn(player);
        Mockito.when(juniorCardRepositorie.findAllByPlayer(player)).thenReturn(cardsWithUser);

        try {
            juniorCardService.saveCardsIntoPlayer(juniorCardDTOList, 1L);
        } catch (PlayerFullCards ex) {
            Assert.assertSame(ex.getCode(), MagicErrorCode.MEC002);
        } finally {
            Mockito.verify(juniorCardMapper, Mockito.times(1)).toEntity(juniorCardDTOList);
            Mockito.verify(playerService, Mockito.times(1)).findById(1L);
            Mockito.verify(playerMapper, Mockito.times(1)).toEntity(playerDTO);
            Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findAllByPlayer(player);
        }
    }

    private List<JuniorCard> buildCardsWithUser(Player player) {
        List<JuniorCard> juniorCards = new ArrayList<>();
        JuniorCard juniorCard = new JuniorCard(1L, "title", "description", 3, 5, null, player);
        JuniorCard juniorCard1 = new JuniorCard(2L, "title", "description", 3, 5, null, player);
        JuniorCard juniorCard2 = new JuniorCard(3L, "title", "description", 3, 5, null, player);
        JuniorCard juniorCard3 = new JuniorCard(4L, "title", "description", 3, 5, null, player);

        juniorCards.add(juniorCard);
        juniorCards.add(juniorCard1);
        juniorCards.add(juniorCard2);
        juniorCards.add(juniorCard3);

        return juniorCards;
    }

    private List<JuniorCardDTO> buildStackJuniorDtoCards() {
        List<JuniorCardDTO> juniorCardDTOList = new ArrayList<>();
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(1L, "title", "description", 3, 5, null);

        juniorCardDTOList.add(juniorCardDTO);

        return juniorCardDTOList;
    }

    private List<JuniorCard> buildStackJuniorCards() {
        List<JuniorCard> juniorCards = new ArrayList<>();
        JuniorCard juniorCard = new JuniorCard(1L, "title", "description", 3, 5, null, null);

        juniorCards.add(juniorCard);

        return juniorCards;
    }

    private List<JuniorCardDTO> buildJuniorCardDtoList() throws IOException {
        String bugCardJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/junior-cards-dto-payload.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(bugCardJson, new TypeReference<List<JuniorCardDTO>>() {});
    }

    private List<JuniorCard> buildJuniorCardList() throws IOException {
        String bugCardJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/junior-cards-entity.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(bugCardJson, new TypeReference<List<JuniorCard>>() {});
    }
}
