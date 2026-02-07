package dto.claude;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

public class ClaudeResponse {

    private String id;
    private String type;
    private String role;

    @JsonProperty("content")
    private List<Content> content;

    private String model;

    @JsonProperty("stop_reason")
    private String stopReason;

    @JsonProperty("stop_sequence")
    private String stopSequence;

    private Usage usage;

    // Constructors
    public ClaudeResponse() {
    }

    public ClaudeResponse(String id, String type, String role, List<Content> content,
                          String model, String stopReason, String stopSequence, Usage usage) {
        this.id = id;
        this.type = type;
        this.role = role;
        this.content = content;
        this.model = model;
        this.stopReason = stopReason;
        this.stopSequence = stopSequence;
        this.usage = usage;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    public String getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(String stopSequence) {
        this.stopSequence = stopSequence;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    // Inner class: Content
    public static class Content {
        private String type;

        @JsonProperty("text")
        private String text;

        public Content() {
        }

        public Content(String type, String text) {
            this.type = type;
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    // Inner class: Usage
    public static class Usage {
        @JsonProperty("input_tokens")
        private Integer inputTokens;

        @JsonProperty("output_tokens")
        private Integer outputTokens;

        public Usage() {
        }

        public Usage(Integer inputTokens, Integer outputTokens) {
            this.inputTokens = inputTokens;
            this.outputTokens = outputTokens;
        }

        public Integer getInputTokens() {
            return inputTokens;
        }

        public void setInputTokens(Integer inputTokens) {
            this.inputTokens = inputTokens;
        }

        public Integer getOutputTokens() {
            return outputTokens;
        }

        public void setOutputTokens(Integer outputTokens) {
            this.outputTokens = outputTokens;
        }
    }
}