package dto.claude;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaudeRequest {

    private String model;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private List<Message> messages;

    public <T> ClaudeRequest(String model, Integer maxTokens, List<T> ts) {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;

        public Message(String user, String prompt) {
        }
    }
}