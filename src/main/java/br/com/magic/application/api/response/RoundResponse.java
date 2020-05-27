package br.com.magic.application.api.response;

public class RoundResponse {
    private PlayerResponse player;
    private BugResponse bug;
    private Long cardId;

    public RoundResponse() {
    }

    public RoundResponse(PlayerResponse player, BugResponse bug, Long cardId) {
        this.player = player;
        this.bug = bug;
        this.cardId = cardId;
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerResponse player) {
        this.player = player;
    }

    public BugResponse getBug() {
        return bug;
    }

    public void setBug(BugResponse bug) {
        this.bug = bug;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
