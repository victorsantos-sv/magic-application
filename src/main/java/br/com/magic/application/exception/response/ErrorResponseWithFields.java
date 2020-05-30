package br.com.magic.application.exception.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseWithFields {
    private String code;
    private String message;
    private List<Map<String, String>> fields;
}
