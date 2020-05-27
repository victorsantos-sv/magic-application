package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.JuniorCardMapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.entity.model.JuniorCard;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.CardNotFound;
import br.com.magic.application.exception.FullCards;
import br.com.magic.application.repositories.JuniorCardRepositorie;
import br.com.magic.application.services.IPlayerService;
import br.com.magic.application.services.impl.JuniorCardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class JuniorCardServiceTest {

    private final JuniorCardRepositorie juniorCardRepositorie = Mockito.mock(JuniorCardRepositorie.class);
    private final IPlayerService playerService = Mockito.mock(IPlayerService.class);
    private final JuniorCardMapper juniorCardMapper = Mockito.mock(JuniorCardMapper.class);
    private final PlayerMapper playerMapper = Mockito.mock(PlayerMapper.class);
    private final JuniorCardService juniorCardService = new JuniorCardService(juniorCardRepositorie, playerService, juniorCardMapper, playerMapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldFindAllTheCards() throws IOException {
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
    public void shouldFindAllCardsWithoutPlayer() {
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
    public void shouldSaveCardsIntoPlayer() {
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
    public void shouldThrowAnExceptionWhenUserIsFullCards() {
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
        } catch (FullCards ex) {
            Assert.assertSame(ex.getCode(), MagicErrorCode.MEC002);
        } finally {
            Mockito.verify(juniorCardMapper, Mockito.times(1)).toEntity(juniorCardDTOList);
            Mockito.verify(playerService, Mockito.times(1)).findById(1L);
            Mockito.verify(playerMapper, Mockito.times(1)).toEntity(playerDTO);
            Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findAllByPlayer(player);
        }
    }

    @Test
    public void shouldFindCardByIdWithSuccess() {
        Long id = 1L;
        JuniorCard juniorCard = new JuniorCard(id, "title", "description", 4, 6, null, null);
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(id, "title", "description", 4, 5, null);

        Mockito.when(juniorCardRepositorie.findById(id)).thenReturn(Optional.of(juniorCard));
        Mockito.when(juniorCardMapper.toDto(juniorCard)).thenReturn(juniorCardDTO);

        JuniorCardDTO juniorCardDTOFound = juniorCardService.findById(id);

        Assert.assertSame(juniorCardDTO, juniorCardDTOFound);

        Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findById(id);
        Mockito.verify(juniorCardMapper, Mockito.times(1)).toDto(juniorCard);
    }

    @Test
    public void shouldThrowAnExceptionWhenCardNotFoundById() {
        Long id = 1L;

        Mockito.when(juniorCardRepositorie.findById(id)).thenReturn(Optional.empty());

        try {
            juniorCardService.findById(id);
        } catch (CardNotFound ex) {
            Assert.assertSame(ex.getCode(), MagicErrorCode.MEC004);
        } finally {
            Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findById(id);
        }
    }

    @Test
    public void shouldFindCardByIdAndPlayerIdWithSuccess() {
        Long cardId = 4L;
        Long playerId = 4L;
        Player player = new Player(playerId, "player", 20, 20);
        JuniorCard juniorCard = new JuniorCard(cardId, "title", "description", 4, 6, null, player);
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(cardId, "title", "description", 4, 5, null);

        Mockito.when(juniorCardRepositorie.findByIdAndPlayerId(cardId, playerId)).thenReturn(Optional.of(juniorCard));
        Mockito.when(juniorCardMapper.toDto(juniorCard)).thenReturn(juniorCardDTO);

        JuniorCardDTO juniorCardDTOFound = juniorCardService.findByPlayerId(cardId, playerId);

        Assert.assertSame(juniorCardDTO, juniorCardDTOFound);

        Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findByIdAndPlayerId(cardId, playerId);
        Mockito.verify(juniorCardMapper, Mockito.times(1)).toDto(juniorCard);
    }

    @Test
    public void shouldThrowAnExceptionWhenCardNotFoundByIdAndPlayerId() {
        Long cardId = 4L;
        Long playerId = 1L;

        Mockito.when(juniorCardRepositorie.findByIdAndPlayerId(cardId, playerId)).thenReturn(Optional.empty());

        try {
            juniorCardService.findByPlayerId(cardId, playerId);
        } catch (CardNotFound ex) {
            Assert.assertSame(ex.getCode(), MagicErrorCode.MEC004);
        } finally {
            Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findByIdAndPlayerId(cardId, playerId);
        }
    }

    @Test
    public void shouldGetARandomCardWithSuccess() {
        List<JuniorCard> juniorCards = buildCardsWithUser(null);
        List<JuniorCardDTO> juniorCardDTOList = buildCardsWithUserDTO();

        Mockito.when(juniorCardRepositorie.findAllByPlayerIsNullOrderById()).thenReturn(juniorCards);
        Mockito.when(juniorCardMapper.toDto(juniorCards)).thenReturn(juniorCardDTOList);

        JuniorCardDTO randomCard = juniorCardService.getRandomCard();

        Assert.assertNotNull(randomCard);
        Assert.assertTrue(juniorCardDTOList.contains(randomCard));

        Mockito.verify(juniorCardRepositorie, Mockito.times(1)).findAllByPlayerIsNullOrderById();
    }

    @Test
    public void shouldRemoveCardFromPlayerWithSuccess() {
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(1L, "title", "description", 3, 5, null);
        JuniorCard juniorCard = new JuniorCard(1L, "title", "description", 3, 5, null, null);
        ArgumentCaptor<JuniorCard> argumentCaptor = ArgumentCaptor.forClass(JuniorCard.class);

        Mockito.when(juniorCardMapper.toEntity(juniorCardDTO)).thenReturn(juniorCard);
        Mockito.when(juniorCardRepositorie.save(juniorCard)).thenReturn(juniorCard);

        juniorCardService.removeCardFromJunior(juniorCardDTO);

        Mockito.verify(juniorCardMapper, Mockito.times(1)).toEntity(juniorCardDTO);
        Mockito.verify(juniorCardRepositorie, Mockito.times(1)).save(argumentCaptor.capture());

        Assert.assertSame(juniorCard, argumentCaptor.getValue());
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

    private List<JuniorCardDTO> buildCardsWithUserDTO() {
        List<JuniorCardDTO> juniorCardsDtoList = new ArrayList<>();
        JuniorCardDTO juniorCard = new JuniorCardDTO(1L, "title", "description", 3, 5, null);
        JuniorCardDTO juniorCard1 = new JuniorCardDTO(2L, "title", "description", 3, 5, null);
        JuniorCardDTO juniorCard2 = new JuniorCardDTO(3L, "title", "description", 3, 5, null);
        JuniorCardDTO juniorCard3 = new JuniorCardDTO(4L, "title", "description", 3, 5, null);

        juniorCardsDtoList.add(juniorCard);
        juniorCardsDtoList.add(juniorCard1);
        juniorCardsDtoList.add(juniorCard2);
        juniorCardsDtoList.add(juniorCard3);

        return juniorCardsDtoList;
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
            .getResourceAsStream("payloads/junior-cards-dto-payload.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(bugCardJson, new TypeReference<List<JuniorCardDTO>>() {
        });
    }

    private List<JuniorCard> buildJuniorCardList() throws IOException {
        String bugCardJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/junior-cards-entity.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(bugCardJson, new TypeReference<List<JuniorCard>>() {
        });
    }
}
