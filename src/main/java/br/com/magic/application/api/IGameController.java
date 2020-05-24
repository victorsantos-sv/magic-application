package br.com.magic.application.api;

import br.com.magic.application.api.response.GameResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface IGameController {

    @GetMapping("/load-board/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<GameResponse> loadBoardGame(@PathVariable Long id);
}
