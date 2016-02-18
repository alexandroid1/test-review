package com.apofig.testreview;

import com.apofig.samples.spring.service.QuestionAnswerService;
import com.apofig.samples.spring.service.WithFile;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by indigo on 15.05.2015
 * how to use it?
 */
public class QuestionAnswerServiceTest {

    String charset = "UTF8";
    char separator = '|';
    String nameColumn = "name";
    String nextLineSeparator = "^";
    String nextLineReplacement = "<br>";
    String fileName = "target/report-" + Math.abs(new Random().nextInt()) + ".csv";
    WithFile file = new WithFile(fileName);

    @Test
    public void shouldOneUser() {
        int questionsCount = 6;
        file.append("#|date|name|question1|question2|question3\r\n");
        file.append("1|2015-01-01|Саня|Ответ1|Ответ2|Ответ3\r\n");

        QuestionAnswerService service = new QuestionAnswerService(fileName, charset,
                separator, nameColumn,
                nextLineSeparator, nextLineReplacement, questionsCount);

        assertEquals("[#, date, name, question1, question2, question3]",
                service.getQuestions().toString());

        assertEquals("[{name:'Саня', total:0}]",
                service.getAuthors().toString());

        assertEquals("[{id:0, question:'#', answer:'1', vote:0, author:{name:'Саня', total:0}, nextId:1, prevId:-1}, " +
                        "{id:1, question:'date', answer:'2015-01-01', vote:0, author:{name:'Саня', total:0}, nextId:2, prevId:0}, " +
                        "{id:2, question:'name', answer:'Саня', vote:0, author:{name:'Саня', total:0}, nextId:3, prevId:1}, " +
                        "{id:3, question:'question1', answer:'Ответ1', vote:0, author:{name:'Саня', total:0}, nextId:4, prevId:2}, " +
                        "{id:4, question:'question2', answer:'Ответ2', vote:0, author:{name:'Саня', total:0}, nextId:5, prevId:3}, " +
                        "{id:5, question:'question3', answer:'Ответ3', vote:0, author:{name:'Саня', total:0}, nextId:6, prevId:4}]",
                service.getAll().toString());
    }

    @Test
    public void shouldTwoUsers() {
        int questionsCount = 3;
        file.append("name|question1|question2\r\n");
        file.append("Саня|Ответ11|Ответ12\r\n");
        file.append("Баня|Ответ21|Ответ22\r\n");

        QuestionAnswerService service = new QuestionAnswerService(fileName, charset,
                separator, nameColumn,
                nextLineSeparator, nextLineReplacement, questionsCount);

        assertEquals("[name, question1, question2]",
                service.getQuestions().toString());

        assertEquals("[{name:'Саня', total:0}, " +
                        "{name:'Баня', total:0}]",
                service.getAuthors().toString());

        assertEquals("[{id:0, question:'name', answer:'Саня', vote:0, author:{name:'Саня', total:0}, nextId:1, prevId:-1}, " +
                        "{id:1, question:'name', answer:'Баня', vote:0, author:{name:'Баня', total:0}, nextId:2, prevId:0}, " +

                        "{id:2, question:'question1', answer:'Ответ11', vote:0, author:{name:'Саня', total:0}, nextId:3, prevId:1}, " +
                        "{id:3, question:'question1', answer:'Ответ21', vote:0, author:{name:'Баня', total:0}, nextId:4, prevId:2}, " +

                        "{id:4, question:'question2', answer:'Ответ12', vote:0, author:{name:'Саня', total:0}, nextId:5, prevId:3}, " +
                        "{id:5, question:'question2', answer:'Ответ22', vote:0, author:{name:'Баня', total:0}, nextId:6, prevId:4}]",
                service.getAll().toString());
    }

    @Test
    public void shouldReplaceNextLine() {
        int questionsCount = 4;
        file.append("name|question1|question2|question3\r\n");
        file.append("Саня|Отв\nет1|Отв^ет2|Отв\n^ет3\r\n");

        QuestionAnswerService service = new QuestionAnswerService(fileName, charset,
                separator, nameColumn,
                nextLineSeparator, nextLineReplacement, questionsCount);

        assertEquals("[name, question1, question2, question3]",
                service.getQuestions().toString());

        assertEquals("[{name:'Саня', total:0}]",
                service.getAuthors().toString());

        String br = "<br>";
        assertEquals("[{id:0, question:'name', answer:'Саня', vote:0, author:{name:'Саня', total:0}, nextId:1, prevId:-1}, " +
                        "{id:1, question:'question1', answer:'Отв" + br + "ет1', vote:0, author:{name:'Саня', total:0}, nextId:2, prevId:0}, " +
                        "{id:2, question:'question2', answer:'Отв" + br + "ет2', vote:0, author:{name:'Саня', total:0}, nextId:3, prevId:1}, " +
                        "{id:3, question:'question3', answer:'Отв" + br +  br + "ет3', vote:0, author:{name:'Саня', total:0}, nextId:4, prevId:2}]",
                service.getAll().toString());
    }

    @Test
    public void shouldSkipEmpty() {
        int questionsCount = 3;
        file.append("name|question1|question2\r\n");
        file.append("Саня||Ответ12\r\n");

        QuestionAnswerService service = new QuestionAnswerService(fileName, charset,
                separator, nameColumn,
                nextLineSeparator, nextLineReplacement, questionsCount);

        assertEquals("[name, question1, question2]",
                service.getQuestions().toString());

        assertEquals("[{name:'Саня', total:0}]",
                service.getAuthors().toString());

        String empty = "";
        assertEquals("[{id:0, question:'name', answer:'Саня', vote:0, author:{name:'Саня', total:0}, nextId:1, prevId:-1}, " +
                        "{id:1, question:'question1', answer:'" + empty +  "', vote:0, author:{name:'Саня', total:0}, nextId:2, prevId:0}, " +
                        "{id:2, question:'question2', answer:'Ответ12', vote:0, author:{name:'Саня', total:0}, nextId:3, prevId:1}]",
                service.getAll().toString());
    }

    @Test
    public void shouldSmoke() {
        int questionsCount = 7;
        file.append("#|date|name|question1|question2|question3|question4\r\n");
        file.append("1|2015-01-01|Саня|Ответ11|Ответ12|Ответ13|Ответ14\r\n");
        file.append("2|2015-01-02|Баня|Отв\nет^21|Отв\nет^22|Отв\nет^23|Ответ24\r\n");
        file.append("3|2015-01-03|Каня|Ответ31|Ответ32|Ответ33|Ответ34\r\n");
        file.append("5|2015-01-05|Ваня|Ответ51||Ответ53|Ответ54\r\n");
        file.append("7||Даня||||\r\n");

        QuestionAnswerService service = new QuestionAnswerService(fileName, charset,
                separator, nameColumn,
                nextLineSeparator, nextLineReplacement, questionsCount);

        assertEquals("[#, date, name, question1, question2, question3, question4]",
                service.getQuestions().toString());

        assertEquals("[{name:'Саня', total:0}, " +
                        "{name:'Баня', total:0}, " +
                        "{name:'Каня', total:0}, " +
                        "{name:'Ваня', total:0}, " +
                        "{name:'Даня', total:0}]",
                service.getAuthors().toString());

        assertEqualsM("[{id:0, question:'#', answer:'1', vote:0, author:{name:'Саня', total:0}, nextId:1, prevId:-1}, " +
                        "{id:1, question:'#', answer:'2', vote:0, author:{name:'Баня', total:0}, nextId:2, prevId:0}, " +
                        "{id:2, question:'#', answer:'3', vote:0, author:{name:'Каня', total:0}, nextId:3, prevId:1}, " +
                        "{id:3, question:'#', answer:'5', vote:0, author:{name:'Ваня', total:0}, nextId:4, prevId:2}, " +
                        "{id:4, question:'#', answer:'7', vote:0, author:{name:'Даня', total:0}, nextId:5, prevId:3}, " +

                        "{id:5, question:'date', answer:'2015-01-01', vote:0, author:{name:'Саня', total:0}, nextId:6, prevId:4}, " +
                        "{id:6, question:'date', answer:'2015-01-02', vote:0, author:{name:'Баня', total:0}, nextId:7, prevId:5}, " +
                        "{id:7, question:'date', answer:'2015-01-03', vote:0, author:{name:'Каня', total:0}, nextId:8, prevId:6}, " +
                        "{id:8, question:'date', answer:'2015-01-05', vote:0, author:{name:'Ваня', total:0}, nextId:9, prevId:7}, " +
                        "{id:9, question:'date', answer:'', vote:0, author:{name:'Даня', total:0}, nextId:10, prevId:8}, " +

                        "{id:10, question:'name', answer:'Саня', vote:0, author:{name:'Саня', total:0}, nextId:11, prevId:9}, " +
                        "{id:11, question:'name', answer:'Баня', vote:0, author:{name:'Баня', total:0}, nextId:12, prevId:10}, " +
                        "{id:12, question:'name', answer:'Каня', vote:0, author:{name:'Каня', total:0}, nextId:13, prevId:11}, " +
                        "{id:13, question:'name', answer:'Ваня', vote:0, author:{name:'Ваня', total:0}, nextId:14, prevId:12}, " +
                        "{id:14, question:'name', answer:'Даня', vote:0, author:{name:'Даня', total:0}, nextId:15, prevId:13}, " +

                        "{id:15, question:'question1', answer:'Ответ11', vote:0, author:{name:'Саня', total:0}, nextId:16, prevId:14}, " +
                        "{id:16, question:'question1', answer:'Отв<br>ет<br>21', vote:0, author:{name:'Баня', total:0}, nextId:17, prevId:15}, " +
                        "{id:17, question:'question1', answer:'Ответ31', vote:0, author:{name:'Каня', total:0}, nextId:18, prevId:16}, " +
                        "{id:18, question:'question1', answer:'Ответ51', vote:0, author:{name:'Ваня', total:0}, nextId:19, prevId:17}, " +
                        "{id:19, question:'question1', answer:'', vote:0, author:{name:'Даня', total:0}, nextId:20, prevId:18}, " +

                        "{id:20, question:'question2', answer:'Ответ12', vote:0, author:{name:'Саня', total:0}, nextId:21, prevId:19}, " +
                        "{id:21, question:'question2', answer:'Отв<br>ет<br>22', vote:0, author:{name:'Баня', total:0}, nextId:22, prevId:20}, " +
                        "{id:22, question:'question2', answer:'Ответ32', vote:0, author:{name:'Каня', total:0}, nextId:23, prevId:21}, " +
                        "{id:23, question:'question2', answer:'', vote:0, author:{name:'Ваня', total:0}, nextId:24, prevId:22}, " +
                        "{id:24, question:'question2', answer:'', vote:0, author:{name:'Даня', total:0}, nextId:25, prevId:23}, " +

                        "{id:25, question:'question3', answer:'Ответ13', vote:0, author:{name:'Саня', total:0}, nextId:26, prevId:24}, " +
                        "{id:26, question:'question3', answer:'Отв<br>ет<br>23', vote:0, author:{name:'Баня', total:0}, nextId:27, prevId:25}, " +
                        "{id:27, question:'question3', answer:'Ответ33', vote:0, author:{name:'Каня', total:0}, nextId:28, prevId:26}, " +
                        "{id:28, question:'question3', answer:'Ответ53', vote:0, author:{name:'Ваня', total:0}, nextId:29, prevId:27}, " +
                        "{id:29, question:'question3', answer:'', vote:0, author:{name:'Даня', total:0}, nextId:30, prevId:28}, " +

                        "{id:30, question:'question4', answer:'Ответ14', vote:0, author:{name:'Саня', total:0}, nextId:31, prevId:29}, " +
                        "{id:31, question:'question4', answer:'Ответ24', vote:0, author:{name:'Баня', total:0}, nextId:32, prevId:30}, " +
                        "{id:32, question:'question4', answer:'Ответ34', vote:0, author:{name:'Каня', total:0}, nextId:33, prevId:31}, " +
                        "{id:33, question:'question4', answer:'Ответ54', vote:0, author:{name:'Ваня', total:0}, nextId:34, prevId:32}, " +
                        "{id:34, question:'question4', answer:'', vote:0, author:{name:'Даня', total:0}, nextId:35, prevId:33}]",
                service.getAll().toString());
    }

    private void assertEqualsM(String expected, String actual) {
        assertEquals(expected.replace("}, {", "}, \n{"), actual.replace("}, {", "}, \n{"));
    }

    @Test
    public void shouldUsers_setVote() {
        int questionsCount = 3;
        file.append("name|question1|question2\r\n");
        file.append("Саня|Ответ11|Ответ12\r\n");
        file.append("Баня|Ответ21|Ответ22\r\n");
        file.append("Ваня|Ответ31|Ответ32\r\n");

        QuestionAnswerService service = new QuestionAnswerService(fileName, charset,
                separator, nameColumn,
                nextLineSeparator, nextLineReplacement, questionsCount);

        service.setVote(0, 5);
        service.setVote(1, 0);

        service.setVote(3, 10);
        service.setVote(4, 7);

        assertEquals("[name, question1, question2]",
                service.getQuestions().toString());

        String a1 = "{name:'Саня', total:15}";
        String a2 = "{name:'Баня', total:7}";
        String a3 = "{name:'Ваня', total:0}";
        assertEquals(Arrays.asList(a1, a2, a3).toString(),
                service.getAuthors().toString());

        assertEqualsM("[{id:0, question:'name', answer:'Саня', vote:5, author:" + a1 + ", nextId:1, prevId:-1}, " +
                        "{id:1, question:'name', answer:'Баня', vote:0, author:" + a2 + ", nextId:2, prevId:0}, " +
                        "{id:2, question:'name', answer:'Ваня', vote:0, author:" + a3 + ", nextId:3, prevId:1}, " +

                        "{id:3, question:'question1', answer:'Ответ11', vote:10, author:" + a1 + ", nextId:4, prevId:2}, " +
                        "{id:4, question:'question1', answer:'Ответ21', vote:7, author:" + a2 + ", nextId:5, prevId:3}, " +
                        "{id:5, question:'question1', answer:'Ответ31', vote:0, author:" + a3 + ", nextId:6, prevId:4}, " +

                        "{id:6, question:'question2', answer:'Ответ12', vote:0, author:" + a1 + ", nextId:7, prevId:5}, " +
                        "{id:7, question:'question2', answer:'Ответ22', vote:0, author:" + a2 + ", nextId:8, prevId:6}, " +
                        "{id:8, question:'question2', answer:'Ответ32', vote:0, author:" + a3 + ", nextId:9, prevId:7}]",
                service.getAll().toString());

        String fileName2 = "target/report-" + Math.abs(new Random().nextInt()) + ".csv";
        service.saveToFile(fileName2);

        WithFile file2 = new WithFile(fileName2);
        assertEquals("Total|name|question1|question2\r\n" +
                        "15|5 => Саня|10 => Ответ11|0 => Ответ12\r\n" +
                        "7|0 => Баня|7 => Ответ21|0 => Ответ22\r\n" +
                        "0|0 => Ваня|0 => Ответ31|0 => Ответ32\r\n",
                file2.load());

        service.saveToFile(fileName2);

        file2 = new WithFile(fileName2);
        assertEquals("Total|name|question1|question2\r\n" +
                        "15|5 => Саня|10 => Ответ11|0 => Ответ12\r\n" +
                        "7|0 => Баня|7 => Ответ21|0 => Ответ22\r\n" +
                        "0|0 => Ваня|0 => Ответ31|0 => Ответ32\r\n",
                file2.load());
    }
}
