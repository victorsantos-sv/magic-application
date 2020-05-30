package br.com.magic.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BugCardResponse {
    private Long id;
    private String title;
    private String description;
    private Integer cost;
    private Integer lifeDamage;
    private Integer manaDamage;
}
