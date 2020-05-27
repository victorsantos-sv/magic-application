package br.com.magic.application.services;

import br.com.magic.application.api.response.ResponseWrapper;
import br.com.magic.application.entity.dto.GameDTO;
import br.com.magic.application.entity.dto.PlayerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface IGameService {

    GameDTO loadBoard(Long id);

    PlayerDTO scoreboardPlayer(Long id);

}
