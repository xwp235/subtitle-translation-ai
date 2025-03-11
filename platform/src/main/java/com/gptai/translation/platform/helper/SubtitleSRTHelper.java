package com.gptai.translation.platform.helper;

import com.gptai.translation.platform.dto.SubtitleDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SubtitleSRTHelper {

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

    public static void writeSRT(List<SubtitleDTO> subtitles, String outputPath) throws IOException {
        var srtContent = new StringBuilder();
        for (var subtitle : subtitles) {
            srtContent.append(subtitle.getIndex()).append("\n")
                    .append(subtitle.getStartTime()).append(" --> ").append(subtitle.getEndTime()).append("\n")
                    .append(subtitle.getText()).append("\n\n");
        }
        Files.writeString(Paths.get(outputPath), srtContent.toString());
    }

}
