package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.LoginDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.PlayerNotFound;
import br.com.magic.application.repositories.PlayerRepositorie;
import br.com.magic.application.services.IBugService;
import br.com.magic.application.services.impl.PlayerService;
import java.util.Optional;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerServiceTest {

    private final PlayerRepositorie playerRepositorie = mock(PlayerRepositorie.class);
    private final PlayerMapper playerMapper = mock(PlayerMapper.class);
    private final IBugService bugService = mock(IBugService.class);
    private final PlayerService playerService = new PlayerService(playerRepositorie, playerMapper, bugService);

    @Test
    public void shouldSavePlayerWithSuccess() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setNickName("Player");
        Player player = new Player();
        Player playerSaved = new Player(1L, playerDTO.getNickName(), player.getMana(), player.getLife());
        PlayerDTO playerDTOSaved = new PlayerDTO(playerSaved.getId(), playerSaved.getNickName(), playerSaved.getMana(), playerSaved.getLife());
        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        BugDTO bugDTO = new BugDTO(1L, 20, 20);
        LoginDTO loginDTO = new LoginDTO(playerDTOSaved, bugDTO);

        when(playerMapper.toEntity(playerDTO)).thenReturn(player);
        when(playerMapper.toDto(playerSaved)).thenReturn(playerDTOSaved);
        when(playerMapper.toDto(playerDTOSaved, bugDTO)).thenReturn(loginDTO);
        when(playerRepositorie.save(player)).thenReturn(playerSaved);
        when(bugService.createBug()).thenReturn(bugDTO);

        LoginDTO loginDTOCreated = playerService.create(playerDTO);

        assertSame(loginDTOCreated.getPlayerDTO().getNickName(), playerDTOSaved.getNickName());
        assertSame(loginDTOCreated.getPlayerDTO().getId(), playerDTOSaved.getId());
        assertNotNull(loginDTOCreated.getBugDTO().getId());

        verify(playerMapper, times(1)).toEntity(playerDTO);
        verify(playerMapper, times(1)).toDto(playerDTOSaved, bugDTO);
        verify(playerMapper, times(1)).toDto(playerSaved);
        verify(playerRepositorie, times(1)).save(player);
        verify(bugService, times(1)).createBug();
    }

    @Test
    public void shouldThrowAnExceptionWhenNotFindPlayer() {
        Long id = 1L;

        when(playerRepositorie.findById(id)).thenReturn(Optional.empty());

        PlayerNotFound playerNotFound = assertThrows(PlayerNotFound.class, () -> {
            playerService.findById(id);
        });

        assertSame(playerNotFound.getCode(), MagicErrorCode.MEC001);

        verify(playerRepositorie, times(1)).findById(id);
    }

    @Test
    public void shouldFindPlayerById() {
        Long id = 1L;
        Player player = new Player(id, "player", 20, 20);

        when(playerRepositorie.findById(id)).thenReturn(Optional.of(player));
        when(playerMapper.toDto(player)).thenReturn(new PlayerDTO(id, "player", 20, 20));

        PlayerDTO playerDTO = playerService.findById(id);

        assertSame(player.getId(), playerDTO.getId());
        assertSame(player.getNickName(), playerDTO.getNickName());
        assertSame(player.getMana(), playerDTO.getMana());
        assertSame(player.getLife(), playerDTO.getLife());

        verify(playerRepositorie, times(1)).findById(id);
    }

    @Test
    public void shouldUpdatePlayerWithSuccess() {
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 15, 20);
        Player player = new Player(1L, "player", 15, 20);

        when(playerMapper.toEntity(playerDTO)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerDTO);
        when(playerRepositorie.save(player)).thenReturn(player);

        PlayerDTO playerDTOUpdated = playerService.update(playerDTO);

        assertSame(playerDTO, playerDTOUpdated);

        verify(playerMapper, times(1)).toDto(player);
        verify(playerMapper, times(1)).toEntity(playerDTO);
        verify(playerRepositorie, times(1)).save(player);
    }

    @Test
    public void shouldDeletePlayerById() {
        Long id = 1L;
        Player player = new Player(id, "player", 20, 20);

        when(playerRepositorie.findById(id)).thenReturn(Optional.of(player));

        doNothing().when(playerRepositorie).deleteById(id);

        playerService.deleteById(id);

        verify(playerRepositorie, times(1)).deleteById(id);
    }
}
