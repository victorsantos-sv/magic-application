package br.com.magic.application.exception;

import br.com.magic.application.commons.ResourceBundle;
import br.com.magic.application.exception.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MagicExceptionHandler {

    private ResourceBundle resourceBundle;

    @Autowired
    public MagicExceptionHandler(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @ExceptionHandler(PlayerNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorResponse handlePlayerNotFound(PlayerNotFound exception) {
        String message = resourceBundle.getMessage(exception.getCode().getKey());

        return new ErrorResponse(exception.getCode().getCode(), message);
    }

    @ExceptionHandler(PlayerFullCards.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    ErrorResponse handlePlayerFullCards(PlayerFullCards exception) {
        String message = resourceBundle.getMessage(exception.getCode().getKey());

        return new ErrorResponse(exception.getCode().getCode(), message);
    }



}
