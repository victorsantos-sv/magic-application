package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.JuniorCardDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.BugCardMapper;
import br.com.magic.application.entity.mapper.BugMapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.entity.model.BugCard;
import br.com.magic.application.entity.model.JuniorCard;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.FullCards;
import br.com.magic.application.repositories.BugCardRepositorie;
import br.com.magic.application.services.IBugService;
import br.com.magic.application.services.IPlayerService;
import br.com.magic.application.services.impl.BugCardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class BugCardServiceTest {
    private BugCardRepositorie bugCardRepositorie = Mockito.mock(BugCardRepositorie.class);
    private BugCardMapper bugCardMapper = Mockito.mock(BugCardMapper.class);
    private BugCardService bugCardService = new BugCardService(bugCardRepositorie, bugCardMapper);
    private IBugService bugService = Mockito.mock(IBugService.class);
    private BugMapper bugMapper = Mockito.mock(BugMapper.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldFindAllCardsWithoutUse() throws IOException {
        List<BugCard> bugCards = buildBugCardList();
        List<BugCardDTO> bugCardDTOList = buildBugCardDtoList();

        Mockito.when(bugCardRepositorie.findAllByIsInUseFalse()).thenReturn(bugCards);
        Mockito.when(bugCardMapper.toDtoList(bugCards)).thenReturn(bugCardDTOList);

        List<BugCardDTO> bugCardDTOS = bugCardService.getCardsWithoutBug();

        Assert.assertSame(bugCardDTOList, bugCardDTOS);

        Mockito.verify(bugCardRepositorie, Mockito.times(1)).findAllByIsInUseFalse();
        Mockito.verify(bugCardMapper, Mockito.times(1)).toDtoList(bugCards);
    }

    @Test
    void shouldGetARandomCardWithSuccess() {
        List<BugCard> bugCards = buildCardsIsInUse();

        Mockito.when(bugCardRepositorie.findAllByIsInUseTrue()).thenReturn(bugCards);

        bugCardService.selectRandomCard();

        Mockito.when(bugCardRepositorie.findAllByIsInUseTrue()).thenReturn(bugCards);
        Mockito.verify(bugCardRepositorie, Mockito.times(1)).findAllByIsInUseTrue();

    }


    @Test
    void shouldRemoveCardFromBugWithSuccess() {
        BugCardDTO bugCardDTO = new BugCardDTO(1L, "title", "description", 3, 5, null);
        BugCard bugCard = new BugCard(1L, "title", "description", 3, 5, null, null);
        ArgumentCaptor<BugCard> argumentCaptor = ArgumentCaptor.forClass(BugCard.class);

        Mockito.when(bugCardMapper.toEntity(bugCardDTO)).thenReturn(bugCard);
        Mockito.when(bugCardRepositorie.save(bugCard)).thenReturn(bugCard);

        bugCardService.removeCardFromBug(bugCardDTO);

        Mockito.verify(bugCardMapper, Mockito.times(1)).toEntity(bugCardDTO);
        Mockito.verify(bugCardRepositorie, Mockito.times(1)).save(argumentCaptor.capture());

        Assert.assertSame(bugCard, argumentCaptor.getValue());
    }


    private List<BugCard> buildCardsIsInUse() {
        List<BugCard> cards = new ArrayList<>();
        BugCard bugCard1 = new BugCard(1L, "title", "description", 3, 5, null, true);
        BugCard bugCard2 = new BugCard(2L, "title", "description", 3, 5, null, true);
        BugCard bugCard3 = new BugCard(3L, "title", "description", 3, 5, null, true);
        cards.add(bugCard1);
        cards.add(bugCard2);
        cards.add(bugCard3);


        return cards;
    }



    private List<BugCardDTO> buildBugCardDtoList() throws IOException {
        String bugCardJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/bug-cards-on-stack.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(bugCardJson, new TypeReference<List<BugCardDTO>>() {});
    }

    private List<BugCard> buildBugCardList() throws IOException {
        String bugCardJson = IOUtils.toString(getClass().getClassLoader()
            .getResourceAsStream("payloads/bug-cards-entity.json"), Charset.forName("UTF-8"));

        return objectMapper.readValue(bugCardJson, new TypeReference<List<BugCard>>() {});
    }
}
