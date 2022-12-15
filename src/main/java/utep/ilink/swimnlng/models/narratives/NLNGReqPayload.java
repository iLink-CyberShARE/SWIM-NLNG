package utep.ilink.swimnlng.models.narratives;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utep.ilink.swimnlng.models.swim.SWIMOutput;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

/*
User roles available:
    City Resident
    Educator
    Environmentalist
    Farmer
    Regulator
    Researcher
    Rural Resident
    Student
    Water Manager
*/

public class NLNGReqPayload {
    public String scenarioid;
    public String role;
    @NotNull
    public String language;
    @NotNull
    public SWIMOutput output; // complete structure or just some parts?

    /**
     * Returns object serialized as JSON string
     * 
     * @throws JsonProcessingException
     */
    public String toJSONString() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    
    // Getters and Setters

    @JsonProperty("scenarioid")
    public String getScenarioid() {
        return scenarioid;
    }

    public void setScenarioid(String scenarioid) {
        this.scenarioid = scenarioid;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    @JsonProperty("output")    
    public SWIMOutput getOutput() {
        return output;
    }

    public void setOutput(SWIMOutput output) {
        this.output = output;
    }
    
}
