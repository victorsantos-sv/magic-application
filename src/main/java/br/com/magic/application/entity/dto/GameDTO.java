package br.com.magic.application.entity.dto;

public class GameDTO {
    private PlayerWithCardsDTO playerWithCardsDTO;
    private BugWithCardsDTO bugWithCardsDTO;

    public GameDTO() {
    }

    public GameDTO(PlayerWithCardsDTO playerWithCardsDTO, BugWithCardsDTO bugWithCardsDTO) {
        this.playerWithCardsDTO = playerWithCardsDTO;
        this.bugWithCardsDTO = bugWithCardsDTO;
    }

    public PlayerWithCardsDTO getPlayerWithCardsDTO() {
        return playerWithCardsDTO;
    }

    public void setPlayerWithCardsDTO(PlayerWithCardsDTO playerWithCardsDTO) {
        this.playerWithCardsDTO = playerWithCardsDTO;
    }

    public BugWithCardsDTO getBugWithCardsDTO() {
        return bugWithCardsDTO;
    }

    public void setBugWithCardsDTO(BugWithCardsDTO bugWithCardsDTO) {
        this.bugWithCardsDTO = bugWithCardsDTO;
    }
}
