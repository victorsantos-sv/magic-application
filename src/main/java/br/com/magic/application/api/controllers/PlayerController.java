package br.com.magic.application.api.controllers;

import br.com.magic.application.api.IPlayerController;
import br.com.magic.application.api.request.PlayerRequest;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.services.IPlayerService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
public class PlayerController implements IPlayerController {

    private final IPlayerService playerService;
    private final PlayerMapper mapper;

    @Autowired
    public PlayerController(IPlayerService playerService, PlayerMapper mapper) {
        this.playerService = playerService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<ResponseWrapper<PlayerResponse>> create(@RequestBody @Valid PlayerRequest playerRequest) {
        PlayerDTO playerDTO = playerService.create(mapper.toDto(playerRequest));

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(mapper.toResponse(playerDTO)));
    }
}
