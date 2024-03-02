import java.sql.*;
import java.util.Arrays;

public class NonPooling {
    public static void main(String[] args) throws SQLException {

        long startTime = System.currentTimeMillis();
        for(int i=0; i < 130 ; i++) {

            System.out.println("Run #"+i);
            Thread t = new Thread(new ParallelTask());
            t.start();
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("All operations completed! taking time: " +  executionTime + " milliseconds");
    }

    public static class ParallelTask implements Runnable {

        ParallelTask() {
        }

        @Override
        public void run() {
            testSelectTable();
        }

        public void testSelectTable() {

            Connection conn = null;
            try {
                // Create a connection pool
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prototypea", "ababshet", "");

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
                conn.close();
            }
            catch(SQLException e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
                throw new RuntimeException(e);
            }
            finally {
                try {
                    if(conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.err.println(Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }
            }



        }
    }
}