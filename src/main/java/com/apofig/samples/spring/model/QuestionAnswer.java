package com.apofig.samples.spring.model;

import com.apofig.samples.spring.service.Author;

public class QuestionAnswer {

	private int id;

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", question:'" + question + '\'' +
                ", answer:'" + answer + '\'' +
                ", vote:" + vote +
                ", author:" + author.toString() +
                ", nextId:" + nextId +
                ", prevId:" + prevId +
                '}';
    }

    private String question;
	private String answer;
	private int vote;
    private Author author;

	private int nextId;
	private int prevId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		nextId = id + 1;
		prevId = id - 1;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public int getPrevId() {
		return prevId;
	}

	public int getNextId() {
		return nextId;
	}

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
