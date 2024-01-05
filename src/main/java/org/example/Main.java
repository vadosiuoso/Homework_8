package org.example;

import org.example.dao.ClientService;
import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:h2:./database","sa", "")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();

        ClientService clientService = new ClientService();
        System.out.println("ID = " + clientService.create("Andrew"));
    }
}