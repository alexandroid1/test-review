package com.apofig.samples.spring.service;

public class Author {
    private String name;
    private int total;

    public Author(String name) {
        this.name = name;
        this.total = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void increaseTotal(int delta) {
        total += delta;
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", total:" + total +
                '}';
    }
}
