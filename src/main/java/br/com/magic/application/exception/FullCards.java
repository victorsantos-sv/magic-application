package br.com.magic.application.exception;

import br.com.magic.application.commons.MagicErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullCards extends BaseException {
    private MagicErrorCode code;
    private String player;
}
