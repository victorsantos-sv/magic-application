package br.com.magic.application.api;

import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface IScoreboardPlayerController {

    @GetMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<PlayerResponse> scoreboardPlayer (@PathVariable Long id, @PathVariable Long idCard);
}
