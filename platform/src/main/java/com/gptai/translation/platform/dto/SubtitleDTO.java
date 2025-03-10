package com.gptai.translation.platform.dto;

public class SubtitleDTO {
    private Integer index;
    private String startTime;
    private String endTime;
    private String text;

    public SubtitleDTO() {
    }

    public SubtitleDTO(Integer index, String startTime, String endTime, String text) {
        this.index = index;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
