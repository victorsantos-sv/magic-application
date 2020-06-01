package br.com.magic.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerWithCardResponse {
    private Long id;
    private String nickName;
    private Integer life;
    private Integer mana;
    private JuniorCardResponse card;
}
