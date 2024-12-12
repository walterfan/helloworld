package com.github.walterfan.hellospringai;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helpdesk")
public class AssistantController {

    @PostMapping
    public AssistantResponse askQuestion(@RequestBody AssistantRequest questionRequest) {
        return new AssistantResponse("This is a response");
    }
}
