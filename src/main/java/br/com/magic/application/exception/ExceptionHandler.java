package br.com.magic.application.exception;

import br.com.magic.application.commons.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandler {

    private ResourceBundle resourceBundle;

    @Autowired
    public ExceptionHandler(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
