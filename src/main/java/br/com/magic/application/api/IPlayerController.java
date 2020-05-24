package br.com.magic.application.api;

import br.com.magic.application.api.request.PlayerRequest;
import br.com.magic.application.api.response.PlayerResponse;
import br.com.magic.application.api.response.ResponseWrapper;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface IPlayerController {

    @PostMapping(path = "/login")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ResponseWrapper<PlayerResponse>> create(@RequestBody @Valid PlayerRequest playerRequest);
}