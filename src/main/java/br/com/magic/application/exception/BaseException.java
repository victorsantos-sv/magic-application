package br.com.magic.application.exception;

import br.com.magic.application.commons.MagicErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private MagicErrorCode code;
}
