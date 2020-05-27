package br.com.magic.application.api;

import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface IGameController {

    @GetMapping("/load-board/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<GameResponse> loadBoardGame(@PathVariable Long id);

    @PutMapping("/scoreboard-player/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<PlayerResponse> scoreboardPlayer(@PathVariable Long id);


}
