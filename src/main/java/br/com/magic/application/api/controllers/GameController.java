package br.com.magic.application.api.controllers;

import br.com.magic.application.api.IGameController;
import br.com.magic.application.api.request.ScoreboardRequest;
import br.com.magic.application.api.response.EndTurnResponse;
import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.api.response.RoundResponse;
import br.com.magic.application.api.response.StackCardsResponse;
import br.com.magic.application.entity.mapper.GameMapper;
import br.com.magic.application.exception.response.ErrorResponse;
import br.com.magic.application.services.IGameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "*")
public class GameController implements IGameController {

    private final IGameService gameService;
    private final GameMapper gameMapper;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public GameController(IGameService gameService, GameMapper gameMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @Override
    @ApiOperation(value = "Returns the initial cards with de player and the bug")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Player não encontrado", response = ErrorResponse.class),
        @ApiResponse(code = 422, message = "Player já possui o máximo de cartas em mãos", response = ErrorResponse.class)
    })
    public ResponseWrapper<GameResponse> loadBoardGame(@PathVariable Long bugId, @PathVariable Long playerId) {
        return new ResponseWrapper<>(gameMapper.toResponse(gameService.loadBoard(bugId, playerId)));
    }

    @Override
    @ApiOperation(value = "Returns the cards without use, bug cards and player cards")
    public ResponseWrapper<StackCardsResponse> getStackCards() {
        return new ResponseWrapper<>(gameMapper.toResponse(gameService.getStackCards()));
    }

    @Override
    @ApiOperation(value = "Bug turn")
    @ApiResponses({
        @ApiResponse(code = 404, message = "Player ou bug não encontrados", response = ErrorResponse.class),
        @ApiResponse(code = 422, message = "O Bug não possui mana suficiente", response = ErrorResponse.class)
    })
    public ResponseWrapper<RoundResponse> scoreboardBug(@PathVariable Long bugId, @PathVariable Long playerId) {
        return new ResponseWrapper<>(gameMapper.toResponse(gameService.scoreboardBug(bugId, playerId)));
    }

    @Override
    @ApiOperation(value = "Player turn")
    @ApiResponses({
        @ApiResponse(code = 404, message = "Player ou carta não encontrados", response = ErrorResponse.class),
        @ApiResponse(code = 422, message = "O Player não possui mana suficiente", response = ErrorResponse.class)
    })
    public ResponseWrapper<RoundResponse> scoreboardPlayer(@PathVariable Long playerId, @RequestBody ScoreboardRequest scoreboardRequest) {
        Long cardId = scoreboardRequest.getCardId();
        Long bugId = scoreboardRequest.getBugId();

        return new ResponseWrapper<>(gameMapper.toResponse(gameService.scoreboardPlayer(playerId, cardId, bugId)));
    }

    @Override
    @ApiOperation(value = "Ends the turn and give 2 points of mana to each player and bug")
    @ApiResponses({
        @ApiResponse(code = 404, message = "Player ou bug não encontrados", response = ErrorResponse.class),
        @ApiResponse(code = 422, message = "Player ou bug já possuem o máximo de cartas em mãos", response = ErrorResponse.class)
    })
    public ResponseWrapper<EndTurnResponse> endTurn(@PathVariable Long playerId, @PathVariable Long bugId) {
        return new ResponseWrapper<>(gameMapper.toResponse(gameService.endTurn(playerId, bugId)));
    }

    @Override
    @ApiOperation(value = "Ends the game and delete the player, bug and clear the cards")
    public ResponseEntity<?> logoff(@PathVariable Long bugId, @PathVariable Long playerId) {
        LOG.info("PlayerId for logoff: [{" + playerId + "}]");
        gameService.logoff(bugId, playerId);

        return ResponseEntity.noContent().build();
    }
}
