package com.gptai.translation.platform.controller;

import com.gptai.translation.platform.helper.PromptHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("prompt")
    public String prompt() {
        return PromptHelper.generatePrompt("描述", "字幕", "Japanese");
    }

}
