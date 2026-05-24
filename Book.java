package com.beykent.akillitestanaliz;

public class Book {
    private int id;
    private String name;
    private String subject;
    private int progress;

    public Book(int id, String name, String subject, int progress) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.progress = progress;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSubject() { return subject; }
    public int getProgress() { return progress; }
}