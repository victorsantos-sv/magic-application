package br.com.magic.application.api.response;

public class RoundResponse {
    private PlayerResponse player;
    private BugResponse bug;

    public RoundResponse() {
    }

    public RoundResponse(PlayerResponse player, BugResponse bug) {
        this.player = player;
        this.bug = bug;
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
}
