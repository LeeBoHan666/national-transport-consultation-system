import java.sql.*;
import java.util.*;

public class SQL {
    static user User;
    static Connection con;

    SQL() throws Exception {
        String dbUrl = System.getenv().getOrDefault(
                "TRANSPORT_DB_URL", "jdbc:mysql://localhost:3306/city");
        String dbUser = System.getenv().getOrDefault("TRANSPORT_DB_USER", "root");
        String dbPassword = System.getenv("TRANSPORT_DB_PASSWORD");
        if (dbPassword == null) {
            throw new IllegalStateException("请先设置 TRANSPORT_DB_PASSWORD 环境变量");
        }
        con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void LoadData() throws Exception {

        Map<String, City> map = new HashMap<>();

        Map<String, String> cityRoutes = new LinkedHashMap<>();
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM city");
        while (rs.next()) {
            int cid = 0;
            String ridStr = "";
            try { cid = rs.getInt("CityID"); } catch (Exception ignored) {}
            try { ridStr = rs.getString("RouteID"); } catch (Exception ignored) {}
            City c = new City(rs.getString("name"),
                    rs.getDouble("longitude"), rs.getDouble("latitude"), cid);
            Main.Cities.add(c);
            map.put(c.name, c);
            cityRoutes.put(c.name, ridStr);
        }


        Map<String, Route> routeMap = new HashMap<>();
        rs = con.createStatement().executeQuery("SELECT * FROM route");
        while (rs.next()) {
            String rid = String.valueOf(rs.getInt("RouteID"));
            Route r = new Route(
                    rid,
                    rs.getString("from"),
                    rs.getString("to"),
                    rs.getInt("way"),
                    rs.getString("number"),
                    String.valueOf(rs.getDouble("price")),
                    rs.getString("time"),
                    rs.getString("departure"),
                    rs.getString("arrival"));
            Main.Routes.add(r);
            routeMap.put(rid, r);
        }


        for (City c : Main.Cities) {
            String Str = cityRoutes.get(c.name);
            if (Str != null) {
                for (String id : Str.split(",")) {
                    id = id.trim();
                    Route rt = routeMap.get(id);
                    if (rt != null) c.routeList.add(rt);
                }
            }
        }
        return ;
    }

    public static boolean LoginCheck(String username,String password)throws Exception{

        String prompt = "SELECT * FROM user WHERE Username = ? AND Password = ?";
        PreparedStatement pst = con.prepareStatement(prompt);
        pst.setString(1,username);
        pst.setString(2,password);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            User = new user(rs.getInt("ID"),rs.getString("Username"),
                    rs.getString("Password"),rs.getString("Role"));
            return true;
        }
        return false;
    }


    public static String getUsername(){
        return User.username;
    }
    public static String getPassword(){
        return User == null ? "" : User.password;
    }
    public static String getRole(){
        return User.role;
    }


    public static void addLine(String table, String[] columns, String[] values) throws Exception {
        StringBuilder cols = new StringBuilder(), placeholders = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            if (cols.length() > 0) {
                cols.append(","); placeholders.append(",");
            }
            cols.append("`").append(columns[i]).append("`");
            placeholders.append("?");
        }
        String sql = "INSERT INTO `" + table + "` (" + cols + ") VALUES (" + placeholders + ")";
        PreparedStatement pst = con.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            pst.setString(i + 1, values[i]);
        }
        pst.executeUpdate();
    }

    public static void delLine(String table, String column, String value) throws Exception {
        String sql = "DELETE FROM `" + table + "` WHERE `" + column + "` = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, value);
        pst.executeUpdate();
    }

}
