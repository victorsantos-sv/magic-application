package br.com.magic.application.api.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BugWithCardResponse {
    private Long id;
    private Integer life;
    private Integer mana;
    private List<BugCardResponse> cards;
}
