package com.gptai.translation.platform.service;

import com.gptai.translation.common.config.properties.SystemProperties;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class OpenAIService implements IAIService {

    private final OpenAIClient client;

    public OpenAIService(SystemProperties systemProperties) {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(systemProperties.getAi().getApiKey())
                .maxRetries(3)
                .timeout(Duration.ofSeconds(30))
                .build();
    }

    public String translateSubtitle(String context, String subtitleText, String targetLanguage) {
        var prompt = "";
        var params = ChatCompletionCreateParams.builder()
                .addSystemMessage("你是一个专业的字幕翻译 AI。")
                .addUserMessage(prompt)
                // 控制生成的随机性
                .temperature(0.7)
                .maxCompletionTokens(2000)
                .model(ChatModel.GPT_3_5_TURBO)
                .build();
        var chatCompletion = client.chat().completions().create(params);
        return null;
    }
}
