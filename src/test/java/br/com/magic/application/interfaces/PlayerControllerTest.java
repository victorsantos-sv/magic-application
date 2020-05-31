package br.com.magic.application.interfaces;

import br.com.magic.application.api.request.PlayerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class PlayerControllerTest extends BaseControllerIT {

    private final String baseUrl = "http://localhost:8080/player";

    @Test
    public void shouldRegisterPlayerWithSuccess() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
            .post(baseUrl + "/login")
            .content(toJson(new PlayerRequest("Player")))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content.player.id").isNotEmpty())
            .andExpect(jsonPath("$.content.player.nickName").value("Player"))
            .andExpect(jsonPath("$.content.player.mana").value(20))
            .andExpect(jsonPath("$.content.player.life").value(20))
            .andExpect(jsonPath("$.content.bug.id").isNotEmpty())
            .andExpect(jsonPath("$.content.bug.mana").value(20))
            .andExpect(jsonPath("$.content.bug.life").value(20));

    }

    private String toJson(PlayerRequest playerRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(playerRequest);
    }
}
