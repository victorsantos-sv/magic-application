package br.com.magic.application.interfaces;

import br.com.magic.application.api.request.ScoreboardRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class GameControllerTest extends BaseControllerIT {
    private final String baseUrl = "http://localhost:8080/game";

    @Test
    @Sql(value = "/scripts/initial-load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldLoadTheBoardWithSuccess() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
            .get(baseUrl + "/{bugId}/load-board/{playerId}", 1, 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.player.id").value(1))
            .andExpect(jsonPath("$.content.bug.id").value(1))
            .andExpect(jsonPath("$.content.player.cards").isNotEmpty())
            .andExpect(jsonPath("$.content.bug.cards").isNotEmpty());
    }

    @Test
    @SqlGroup({
        @Sql(value = "/scripts/initial-load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/scripts/set-cards.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    public void shouldLoadAllStackCards() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
            .get(baseUrl + "/stack-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.juniorCards").isNotEmpty())
            .andExpect(jsonPath("$.content.bugCards").isNotEmpty());
    }

    @Test
    @SqlGroup({
        @Sql(value = "/scripts/initial-load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/scripts/set-cards.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    public void shouldScoreBug() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
            .put(baseUrl + "/{bugId}/scoreboard-bug/{playerId}", 1, 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.player.id").value(1))
            .andExpect(jsonPath("$.content.bug.id").value(1))
            .andExpect(jsonPath("$.content.cardId").isNotEmpty());
    }

    @Test
    @SqlGroup({
        @Sql(value = "/scripts/initial-load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/scripts/set-cards.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    public void shouldScorePlayer() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
            .put(baseUrl + "/{playerId}/scoreboard-player", 1)
            .content(toJson(new ScoreboardRequest(5L, 1L)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.player.id").value(1))
            .andExpect(jsonPath("$.content.bug.id").value(1))
            .andExpect(jsonPath("$.content.cardId").value(5));
    }

    @Test
    @SqlGroup({
        @Sql(value = "/scripts/initial-load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/scripts/set-less-cards.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    public void shouldEndTurn() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
            .put(baseUrl + "/{playerId}/end-turn/{bugId}", 1, 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.player.id").value(1))
            .andExpect(jsonPath("$.content.bug.id").value(1))
            .andExpect(jsonPath("$.content.player.card").isNotEmpty())
            .andExpect(jsonPath("$.content.bug.card").isNotEmpty());
    }

    private String toJson(ScoreboardRequest scoreboardRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(scoreboardRequest);
    }
}
