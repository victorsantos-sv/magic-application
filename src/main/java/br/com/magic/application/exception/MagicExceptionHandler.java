package br.com.magic.application.exception;

import br.com.magic.application.commons.ResourceBundle;
import br.com.magic.application.exception.response.ErroResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MagicExceptionHandler {

    private ResourceBundle resourceBundle;

    @Autowired
    public MagicExceptionHandler(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @ExceptionHandler(PlayerNotFound.class)
    ResponseEntity<ErroResponse> handlePlayerNotFound(PlayerNotFound exception) {
        String message = resourceBundle.getMessage(exception.getCode().getKey());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponse(exception.getCode().getCode(), message));
    }

}
