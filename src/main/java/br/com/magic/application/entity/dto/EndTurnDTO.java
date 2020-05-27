package br.com.magic.application.entity.dto;

public class EndTurnDTO {
    private PlayerDTO playerDTO;
    private JuniorCardDTO juniorCardDTO;
    private BugDTO bugDTO;
    private BugCardDTO bugCardDTO;

    public EndTurnDTO() {
    }

    public EndTurnDTO(PlayerDTO playerDTO, JuniorCardDTO juniorCardDTO, BugDTO bugDTO, BugCardDTO bugCardDTO) {
        this.playerDTO = playerDTO;
        this.juniorCardDTO = juniorCardDTO;
        this.bugDTO = bugDTO;
        this.bugCardDTO = bugCardDTO;
    }

    public PlayerDTO getPlayerDTO() {
        return playerDTO;
    }

    public void setPlayerDTO(PlayerDTO playerDTO) {
        this.playerDTO = playerDTO;
    }

    public JuniorCardDTO getJuniorCardDTO() {
        return juniorCardDTO;
    }

    public void setJuniorCardDTO(JuniorCardDTO juniorCardDTO) {
        this.juniorCardDTO = juniorCardDTO;
    }

    public BugDTO getBugDTO() {
        return bugDTO;
    }

    public void setBugDTO(BugDTO bugDTO) {
        this.bugDTO = bugDTO;
    }

    public BugCardDTO getBugCardDTO() {
        return bugCardDTO;
    }

    public void setBugCardDTO(BugCardDTO bugCardDTO) {
        this.bugCardDTO = bugCardDTO;
    }
}
