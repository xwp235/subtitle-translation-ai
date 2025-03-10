package com.gptai.translation.platform.parser;

import com.gptai.translation.platform.dto.SubtitleDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SRTParser {

    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\s*\\n(\\d{2}:\\d{2}:\\d{2},\\d{3}) --> (\\d{2}:\\d{2}:\\d{2},\\d{3})\\s*\\n((?:.+\\n?)+)");

    public static List<SubtitleDTO> parseSRT(String filePath) throws IOException {
        var subtitles = new ArrayList<SubtitleDTO>();
        var lines = Files.readAllLines(Paths.get(filePath));
        var srtContent = new StringBuilder();
        for (var line : lines) {
            srtContent.append(line).append("\n");
        }
        var matcher = PATTERN.matcher(srtContent.toString());
        while (matcher.find()) {
            var index = Integer.parseInt(matcher.group(1));
            var startTime = matcher.group(2);
            var endTime = matcher.group(3);
            var text = matcher.group(4).trim().replace("\n", " ");
            subtitles.add(new SubtitleDTO(index, startTime, endTime, text));
        }
        return subtitles;
    }

}
