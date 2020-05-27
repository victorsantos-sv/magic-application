package br.com.magic.application.api.controllers;

import br.com.magic.application.api.IGameController;
import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.api.response.RoundResponse;
import br.com.magic.application.api.response.StackCardsResponse;
import br.com.magic.application.entity.mapper.GameMapper;
import br.com.magic.application.entity.mapper.RoundMapper;
import br.com.magic.application.exception.BaseNotFoundException;
import br.com.magic.application.exception.InsufficientMana;
import br.com.magic.application.exception.response.ErrorResponse;
import br.com.magic.application.services.IGameService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@CrossOrigin("*")
public class GameController implements IGameController {

    private final IGameService gameService;
    private final GameMapper gameMapper;
    private final RoundMapper roundMapper;

    public GameController(IGameService gameService, GameMapper gameMapper, RoundMapper roundMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
        this.roundMapper = roundMapper;
    }

    @Override
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Player não encontrado", response = ErrorResponse.class),
        @ApiResponse(code = 422, message = "Player já possui o máximo de cartas em mãos", response = ErrorResponse.class)
    })
    public ResponseWrapper<GameResponse> loadBoardGame(@PathVariable Long id) {
        return new ResponseWrapper<>(gameMapper.toResponse(gameService.loadBoard(id)));
    }

    @Override
    public ResponseWrapper<StackCardsResponse> getStackCards() {
        return new ResponseWrapper<>(gameMapper.toResponse(gameService.getStackCards()));
    }

    @Override
    @ApiResponses({
        @ApiResponse(code = 404, message = "Player ou bug não encontrados", response = ErrorResponse.class),
        @ApiResponse(code = 422, message = "O Bug não possui mana suficiente", response = ErrorResponse.class)
    })
    public ResponseWrapper<RoundResponse> bugRound(@PathVariable Long bugId, @PathVariable Long playerId) {
        return new ResponseWrapper<>(roundMapper.toResponse(gameService.bugTurn(bugId, playerId)));
    }
}
