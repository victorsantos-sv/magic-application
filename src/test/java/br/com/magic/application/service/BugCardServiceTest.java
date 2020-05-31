package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.mapper.BugCardMapper;
import br.com.magic.application.entity.mapper.BugMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.entity.model.BugCard;
import br.com.magic.application.exception.CardNotFound;
import br.com.magic.application.exception.FullCards;
import br.com.magic.application.repositories.BugCardRepositorie;
import br.com.magic.application.services.impl.BugCardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BugCardServiceTest {
    private final BugCardRepositorie bugCardRepositorie = mock(BugCardRepositorie.class);
    private final BugCardMapper bugCardMapper = mock(BugCardMapper.class);
    private final BugMapper bugMapper = mock(BugMapper.class);
    private final BugCardService bugCardService = new BugCardService(bugCardRepositorie, bugCardMapper, bugMapper);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldFindAllCardsWithoutUse() throws IOException {
        List<BugCard> bugCards = buildBugCardList();
        List<BugCardDTO> bugCardDTOList = buildBugCardDtoList();

        when(bugCardRepositorie.findAllByBugIsNull()).thenReturn(bugCards);
        when(bugCardMapper.toDtoList(bugCards)).thenReturn(bugCardDTOList);

        List<BugCardDTO> bugCardDTOS = bugCardService.getCardsWithoutBug();

        assertSame(bugCardDTOList, bugCardDTOS);

        verify(bugCardRepositorie, times(1)).findAllByBugIsNull();
        verify(bugCardMapper, times(1)).toDtoList(bugCards);
    }

    @Test
    public void shouldGetARandomCardWithSuccess() {
        Long bugId = 1L;
        List<BugCard> bugCards = buildCardsIsInUse();
        BugCardDTO bugCardDTO = new BugCardDTO(1L, "title", "description", 4, 5, 3);

        when(bugCardRepositorie.findAllByBugId(bugId)).thenReturn(bugCards);
        when(bugCardMapper.toDto(any())).thenReturn(bugCardDTO);

        BugCardDTO bugCardDTOReturned = bugCardService.selectRandomCard(bugId);

        assertNotNull(bugCardDTOReturned);

        verify(bugCardRepositorie, times(1)).findAllByBugId(bugId);
    }

    @Test
    public void shouldRemoveCardFromBugWithSuccess() {
        BugCardDTO bugCardDTO = new BugCardDTO(1L, "title", "description", 3, 5, null);
        BugCard bugCard = new BugCard(1L, "title", "description", 3, 5, null, null);
        ArgumentCaptor<BugCard> argumentCaptor = ArgumentCaptor.forClass(BugCard.class);

        when(bugCardMapper.toEntity(bugCardDTO)).thenReturn(bugCard);
        when(bugCardRepositorie.save(bugCard)).thenReturn(bugCard);

        bugCardService.removeCardFromBug(bugCardDTO);

        verify(bugCardMapper, times(1)).toEntity(bugCardDTO);
        verify(bugCardRepositorie, times(1)).save(argumentCaptor.capture());

        assertSame(bugCard, argumentCaptor.getValue());
    }

    @Test
    public void shouldFindByIdWithSuccess() {
        Long id = 1L;
        Bug bug = new Bug(1L, 20, 20);
        BugCard bugCard = new BugCard(id, "title", "description", 3, 5, null, bug);
        BugCardDTO bugCardDTO = new BugCardDTO(id, "title", "description", 3, 5, null);

        when(bugCardRepositorie.findById(id)).thenReturn(Optional.of(bugCard));
        when(bugCardMapper.toDto(bugCard)).thenReturn(bugCardDTO);

        BugCardDTO bugCardDTOFound = bugCardService.findById(id);

        assertSame(bugCardDTO, bugCardDTOFound);

        verify(bugCardRepositorie, times(1)).findById(id);
        verify(bugCardMapper, times(1)).toDto(bugCard);
    }

    @Test
    public void shouldThrowAnExceptionWhenNotFindById() {
        Long id = 1L;

        when(bugCardRepositorie.findById(id)).thenReturn(Optional.empty());

        try {
            bugCardService.findById(id);
        } catch (CardNotFound ex) {
            assertSame(ex.getCode(), MagicErrorCode.MEC004);
        } finally {
            verify(bugCardRepositorie, times(1)).findById(id);
        }
    }

    @Test
    public void shouldSetCardsOnBug() {
        List<BugCard> bugCards = buildCardsIsInUse();
        List<BugCardDTO> bugCardDTOList = buildCardsDTOIsInUse();
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        Bug bug = new Bug(1L, 20, 20);
        ArgumentCaptor<List<BugCard>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        when(bugCardMapper.toDtoList(bugCards)).thenReturn(bugCardDTOList);
        when(bugCardMapper.toEntityList(Mockito.any())).thenReturn(bugCards);
        when(bugCardRepositorie.saveAll(Mockito.any())).thenReturn(null);
        when(bugCardRepositorie.findAll()).thenReturn(bugCards);
        when(bugCardMapper.toDtoList(bugCards)).thenReturn(bugCardDTOList);
        when(bugMapper.toEntity(bugDTO)).thenReturn(bug);

        BugWithCardsDTO bugWithCardsDTO = bugCardService.setCardsOnBug(bugDTO);

        verify(bugCardMapper, times(2)).toDtoList(bugCards);
        verify(bugCardMapper, times(1)).toEntityList(Mockito.any());
        verify(bugCardRepositorie, times(1)).saveAll(argumentCaptor.capture());
        verify(bugCardRepositorie, times(1)).findAll();
        verify(bugMapper, times(1)).toEntity(bugDTO);

        Assert.assertNotNull(argumentCaptor.getValue());
        Assert.assertNotNull(bugWithCardsDTO);
    }

    @Test
    public void shouldSaveCardOnBugWithSuccess() throws IOException {
        Long id = 1L;
        BugCard bugCard = new BugCard(id, "title", "description", 3, 5, null, null);
        BugCardDTO bugCardDTO = new BugCardDTO(id, "title", "description", 3, 5, null);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        List<BugCard> bugCards = buildBugCardList();
        List<BugCardDTO> bugCardDTOList = buildBugCardDtoList();
        bugCardDTOList.add(bugCardDTO);

        when(bugCardRepositorie.findAllByBugIsNull()).thenReturn(bugCards);
        when(bugCardMapper.toDtoList(bugCards)).thenReturn(bugCardDTOList);
        when(bugCardMapper.toEntity(bugCardDTO)).thenReturn(bugCard);
        when(bugCardRepositorie.save(bugCard)).thenReturn(bugCard);

        bugCardService.saveCardOnBug(bugCardDTO, bugDTO);

        verify(bugCardRepositorie, times(1)).findAllByBugIsNull();
        verify(bugCardMapper, times(1)).toDtoList(bugCards);
        verify(bugCardMapper, times(1)).toEntity(bugCardDTO);
        verify(bugCardRepositorie, times(1)).save(bugCard);
    }

    @Test
    public void shouldThrowAnExceptionWhenBugIsFullCards() throws IOException {
        Long id = 1L;
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        BugCard bugCard = new BugCard(id, "title", "description", 3, 5, null, null);
        BugCardDTO bugCardDTO = new BugCardDTO(id, "title", "description", 3, 5, null);
        List<BugCard> bugCards = buildBugCardList();
        List<BugCardDTO> bugCardDTOList = buildBugCardDtoList();

        when(bugCardRepositorie.findAllByBugIsNull()).thenReturn(bugCards);
        when(bugCardMapper.toDtoList(bugCards)).thenReturn(bugCardDTOList);
        when(bugCardMapper.toEntity(bugCardDTO)).thenReturn(bugCard);

        try {
            bugCardService.saveCardOnBug(bugCardDTO, bugDTO);
        } catch (FullCards ex) {
            assertSame(ex.getCode(), MagicErrorCode.MEC002);
        } finally {
            verify(bugCardRepositorie, times(1)).findAllByBugIsNull();
            verify(bugCardMapper, times(1)).toDtoList(bugCards);
            verify(bugCardMapper, times(1)).toEntity(bugCardDTO);
        }
    }

    @Test
    public void shouldRemoveAllCardsFromBug() throws IOException {
        Long id = 1L;
        List<BugCard> bugCards = buildBugCardList();

        when(bugCardRepositorie.findAllByBugId(id)).thenReturn(bugCards);
        when(bugCardRepositorie.saveAll(bugCards)).thenReturn(null);

        bugCardService.removeAllCards(id);

        Mockito.verify(bugCardRepositorie, times(1)).findAllByBugId(id);
        Mockito.verify(bugCardRepositorie, times(1)).saveAll(bugCards);
    }

    private List<BugCard> buildCardsIsInUse() {
        List<BugCard> bugCards = new ArrayList<>();
        BugCard bugCard1 = new BugCard(1L, "title", "description", 3, 5, null, null);
        BugCard bugCard2 = new BugCard(2L, "title", "description", 3, 5, null, null);
        BugCard bugCard3 = new BugCard(3L, "title", "description", 3, 5, null, null);
        BugCard bugCard4 = new BugCard(4L, "title", "description", 3, 5, null, null);
        bugCards.add(bugCard1);
        bugCards.add(bugCard2);
        bugCards.add(bugCard3);
        bugCards.add(bugCard4);

        return bugCards;
    }

    private List<BugCardDTO> buildCardsDTOIsInUse() {
        List<BugCardDTO> cardDTOS = new ArrayList<>();
        BugCardDTO bugCardDTO1 = new BugCardDTO(1L, "title", "description", 3, 5, null);
        BugCardDTO bugCardDTO2 = new BugCardDTO(2L, "title", "description", 3, 5, null);
        BugCardDTO bugCardDTO3 = new BugCardDTO(3L, "title", "description", 3, 5, null);
        BugCardDTO bugCardDTO4 = new BugCardDTO(4L, "title", "description", 3, 5, null);
        cardDTOS.add(bugCardDTO1);
        cardDTOS.add(bugCardDTO2);
        cardDTOS.add(bugCardDTO3);
        cardDTOS.add(bugCardDTO4);

        return cardDTOS;
    }

    private List<BugCardDTO> buildBugCardDtoList() throws IOException {
        String bugCardJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/bug-cards-on-stack.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(bugCardJson, new TypeReference<List<BugCardDTO>>() {
        });
    }

    private List<BugCard> buildBugCardList() throws IOException {
        String bugCardJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/bug-cards-entity.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(bugCardJson, new TypeReference<List<BugCard>>() {
        });
    }
}
