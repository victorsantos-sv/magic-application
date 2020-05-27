package br.com.magic.application.service;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.entity.model.Player;
import br.com.magic.application.exception.PlayerNotFound;
import br.com.magic.application.repositories.PlayerRepositorie;
import br.com.magic.application.services.impl.PlayerService;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class PlayerServiceTest {

    private PlayerRepositorie playerRepositorie = Mockito.mock(PlayerRepositorie.class);
    private PlayerMapper playerMapper = Mockito.mock(PlayerMapper.class);
    private PlayerService playerService = new PlayerService(playerRepositorie, playerMapper);

    @Test
    public void shouldSavePlayerWithSuccess() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setNickName("Player");
        Player player = new Player();
        Player playerSaved = new Player(1L, playerDTO.getNickName(), player.getMana(), player.getLife());
        PlayerDTO playerDTOSaved = new PlayerDTO(playerSaved.getId(), playerSaved.getNickName(), playerSaved.getMana(), playerSaved.getLife());
        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);

        Mockito.when(playerMapper.toEntity(playerDTO)).thenReturn(player);
        Mockito.when(playerMapper.toDto(playerSaved)).thenReturn(playerDTOSaved);
        Mockito.when(playerRepositorie.save(player)).thenReturn(playerSaved);

        playerDTOSaved = playerService.create(playerDTO);

        Assert.assertSame(playerDTO.getNickName(), playerDTOSaved.getNickName());
        Assert.assertSame(playerSaved.getId(), playerDTOSaved.getId());

        Mockito.verify(playerMapper, Mockito.times(1)).toEntity(playerDTO);
        Mockito.verify(playerMapper, Mockito.times(1)).toDto(playerSaved);
        Mockito.verify(playerRepositorie, Mockito.times(1)).save(player);
    }

    @Test
    public void shouldThrowAnExceptionWhenNotFindPlayer() {
        Long id = 1L;

        Mockito.when(playerRepositorie.findById(id)).thenReturn(Optional.empty());

        PlayerNotFound playerNotFound = Assert.assertThrows(PlayerNotFound.class, () -> {
            playerService.findById(id);
        });

        Assert.assertSame(playerNotFound.getCode(), MagicErrorCode.MEC001);

        Mockito.verify(playerRepositorie, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldFindPlayerById() {
        Long id = 1L;
        Player player = new Player(id, "player", 20, 20);

        Mockito.when(playerRepositorie.findById(id)).thenReturn(Optional.of(player));
        Mockito.when(playerMapper.toDto(player)).thenReturn(new PlayerDTO(id, "player", 20, 20));

        PlayerDTO playerDTO = playerService.findById(id);

        Assert.assertSame(player.getId(), playerDTO.getId());
        Assert.assertSame(player.getNickName(), playerDTO.getNickName());
        Assert.assertSame(player.getMana(), playerDTO.getMana());
        Assert.assertSame(player.getLife(), playerDTO.getLife());

        Mockito.verify(playerRepositorie, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldUpdatePlayerWithSuccess() {
        PlayerDTO playerDTO = new PlayerDTO(1L, "player", 15, 20);
        Player player = new Player(1L, "player", 15, 20);

        Mockito.when(playerMapper.toEntity(playerDTO)).thenReturn(player);
        Mockito.when(playerMapper.toDto(player)).thenReturn(playerDTO);
        Mockito.when(playerRepositorie.save(player)).thenReturn(player);

        PlayerDTO playerDTOUpdated = playerService.update(playerDTO);

        Assert.assertSame(playerDTO, playerDTOUpdated);

        Mockito.verify(playerMapper, Mockito.times(1)).toDto(player);
        Mockito.verify(playerMapper, Mockito.times(1)).toEntity(playerDTO);
        Mockito.verify(playerRepositorie, Mockito.times(1)).save(player);
    }
}
