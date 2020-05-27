package br.com.magic.application.api;

import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.api.response.RoundResponse;
import br.com.magic.application.api.response.StackCardsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface IGameController {

    @GetMapping("/load-board/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<GameResponse> loadBoardGame(@PathVariable Long id);

    @GetMapping("/stack-cards")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<StackCardsResponse> getStackCards();

    @PutMapping("{bugId}/bug-round/{playerId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<RoundResponse> bugRound(@PathVariable Long bugId, @PathVariable Long playerId);

    @PutMapping("/scoreboard-player/{playerId}/{cardId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<RoundResponse> scoreboardPlayer(@PathVariable Long playerId, @PathVariable Long cardId);


}
