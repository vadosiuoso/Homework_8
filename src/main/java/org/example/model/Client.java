package org.example.model;

import org.example.db.Database;

import java.sql.Connection;

public class Client {
    private long id;
    private String name;

    public Client(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
