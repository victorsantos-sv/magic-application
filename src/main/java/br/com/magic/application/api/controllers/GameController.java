package br.com.magic.application.api.controllers;

import br.com.magic.application.api.IGameController;
import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.entity.mapper.GameMapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.services.IGameService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController implements IGameController {

    private IGameService gameService;
    private GameMapper mapper;
    private PlayerMapper playerMapper;

    public GameController(IGameService gameService, GameMapper mapper) {
        this.gameService = gameService;
        this.mapper = mapper;
    }

    @Override
    public ResponseWrapper<GameResponse> loadBoardGame(@PathVariable Long id) {
        return new ResponseWrapper<>(mapper.toResponse(gameService.loadBoard(id)));
    }

    @Override
    public ResponseWrapper<PlayerResponse> scoreboardPlayer(@PathVariable Long id) {
        return new ResponseWrapper<>(playerMapper.toResponse(gameService.scoreboardPlayer(id)));
    }

}
