import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionPool {
    private static ArrayBlockingQueue<Connection> connections;

    public ConnectionPool(String url, String username, String password) throws SQLException, InterruptedException {
        int capacity = 10;
        connections = new ArrayBlockingQueue<>(capacity);

        for (int i = 0; i < capacity; i++) {
            connections.put(DriverManager.getConnection(url, username, password));
            //connections.add(DriverManager.getConnection(url, username, password));
        }
    }

    public Connection getConnection() throws InterruptedException {
        Connection conn = connections.take();
        return conn;
    }

    public static void returnConnection(Connection conn) throws InterruptedException {
        connections.put(conn);
    }
}
