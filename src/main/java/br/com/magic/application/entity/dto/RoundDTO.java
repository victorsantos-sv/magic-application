package br.com.magic.application.entity.dto;

public class RoundDTO {
    private PlayerDTO playerDTO;
    private BugDTO bugDTO;
    private Long bugCardId;

    public RoundDTO() {
    }

    public RoundDTO(PlayerDTO playerDTO, BugDTO bugDTO, Long bugCardId) {
        this.playerDTO = playerDTO;
        this.bugDTO = bugDTO;
        this.bugCardId = bugCardId;
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

    public Long getBugCardId() {
        return bugCardId;
    }

    public void setBugCardId(Long bugCardId) {
        this.bugCardId = bugCardId;
    }
}
