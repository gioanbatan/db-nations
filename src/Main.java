import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Const
        String URL = "jdbc:mysql://localhost:3306/nations_db";
        String USER = "root";
        String PASSWORD = "root";

        // Queries
        String nationsQuery = """
                select c.name as country, c.country_id, r.name as region, cont.name as continent
                from countries c
                join regions r on r.region_id  = c.region_id
                join continents cont on cont.continent_id = r.continent_id
                where c.name like ?
                order by c.name;
                """;

        String countryLanguagesQuery = """
                select c.name  country, language
                from languages l
                join country_languages cl on l.language_id  = cl.language_id
                join countries c on c.country_id = cl.country_id
                where cl.country_id = ?;
                """;

        String countryStatsQuery = """
                select c.name country, 'year', population, gdp
                from country_stats cs
                join countries c on c.country_id = cs.country_id
                where cs.country_id = ?;
                """;

        Scanner scan = new Scanner(System.in);

        System.out.println("-- SQL query in java --");
        System.out.println("-----------------------\n");

        // User input
        System.out.print("-> Inserire la nazione da cercare o parte del suo nome: ");
        String userInput = scan.nextLine();

        // Connection object
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // Prepared statement from query
            try (PreparedStatement ps = connection.prepareStatement(nationsQuery)) {
                // Parameters
                ps.setString(1, "%" + userInput + "%");

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

        // Second user input
        System.out.print("-> Inserire l'id di una nazione: ");
        userInput = scan.nextLine();  // ADD handle exceptions

        // Connection object
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // Prepared statement from query
            try (PreparedStatement ps = connection.prepareStatement(countryLanguagesQuery)) {
                // Parameters
                ps.setInt(1, Integer.parseInt(userInput));

                // Execute query
                try (ResultSet rs = ps.executeQuery()) {
                    String nationName = rs.getString(1);
                    System.out.println("Nazione selezionata: " + nationName);

                    /*List <String> */
                    // Result iterations
                    while (rs.next()) {
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