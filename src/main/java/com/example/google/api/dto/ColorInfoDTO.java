package com.example.google.api.dto;

public class ColorInfoDTO {
    private int red;
    private int green;
    private int blue;
    private float score;

    public ColorInfoDTO(float red, float green, float blue, float score) {
        this.red = (int) red;
        this.green = (int) green;
        this.blue = (int) blue;
        this.score = score;
    }

// 게터 및 세터 메소드
// ...

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
