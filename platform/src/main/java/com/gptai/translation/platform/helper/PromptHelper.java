package com.gptai.translation.platform.helper;

public class PromptHelper {

    public static String generatePrompt(String context, String subtitleText, String targetLanguage) {
        String userFeedback = "用户反馈建议： （无用户反馈）" +
                "{" +
                "\"原文\": \"你好，欢迎来到未来世界！\"," +
                "\"GPT翻译\": \"こんにちは、未来の世界へようこそ！\"," +
                "\"用户反馈\": \"ようこそ、未来の世界へ！\"," +
                "\"修改原因\": \"更符合日语的表达习惯，使语言更自然\"" +
                "}";
        return """
                你是一个专业的抖音短视屏爽剧的字幕翻译AI。
                背景信息：
                %s
                请将以下简体中文字幕翻译成%s，确保符合情境，避免直译，流畅自然。
                %s
                字幕内容:
                %s
                """.formatted(context, targetLanguage, userFeedback, subtitleText);
    }
}
