package com.apofig.samples.spring.service;

/**
 * Created by indigo on 15.05.2015.
 */
public class Settings {
    private final String charset;
    private final char separator;
    private final String nameColumn;
    private final String nextLineSeparator;
    private final String nextLineReplacement;
    private final int questionsCount;

    public Settings(String charset, char separator, String nameColumn, String nextLineSeparator, String nextLineReplacement, int questionsCount) {
        this.charset = charset;
        this.separator = separator;
        this.nameColumn = nameColumn;
        this.nextLineSeparator = nextLineSeparator;
        this.nextLineReplacement = nextLineReplacement;
        this.questionsCount = questionsCount;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public String getNextLineReplacement() {
        return nextLineReplacement;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public char getSeparator() {
        return separator;
    }

    public String getCharset() {
        return charset;
    }

    public String getNextLineSeparator() {
        return nextLineSeparator;
    }
}
