package br.com.magic.application.api;

import br.com.magic.application.api.request.ScoreboardRequest;
import br.com.magic.application.api.response.EndTurnResponse;
import br.com.magic.application.api.response.GameResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.api.response.RoundResponse;
import br.com.magic.application.api.response.StackCardsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface IGameController {

    @GetMapping("{bugId}/load-board/{playerId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<GameResponse> loadBoardGame(@PathVariable Long bugId, @PathVariable Long playerId);

    @GetMapping("/stack-cards")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<StackCardsResponse> getStackCards();

    @PutMapping("{bugId}/scoreboard-bug/{playerId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<RoundResponse> scoreboardBug(@PathVariable Long bugId, @PathVariable Long playerId);

    @PutMapping("/{playerId}/scoreboard-player")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<RoundResponse> scoreboardPlayer(@PathVariable Long playerId, @RequestBody ScoreboardRequest scoreboardRequest);

    @PutMapping("/{playerId}/end-turn/{bugId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ResponseWrapper<EndTurnResponse> endTurn(@PathVariable Long playerId, @PathVariable Long bugId);

    @DeleteMapping("{bugId}/logoff/{playerId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<?> logoff(@PathVariable Long bugId, @PathVariable Long playerId);
}
