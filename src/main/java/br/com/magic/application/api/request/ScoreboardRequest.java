package br.com.magic.application.api.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreboardRequest {
    @NotNull
    private Long cardId;
    @NotNull
    private Long bugId;
}
