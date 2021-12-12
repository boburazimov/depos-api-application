package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.VotingOption;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a voting-option, with only the public attributes.
 */
public class VotingEditDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private Long id;

    @NotNull(message = "Voting text must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private String votingText;

    public VotingEditDTO() {}

    public VotingEditDTO(VotingOption votingOption) {
        this.id = votingOption.getId();
        this.votingText = votingOption.getVotingText();
    }

    public VotingEditDTO(Long id, String votingText) {
        this.id = id;
        this.votingText = votingText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVotingText() {
        return votingText;
    }

    public void setVotingText(String votingText) {
        this.votingText = votingText;
    }

    @Override
    public String toString() {
        return "VotingEditDTO{" + "id=" + id + ", votingText='" + votingText + '\'' + '}';
    }
}
