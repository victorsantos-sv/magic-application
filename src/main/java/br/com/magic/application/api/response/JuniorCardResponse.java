package br.com.magic.application.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuniorCardResponse {
    private Long id;
    private String title;
    private String description;
    private Integer cost;
    private Integer lifeDamage;
    private Integer passive;
}
