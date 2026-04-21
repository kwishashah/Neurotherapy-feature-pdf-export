import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestMySQL {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/clinic", "root", "yourpassword")) {
            System.out.println("Connected to MySQL!");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
