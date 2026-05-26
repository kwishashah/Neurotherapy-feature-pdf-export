import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMySQL {

    private static final Logger logger = LoggerFactory.getLogger(TestMySQL.class);

    private TestMySQL() {}

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/clinic", "root", "yourpassword")) {
            logger.info("Connected to MySQL!");
        } catch (SQLException e) {
            logger.error("MySQL connection failed", e);
        }
    }
}
