package com.github.walterfan.hellospringai;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class QuestionRequest {
    @JsonProperty("prompt_message")
    String promptMessage;

    @JsonProperty("history_id")
    String historyId;
}
