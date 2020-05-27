package br.com.magic.application.entity.dto;

public class RoundDTO {
    private PlayerDTO playerDTO;
    private BugDTO bugDTO;

    public RoundDTO() {
    }

    public RoundDTO(PlayerDTO playerDTO, BugDTO bugDTO) {
        this.playerDTO = playerDTO;
        this.bugDTO = bugDTO;
    }

    public PlayerDTO getPlayerDTO() {
        return playerDTO;
    }

    public void setPlayerDTO(PlayerDTO playerDTO) {
        this.playerDTO = playerDTO;
    }

    public BugDTO getBugDTO() {
        return bugDTO;
    }

    public void setBugDTO(BugDTO bugDTO) {
        this.bugDTO = bugDTO;
    }
}
