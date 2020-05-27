package br.com.magic.application.api;

import br.com.magic.application.api.controllers.ScoreboardPlayerController;
import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.api.response.ScoreboardPlayerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface IScoreboardPlayerController {

    @GetMapping("/scoreboard-player/{id}/{idCard}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<ScoreboardPlayerResponse> scoreboardPlayer (@PathVariable Long id, @PathVariable Long idCard);
}
