package br.com.magic.application.api.controllers;

import br.com.magic.application.api.IGameController;
import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.api.response.StackCardsResponse;
import br.com.magic.application.entity.mapper.GameMapper;
import br.com.magic.application.exception.response.ErrorResponse;
import br.com.magic.application.services.IGameService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@CrossOrigin("*")
public class GameController implements IGameController {

    private IGameService gameService;
    private GameMapper mapper;

    public GameController(IGameService gameService, GameMapper mapper) {
        this.gameService = gameService;
        this.mapper = mapper;
    }

    @Override
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Player não encontrado", response = ErrorResponse.class),
        @ApiResponse(code = 422, message = "Player já possui o máximo de cartas em mãos", response = ErrorResponse.class)
    })
    public ResponseWrapper<GameResponse> loadBoardGame(@PathVariable Long id) {
        return new ResponseWrapper<>(mapper.toResponse(gameService.loadBoard(id)));
    }

    @Override
    public ResponseWrapper<StackCardsResponse> getStackCards() {
        return new ResponseWrapper<>(mapper.toResponse(gameService.getStackCards()));
    }
}
