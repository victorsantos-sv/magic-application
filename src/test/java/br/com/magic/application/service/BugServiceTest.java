package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.mapper.BugMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.exception.BugNotFound;
import br.com.magic.application.repositories.BugRepositorie;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.services.impl.BugService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class BugServiceTest {

    private final BugRepositorie bugRepositorie = Mockito.mock(BugRepositorie.class);
    private final IBugCardService bugCardService = Mockito.mock(IBugCardService.class);
    private final BugMapper bugMapper = Mockito.mock(BugMapper.class);
    private final BugService bugService = new BugService(bugRepositorie, bugCardService, bugMapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldLoadCardsAndCreateBug() throws IOException {
        List<BugCardDTO> bugCardDTOList = buildBugCardsInUse();
        Bug bug = new Bug();
        bug.setId(1L);
        BugWithCardsDTO bugWithCardsDTO = buildBugWithCardsDTO(bug, bugCardDTOList);
        ArgumentCaptor<Bug> argumentCaptor = ArgumentCaptor.forClass(Bug.class);

        Mockito.when(bugCardService.setCardsOnBug()).thenReturn(bugCardDTOList);
        Mockito.when(bugRepositorie.save(Mockito.any())).thenReturn(bug);
        Mockito.when(bugMapper.toDto(bug, bugCardDTOList)).thenReturn(bugWithCardsDTO);

        BugWithCardsDTO bugWithCardsDTOLoaded = bugService.getInitialCards();

        Assert.assertSame(bugWithCardsDTOLoaded, bugWithCardsDTO);

        Mockito.verify(bugRepositorie, Mockito.times(1)).save(argumentCaptor.capture());
        Mockito.verify(bugMapper, Mockito.times(1)).toDto(bug, bugCardDTOList);

        Assert.assertSame(argumentCaptor.getValue().getLife(), new Bug().getLife());
        Assert.assertSame(argumentCaptor.getValue().getMana(), new Bug().getMana());
    }

    @Test
    public void shouldFindBugById() {
        Long id = 1L;
        Bug bug = new Bug(id, 20, 20);
        BugDTO bugDTO = new BugDTO(id, 20, 20);

        Mockito.when(bugRepositorie.findById(id)).thenReturn(Optional.of(bug));
        Mockito.when(bugMapper.toDto(bug)).thenReturn(bugDTO);

        BugDTO bugDTOFound = bugService.findById(id);

        Assert.assertSame(bugDTOFound, bugDTO);

        Mockito.verify(bugRepositorie, Mockito.times(1)).findById(id);
        Mockito.verify(bugMapper, Mockito.times(1)).toDto(bug);
    }

    @Test
    public void shouldThrowAnExceptionWhenNotFindBug() {
        Long id = 1L;

        Mockito.when(bugRepositorie.findById(id)).thenReturn(Optional.empty());

        try {
            bugService.findById(id);
        } catch (BugNotFound ex) {
            Assert.assertSame(ex.getCode(), MagicErrorCode.MEC005);
        } finally {
            Mockito.verify(bugRepositorie, Mockito.times(1)).findById(id);
        }
    }

    @Test
    public void shouldUpdateBug() {
        BugDTO bugDTO = new BugDTO(1L, 15, 20);
        Bug bug = new Bug(1L, 15, 20);

        Mockito.when(bugMapper.toEntity(bugDTO)).thenReturn(bug);
        Mockito.when(bugMapper.toDto(bug)).thenReturn(bugDTO);
        Mockito.when(bugRepositorie.save(bug)).thenReturn(bug);

        BugDTO bugDTOUpdated = bugService.updateBug(bugDTO);

        Assert.assertSame(bugDTOUpdated, bugDTO);

        Mockito.verify(bugMapper, Mockito.times(1)).toEntity(bugDTO);
        Mockito.verify(bugMapper, Mockito.times(1)).toDto(bug);
        Mockito.verify(bugRepositorie, Mockito.times(1)).save(bug);
    }

    private BugWithCardsDTO buildBugWithCardsDTO(Bug bug, List<BugCardDTO> bugCardDTOList) {
        return new BugWithCardsDTO(bug.getId(), bug.getLife(), bug.getMana(), bugCardDTOList);
    }

    private List<BugCardDTO> buildBugCardsInUse() throws IOException {
        String bugCardDTOListJson = IOUtils.toString(getClass().getClassLoader().
            getResourceAsStream("payloads/bug-cards-dto-in-use.json"), StandardCharsets.UTF_8);

        return objectMapper.readValue(bugCardDTOListJson, new TypeReference<List<BugCardDTO>>() {
        });
    }
}
