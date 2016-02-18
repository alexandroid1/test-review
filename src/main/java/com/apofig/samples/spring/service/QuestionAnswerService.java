package com.apofig.samples.spring.service;

import com.apofig.samples.spring.model.QuestionAnswer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Component(value = "questionAnswer")
public class QuestionAnswerService {

    private final Settings settings;
    private static final Author NO_NAME = new Author("NoName");
    private Map<Integer, QuestionAnswer> qam = new LinkedHashMap<>();;
    private List<String> questions = new LinkedList<>();
    private List<Author> authors = new LinkedList<>();
    private List<List<String>> qas = new LinkedList<>();

    public static void main(String[] args) {
        try {
            Reader in = new ReplaceLRReader(new InputStreamReader(new FileInputStream("data/report-result.csv"), Charset.forName("UTF8")));
            Iterable<CSVRecord> records = CSVFormat.newFormat('§').parse(in);
            boolean firstLine = true;
            int y = 0;
            for (CSVRecord record : records) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                int count = 0;
                for (int x = 0; x < record.size(); x++) {
                    String line = record.get(x);
                    String[] split = line.split(" => ");
                    if (x == 0) {
                        System.out.print(line + "=");
                    } else {
                        count += Integer.valueOf(split[0]);
                    }
                    if (x == 2) {
                        System.out.print(split[1]);
                    }
                }
                System.out.println("=" + count);
                y++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public QuestionAnswerService(String fileName, String charset,
                                 char separator, String nameColumn,
                                 String nextLineSeparator, String nextLineReplacement,
                                 int questionsCount) {
        this.settings = new Settings(charset, separator, nameColumn, nextLineSeparator, nextLineReplacement, questionsCount);

        try {
            Reader in = new ReplaceLRReader(new InputStreamReader(new FileInputStream(fileName), Charset.forName(charset)));
            Iterable<CSVRecord> records = CSVFormat.newFormat(separator).parse(in);
            boolean firstLine = true;
            int y = 0;
            for (CSVRecord record : records) {
                if (firstLine) {
                    firstLine = false;
                    for (int i = 0; i < record.size(); i++) {
                        questions.add(record.get(i));
                    }
                    if (questions.size() != questionsCount) {
                        throw new RuntimeException("Количество вопросов не совпадает: " + questions.size());
                    }
                    continue;
                }

                List<String> list;
                if (y >= qas.size()) {
                    list = new LinkedList<>();
                    qas.add(list);
                } else {
                    list = qas.get(y);
                }

                if (record.size() != questions.size() || record.size() != questionsCount) {
                    throw new RuntimeException("Количество ответов не совпадает: " + record.size() +
                            " для: " + record.toString());
                }

                for (int x = 0; x < record.size(); x++) {
                    String line = record.get(x);
                    list.add(line.replace(nextLineSeparator, nextLineReplacement));
                    if (questions.get(x).contains(nameColumn)) {
                        authors.add(new Author(line));
                    }
                }
                y++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (authors.size() != qas.size()) {
            throw new RuntimeException("Отличаются размеры: " + authors.size() + " != " + qas.size());
        }

        splitToQuestionAnswers();
    }

    private void splitToQuestionAnswers() {
        for (int y = 0; y < qas.size(); y++) {
            for (int x = 0; x < questions.size(); x++) {
                String question = questions.get(x);
                Author author = authors.get(y);
                String answer = qas.get(y).get(x);

                int id = x*qas.size() + y;

                QuestionAnswer qa = new QuestionAnswer();
                qa.setAuthor(author);
                qa.setId(id);
                qa.setQuestion(question);
                qa.setAnswer(answer);
                qa.setVote(0);

                qam.put(id, qa);
            }
        }
    }

    public QuestionAnswer get(int id) {
        QuestionAnswer result = qam.get(id);
        if (result == null) {
            result = new QuestionAnswer();
            result.setId(id);
            result.setAuthor(NO_NAME);
            result.setQuestion("Question " + id);
            result.setAnswer("Answer " + id);
            result.setVote(0);
            qam.put(id, result);
        }
        return result;
    }

    public void setVote(int id, int vote) {
        QuestionAnswer qa = qam.get(id);
        if (qa == null) {
            throw new RuntimeException("Не могу найти Qa: " + id);
        }
        int oldVote = qa.getVote();
        qa.setVote(vote);
        qa.getAuthor().increaseTotal(vote - oldVote);
    }

    public List<QuestionAnswer> getAll() {
        List<QuestionAnswer> result = new LinkedList<>();
        int id = 0;
        while (get(id).getAuthor() != NO_NAME) {
            result.add(get(id));
            id++;
        }
        return result;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void saveToFile(String fileName) {
        WithFile file = new WithFile(fileName);
        file.clear();

        String sep = String.valueOf(settings.getSeparator());

        file.appendAnd("Total").appendAnd(sep);

        int countQuestions = 0;
        for (String q : questions) {
            file.appendAnd(q);

            countQuestions++;
            if (countQuestions % settings.getQuestionsCount() == 0) {
                file.appendAnd("\r\n");
            } else {
                file.appendAnd(sep);
            }
        }

        int count = 0;
        for (Map.Entry<Integer, QuestionAnswer> entry : qam.entrySet()) {
            QuestionAnswer qa = entry.getValue();
            if (qa.getAuthor() == NO_NAME) continue;

            if (count % settings.getQuestionsCount() == 0) {
                file.appendAnd(String.valueOf(qa.getAuthor().getTotal()));
                file.appendAnd(sep);
            }

            file.appendAnd(qa.getVote() + " => ").appendAnd(qa.getAnswer());

            count++;
            if (count % settings.getQuestionsCount() == 0) {
                file.appendAnd("\r\n");
            } else {
                file.appendAnd(sep);
            }
        }
        file.close();
    }
}
