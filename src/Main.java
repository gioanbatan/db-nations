import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Const
        String URL = "jdbc:mysql://localhost:3306/nations_db";
        String USER = "root";
        String PASSWORD = "root";

        Scanner scan = new Scanner(System.in);

        System.out.println("-- SQL query in java --");
        System.out.println("-----------------------\n");

        // User input
        System.out.print("-> Inserire la nazione da cercare o parte del suo nome: ");
        String userInput = scan.nextLine();

        // Connection object
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // SQL query
            String query = """
                    select c.name as country, c.country_id, r.name as region, cont.name as continent
                    from countries c
                    join regions r on r.region_id  = c.region_id
                    join continents cont on cont.continent_id = r.continent_id
                    where c.name like ?
                    order by c.name;
                    """;
            // Prepared statement from query
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                // Parameters
                userInput = "%" + userInput + "%";
                ps.setString(1, userInput);

                // Execute query
                try (ResultSet rs = ps.executeQuery()) {
                    // Result iterations
                    while (rs.next()) {
                        String nationName = rs.getString(1);
                        int nationId = rs.getInt(2);
                        String regionName = rs.getString(3);
                        String continentName = rs.getString(4);
                        System.out.printf("%-47s %4d %29s %16s%n", nationName, nationId, regionName, continentName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}