package br.com.magic.application.exception;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.commons.ResourceBundle;
import br.com.magic.application.exception.response.ErrorResponse;
import br.com.magic.application.exception.response.ErrorResponseWithFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorResponseWithFields handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String code = MagicErrorCode.MEC003.getCode();
        String key = MagicErrorCode.MEC003.getKey();
        String message = resourceBundle.getMessage(key);
        List<Map<String, String>> fields = exception.getBindingResult().getFieldErrors().stream().map(fieldError -> {
            Map<String, String> field = new HashMap<>();
            field.put("field", fieldError.getField());

            return field;
        }).collect(Collectors.toList());

        return new ErrorResponseWithFields(code, message, fields);
    }

}
