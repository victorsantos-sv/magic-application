package br.com.magic.application.api.controllers;

import br.com.magic.application.api.IScoreboardPlayerController;
import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.api.response.ScoreboardPlayerResponse;

public class ScoreboardPlayerController implements IScoreboardPlayerController {


    @Override
    public ResponseWrapper<ScoreboardPlayerResponse> scoreboardPlayer(Long id, Long idCard) {
        return null;
    }
}
