package model;

import java.io.FileReader;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MySQL {

    private static Connection connection;

    static {

         JSONParser parser = new JSONParser();
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String userDirectory = FileSystems.getDefault()
                    .getPath("")
                    .toAbsolutePath()
                    .toString();

            String url = userDirectory + "\\lib\\databs.json";

            Object obj = parser.parse(new FileReader(url));
            JSONObject j = (JSONObject) obj;

            String host = String.valueOf(j.get("host"));
            String databseName = String.valueOf(j.get("databaseName"));
            String username = String.valueOf(j.get("username"));
            String password = String.valueOf(j.get("password"));

            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + databseName + "", username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ResultSet execute(String query) throws Exception{
      
            Statement statement = connection.createStatement();
            if (query.startsWith("SELECT")) {
                ResultSet r = statement.executeQuery(query);
                return r;
            } else {
                int result = statement.executeUpdate(query);
                return null;

            }


    }

}
