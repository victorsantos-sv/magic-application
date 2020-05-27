package br.com.magic.application.api.response;

public class RoundResponse {
    private PlayerResponse player;
    private BugResponse bug;
    private Long bugCardId;

    public RoundResponse() {
    }

    public RoundResponse(PlayerResponse player, BugResponse bug, Long bugCardId) {
        this.player = player;
        this.bug = bug;
        this.bugCardId = bugCardId;
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

    public Long getBugCardId() {
        return bugCardId;
    }

    public void setBugCardId(Long bugCardId) {
        this.bugCardId = bugCardId;
    }
}
