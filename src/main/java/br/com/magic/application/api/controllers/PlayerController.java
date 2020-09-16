package br.com.magic.application.api.controllers;

import br.com.magic.application.api.IPlayerController;
import br.com.magic.application.api.request.PlayerRequest;
import br.com.magic.application.api.response.LoginResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.exception.response.ErrorResponseWithFields;
import br.com.magic.application.services.IPlayerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/player")
public class PlayerController implements IPlayerController {

    private final IPlayerService playerService;
    private final PlayerMapper mapper;
    private final Logger log = LogManager.getLogger(this.getClass());

    public PlayerController(IPlayerService playerService, PlayerMapper mapper) {
        this.playerService = playerService;
        this.mapper = mapper;
    }

    @Override
    @ApiOperation(value = "Creates a new player")
    @ApiResponses({
        @ApiResponse(code = 400, message = "Alguns campos são inválidos", response = ErrorResponseWithFields.class)
    })
    public ResponseWrapper<LoginResponse> create(@RequestBody @Valid PlayerRequest playerRequest) {
        log.info("Player registering: " + playerRequest.toString());

        return new ResponseWrapper<>(mapper.toResponse(playerService.create(mapper.toDto(playerRequest))));
    }
}
