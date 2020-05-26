package br.com.magic.application.service;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.mapper.BugCardMapper;
import br.com.magic.application.entity.model.BugCard;
import br.com.magic.application.repositories.BugCardRepositorie;
import br.com.magic.application.services.impl.BugCardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BugCardServiceTest {
    private BugCardRepositorie bugCardRepositorie = Mockito.mock(BugCardRepositorie.class);
    private BugCardMapper bugCardMapper = Mockito.mock(BugCardMapper.class);
    private BugCardService bugCardService = new BugCardService(bugCardRepositorie, bugCardMapper);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldFindAllCardsWithoutUse() throws IOException {
        List<BugCard> bugCards = buildBugCardList();
        List<BugCardDTO> bugCardDTOList = buildBugCardDtoList();

        Mockito.when(bugCardRepositorie.findAllByIsInUseFalse()).thenReturn(bugCards);
        Mockito.when(bugCardMapper.toDto(bugCards)).thenReturn(bugCardDTOList);

        List<BugCardDTO> bugCardDTOS = bugCardService.getCardsWithoutBug();

        Assert.assertSame(bugCardDTOList, bugCardDTOS);

        Mockito.verify(bugCardRepositorie, Mockito.times(1)).findAllByIsInUseFalse();
        Mockito.verify(bugCardMapper, Mockito.times(1)).toDto(bugCards);
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
