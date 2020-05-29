package br.com.magic.application.api.controllers;

import br.com.magic.application.api.IPlayerController;
import br.com.magic.application.api.request.PlayerRequest;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.entity.dto.PlayerDTO;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.exception.response.ErrorResponseWithFields;
import br.com.magic.application.services.IPlayerService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = "*")
public class PlayerController implements IPlayerController {

    private final IPlayerService playerService;
    private final PlayerMapper mapper;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public PlayerController(IPlayerService playerService, PlayerMapper mapper) {
        this.playerService = playerService;
        this.mapper = mapper;
    }

    @Override
    @ApiResponses({
        @ApiResponse(code = 400, message = "Alguns campos são inválidos", response = ErrorResponseWithFields.class)
    })
    public ResponseWrapper<PlayerResponse> create(@RequestBody @Valid PlayerRequest playerRequest) {
        LOG.info("Player registering: { \"nickName\": \"" + playerRequest.getNickName() + "\" }");

        PlayerDTO playerDTO = playerService.create(mapper.toDto(playerRequest));

        return new ResponseWrapper<>(mapper.toResponse(playerDTO));
    }
}
