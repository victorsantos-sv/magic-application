package br.com.magic.application.api.response;

public class GameResponse {
    private PlayerWithCardResponse player;
    private BugWithCardResponse bug;

    public GameResponse() {
    }

    public GameResponse(PlayerWithCardResponse player, BugWithCardResponse bug) {
        this.player = player;
        this.bug = bug;
    }

    public PlayerWithCardResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerWithCardResponse player) {
        this.player = player;
    }

    public BugWithCardResponse getBug() {
        return bug;
    }

    public void setBug(BugWithCardResponse bug) {
        this.bug = bug;
    }
}
