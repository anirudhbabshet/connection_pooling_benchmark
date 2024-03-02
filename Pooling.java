

import java.sql.*;

public class Pooling {
    public static void main(String[] args) throws SQLException, InterruptedException {

        ConnectionPool pool = new ConnectionPool("jdbc:postgresql://localhost:5432/prototypea", "ababshet", "");
        long startTime = System.currentTimeMillis();
        for(int i=0; i < 6241; i++) {

            System.out.println("Run #"+i);
            Thread t = new Thread(new ParallelTask(pool));
            t.start();
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("All operations completed! taking time: " +  executionTime + " milliseconds");
    }

    public static class ParallelTask implements Runnable {

        ConnectionPool pool;

        ParallelTask( ConnectionPool pool) {
            this.pool=pool;
        }

        @Override
        public void run() {
            try {
                testSelectTable(pool);
            } catch (SQLException | InterruptedException e) {

                throw new RuntimeException(e);
            }
        }

        public void testSelectTable(ConnectionPool pool) throws SQLException, InterruptedException {
            // Create a connection pool
            //ConnectionPool pool = new ConnectionPool("jdbc:postgresql://localhost:5432/prototypea", "ababshet", "");
            Connection conn = null;
            try {
                // Get a connection from the pool
                conn = pool.getConnection();

                // Create a statement
                Statement stmt = conn.createStatement();

                // Execute a query
                ResultSet rs = stmt.executeQuery("SELECT * FROM company");

                // Iterate over the results
                while (rs.next()) {
                    System.out.println(rs.getString("name"));
                }

                // Close the connection
                rs.close();
                stmt.close();
                ConnectionPool.returnConnection(conn);
            } catch (InterruptedException e) {
                ConnectionPool.returnConnection(conn);
                throw new RuntimeException(e);
            }
            finally {
                ConnectionPool.returnConnection(conn);
            }
        }
    }

}

