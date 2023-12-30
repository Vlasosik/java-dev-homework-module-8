package org.example.migration;

import org.flywaydb.core.Flyway;

import static org.example.util.Constants.*;

public class MigrationMain {
    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource(DB_URL, DB_USER, DB_PASS)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
    }
}



