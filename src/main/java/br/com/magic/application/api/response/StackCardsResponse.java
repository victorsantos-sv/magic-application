package br.com.magic.application.api.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackCardsResponse {
    private List<JuniorCardResponse> juniorCards;
    private List<BugCardResponse> bugCards;
}
