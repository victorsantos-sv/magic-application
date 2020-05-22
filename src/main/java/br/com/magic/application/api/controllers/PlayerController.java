package br.com.magic.application.api.controllers;

import br.com.magic.application.api.request.PlayerRequest;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.services.IPlayerService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final IPlayerService playerService;
    private final PlayerMapper mapper;

    @Autowired
    public PlayerController(IPlayerService playerService, PlayerMapper mapper) {
        this.playerService = playerService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlayerResponse> create(@RequestBody @Valid PlayerRequest playerRequest) {
        PlayerDTO playerDTO = mapper.toDto(playerRequest);

        playerDTO = playerService.create(playerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(playerDTO));
    }
}
