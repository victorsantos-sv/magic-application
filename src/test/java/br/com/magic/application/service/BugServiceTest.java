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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BugServiceTest {

    private final BugRepositorie bugRepositorie = mock(BugRepositorie.class);
    private final IBugCardService bugCardService = mock(IBugCardService.class);
    private final BugMapper bugMapper = mock(BugMapper.class);
    private final BugService bugService = new BugService(bugRepositorie, bugCardService, bugMapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldCreateBugWithSuccess() {
        Bug bug = new Bug(1L, 20, 20);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        ArgumentCaptor<Bug> argumentCaptor = ArgumentCaptor.forClass(Bug.class);

        when(bugRepositorie.save(any())).thenReturn(bug);
        when(bugMapper.toDto(bug)).thenReturn(bugDTO);

        BugDTO bugDTOCreated = bugService.createBug();

        assertSame(bugDTOCreated, bugDTO);

        verify(bugRepositorie, times(1)).save(argumentCaptor.capture());
        verify(bugMapper, times(1)).toDto(bug);

        assertSame(argumentCaptor.getValue().getId(), null);
    }

    @Test
    public void shouldLoadCards() throws IOException {
        Long id = 1L;
        Bug bug = new Bug();
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        bug.setId(id);
        BugWithCardsDTO bugWithCardsDTO = buildBugWithCardsDTO(bug, buildBugCardsInUse());


        when(bugRepositorie.findById(id)).thenReturn(Optional.of(bug));
        when(bugMapper.toDto(bug)).thenReturn(bugDTO);
        when(bugCardService.setCardsOnBug(bugDTO)).thenReturn(bugWithCardsDTO);

        BugWithCardsDTO bugWithCardsDTOLoaded = bugService.getInitialCards(1L);

        assertSame(bugWithCardsDTOLoaded, bugWithCardsDTO);

        verify(bugRepositorie, times(1)).findById(id);
        verify(bugMapper, times(1)).toDto(bug);
        verify(bugCardService, times(1)).setCardsOnBug(bugDTO);
    }

    @Test
    public void shouldFindBugById() {
        Long id = 1L;
        Bug bug = new Bug(id, 20, 20);
        BugDTO bugDTO = new BugDTO(id, 20, 20);

        when(bugRepositorie.findById(id)).thenReturn(Optional.of(bug));
        when(bugMapper.toDto(bug)).thenReturn(bugDTO);

        BugDTO bugDTOFound = bugService.findById(id);

        assertSame(bugDTOFound, bugDTO);

        verify(bugRepositorie, times(1)).findById(id);
        verify(bugMapper, times(1)).toDto(bug);
    }

    @Test
    public void shouldThrowAnExceptionWhenNotFindBug() {
        Long id = 1L;

        when(bugRepositorie.findById(id)).thenReturn(Optional.empty());

        BugNotFound bugNotFound = assertThrows(BugNotFound.class, () ->
            bugService.findById(id)
        );

        assertSame(bugNotFound.getCode(), MagicErrorCode.MEC005);

        verify(bugRepositorie, times(1)).findById(id);
    }

    @Test
    public void shouldUpdateBug() {
        BugDTO bugDTO = new BugDTO(1L, 15, 20);
        Bug bug = new Bug(1L, 15, 20);

        when(bugMapper.toEntity(bugDTO)).thenReturn(bug);
        when(bugMapper.toDto(bug)).thenReturn(bugDTO);
        when(bugRepositorie.save(bug)).thenReturn(bug);

        BugDTO bugDTOUpdated = bugService.updateBug(bugDTO);

        assertSame(bugDTOUpdated, bugDTO);

        verify(bugMapper, times(1)).toEntity(bugDTO);
        verify(bugMapper, times(1)).toDto(bug);
        verify(bugRepositorie, times(1)).save(bug);
    }

    @Test
    public void shouldRemoveAllBugs() {
        doNothing().when(bugRepositorie).deleteAll();

        bugService.deleteAllBugs();

        verify(bugRepositorie, times(1)).deleteAll();
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
