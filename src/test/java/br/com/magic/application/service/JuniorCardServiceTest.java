package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.dto.PlayerWithCardsDTO;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JuniorCardServiceTest {

    private final JuniorCardRepositorie juniorCardRepositorie = mock(JuniorCardRepositorie.class);
    private final IPlayerService playerService = mock(IPlayerService.class);
    private final JuniorCardMapper juniorCardMapper = mock(JuniorCardMapper.class);
    private final PlayerMapper playerMapper = mock(PlayerMapper.class);
    private final JuniorCardService juniorCardService = new JuniorCardService(juniorCardRepositorie, playerService, juniorCardMapper, playerMapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldFindAllTheCards() throws IOException {
        List<JuniorCard> juniorCards = buildJuniorCardList();
        List<JuniorCardDTO> juniorCardDTOList = buildJuniorCardDtoList();

        when(juniorCardRepositorie.findAll()).thenReturn(juniorCards);
        when(juniorCardMapper.toDto(juniorCards)).thenReturn(juniorCardDTOList);

        List<JuniorCardDTO> juniorCardDTOS = juniorCardService.getCards();

        assertSame(juniorCardDTOList, juniorCardDTOS);

        verify(juniorCardRepositorie, times(1)).findAll();
        verify(juniorCardMapper, times(1)).toDto(juniorCards);
    }

    @Test
    public void shouldFindAllCardsWithoutPlayer() {
        List<JuniorCard> juniorCards = buildStackJuniorCards();
        List<JuniorCardDTO> juniorCardDTOList = buildStackJuniorDtoCards();

        when(juniorCardRepositorie.findAllByPlayerIsNullOrderById()).thenReturn(juniorCards);
        when(juniorCardMapper.toDto(juniorCards)).thenReturn(juniorCardDTOList);

        List<JuniorCardDTO> juniorCardDTOS = juniorCardService.getCardsWithoutPlayer();

        assertSame(juniorCardDTOList, juniorCardDTOS);

        verify(juniorCardRepositorie, times(1)).findAllByPlayerIsNullOrderById();
        verify(juniorCardMapper, times(1)).toDto(juniorCards);
    }

    @Test
    public void shouldSaveCardsIntoPlayer() throws IOException {
        List<JuniorCard> juniorCards = buildJuniorCardList();
        List<JuniorCardDTO> juniorCardDTOList = buildJuniorCardDtoList();
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 20, 20);
        Player player = new Player(1L, "player", 20, 20);

        when(juniorCardRepositorie.findAll()).thenReturn(juniorCards);
        when(juniorCardMapper.toDto(juniorCards)).thenReturn(juniorCardDTOList);
        when(juniorCardMapper.toEntity(anyList())).thenReturn(juniorCards);
        when(playerService.findById(1L)).thenReturn(playerDTO);
        when(playerMapper.toEntity(playerDTO)).thenReturn(player);
        when(juniorCardRepositorie.saveAll(anyList())).thenReturn(juniorCards);
        when(juniorCardMapper.toDto(juniorCards)).thenReturn(juniorCardDTOList);

        PlayerWithCardsDTO playerWithCardsDTO = juniorCardService.saveCardsIntoPlayer(1L);

        assertNotNull(playerWithCardsDTO);

        verify(juniorCardRepositorie, times(1)).findAll();
        verify(juniorCardMapper, times(2)).toDto(juniorCards);
        verify(juniorCardMapper, times(1)).toEntity(anyList());
        verify(playerService, times(1)).findById(1L);
        verify(playerMapper, times(1)).toEntity(playerDTO);
        verify(juniorCardRepositorie, times(1)).saveAll(anyList());
    }

    @Test
    public void shouldFindCardByIdWithSuccess() {
        Long id = 1L;
        JuniorCard juniorCard = new JuniorCard(id, "title", "description", 4, 6, null, null);
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(id, "title", "description", 4, 5, null);

        when(juniorCardRepositorie.findById(id)).thenReturn(Optional.of(juniorCard));
        when(juniorCardMapper.toDto(juniorCard)).thenReturn(juniorCardDTO);

        JuniorCardDTO juniorCardDTOFound = juniorCardService.findById(id);

        assertSame(juniorCardDTO, juniorCardDTOFound);

        verify(juniorCardRepositorie, times(1)).findById(id);
        verify(juniorCardMapper, times(1)).toDto(juniorCard);
    }

    @Test
    public void shouldThrowAnExceptionWhenCardNotFoundById() {
        Long id = 1L;

        when(juniorCardRepositorie.findById(id)).thenReturn(Optional.empty());

        try {
            juniorCardService.findById(id);
        } catch (CardNotFound ex) {
            assertSame(ex.getCode(), MagicErrorCode.MEC004);
        } finally {
            verify(juniorCardRepositorie, times(1)).findById(id);
        }
    }

    @Test
    public void shouldFindCardByIdAndPlayerIdWithSuccess() {
        Long cardId = 4L;
        Long playerId = 4L;
        Player player = new Player(playerId, "player", 20, 20);
        JuniorCard juniorCard = new JuniorCard(cardId, "title", "description", 4, 6, null, player);
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(cardId, "title", "description", 4, 5, null);

        when(juniorCardRepositorie.findByIdAndPlayerId(cardId, playerId)).thenReturn(Optional.of(juniorCard));
        when(juniorCardMapper.toDto(juniorCard)).thenReturn(juniorCardDTO);

        JuniorCardDTO juniorCardDTOFound = juniorCardService.findByPlayerId(cardId, playerId);

        assertSame(juniorCardDTO, juniorCardDTOFound);

        verify(juniorCardRepositorie, times(1)).findByIdAndPlayerId(cardId, playerId);
        verify(juniorCardMapper, times(1)).toDto(juniorCard);
    }

    @Test
    public void shouldThrowAnExceptionWhenCardNotFoundByIdAndPlayerId() {
        Long cardId = 4L;
        Long playerId = 1L;

        when(juniorCardRepositorie.findByIdAndPlayerId(cardId, playerId)).thenReturn(Optional.empty());

        CardNotFound cardNotFound = assertThrows(CardNotFound.class, () ->
            juniorCardService.findByPlayerId(cardId, playerId)
        );

        assertSame(cardNotFound.getCode(), MagicErrorCode.MEC004);

        verify(juniorCardRepositorie, times(1)).findByIdAndPlayerId(cardId, playerId);
    }

    @Test
    public void shouldGetARandomCardWithSuccess() {
        List<JuniorCard> juniorCards = buildCardsWithUser(null);
        List<JuniorCardDTO> juniorCardDTOList = buildCardsWithUserDTO();

        when(juniorCardRepositorie.findAllByPlayerIsNullOrderById()).thenReturn(juniorCards);
        when(juniorCardMapper.toDto(juniorCards)).thenReturn(juniorCardDTOList);

        JuniorCardDTO randomCard = juniorCardService.getRandomCard();

        assertNotNull(randomCard);
        assertTrue(juniorCardDTOList.contains(randomCard));

        verify(juniorCardRepositorie, times(1)).findAllByPlayerIsNullOrderById();
    }

    @Test
    public void shouldSaveCartIntoPlayerWithSuccess() {
        Long playerId = 1L;
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(1L, "title", "description", 3, 5, null);
        JuniorCard juniorCard = new JuniorCard(1L, "title", "description", 3, 5, null, null);
        PlayerDTO playerDTO = new PlayerDTO(playerId, "player", 20, 20);
        Player player = new Player(playerId, "player", 20, 20);
        List<JuniorCard> juniorCards = buildCardsWithUser(player);
        juniorCards.remove(3);
        ArgumentCaptor<JuniorCard> argumentCaptor = ArgumentCaptor.forClass(JuniorCard.class);

        when(juniorCardRepositorie.findAllByPlayerId(playerId)).thenReturn(juniorCards);
        when(playerService.findById(playerId)).thenReturn(playerDTO);
        when(juniorCardMapper.toEntity(juniorCardDTO)).thenReturn(juniorCard);
        when(playerMapper.toEntity(playerDTO)).thenReturn(player);
        when(juniorCardRepositorie.save(any())).thenReturn(null);

        juniorCardService.saveCardIntoPlayer(juniorCardDTO, playerId);

        verify(juniorCardRepositorie, times(1)).findAllByPlayerId(playerId);
        verify(playerService, times(1)).findById(playerId);
        verify(juniorCardMapper, times(1)).toEntity(juniorCardDTO);
        verify(playerMapper, times(1)).toEntity(playerDTO);
        verify(juniorCardRepositorie, times(1)).save(argumentCaptor.capture());

        assertNotNull(argumentCaptor.getValue().getPlayer());
    }

    @Test
    public void shouldThrowAnExceptionWhenPlayerIsFullCards() {
        Long playerId = 1L;
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(1L, "title", "description", 3, 5, null);
        PlayerDTO playerDTO = new PlayerDTO(playerId, "player", 20, 20);
        Player player = new Player(playerId, "player", 20, 20);
        List<JuniorCard> juniorCards = buildCardsWithUser(player);

        when(juniorCardRepositorie.findAllByPlayerId(playerId)).thenReturn(juniorCards);
        when(playerService.findById(playerId)).thenReturn(playerDTO);

        FullCards fullCards = assertThrows(FullCards.class, () ->
            juniorCardService.saveCardIntoPlayer(juniorCardDTO, playerId)
        );

        assertSame(fullCards.getCode(), MagicErrorCode.MEC002);

        verify(juniorCardRepositorie, times(1)).findAllByPlayerId(playerId);
        verify(playerService, times(1)).findById(playerId);
    }

    @Test
    public void shouldRemoveCardFromPlayerWithSuccess() {
        JuniorCardDTO juniorCardDTO = new JuniorCardDTO(1L, "title", "description", 3, 5, null);
        JuniorCard juniorCard = new JuniorCard(1L, "title", "description", 3, 5, null, null);
        ArgumentCaptor<JuniorCard> argumentCaptor = ArgumentCaptor.forClass(JuniorCard.class);

        when(juniorCardMapper.toEntity(juniorCardDTO)).thenReturn(juniorCard);
        when(juniorCardRepositorie.save(juniorCard)).thenReturn(juniorCard);

        juniorCardService.removeCardFromJunior(juniorCardDTO);

        verify(juniorCardMapper, times(1)).toEntity(juniorCardDTO);
        verify(juniorCardRepositorie, times(1)).save(argumentCaptor.capture());

        assertSame(juniorCard, argumentCaptor.getValue());
    }

    @Test
    public void shouldRemoveAllCards() throws IOException {
        List<JuniorCard> juniorCards = buildJuniorCardList();

        when(juniorCardRepositorie.findAll()).thenReturn(juniorCards);
        when(juniorCardRepositorie.saveAll(juniorCards)).thenReturn(null);

        juniorCardService.removeAllCards();

        verify(juniorCardRepositorie, times(1)).findAll();
        verify(juniorCardRepositorie, times(1)).saveAll(juniorCards);
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
