package pe.edu.vallegrande.Mybackend.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testConnection() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn);
            System.out.println("Conexión exitosa a la base de datos: " + conn.getMetaData().getURL());
        }
    }
}
