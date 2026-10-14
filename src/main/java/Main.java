import javafx.application.*;
import java.util.*;

public class Main {
    public static String URL = "http://localhost:11434/api/generate";
    public static String Model = "gemma4:e4b";
    public static String API;
    static int code=0;
    public static String OutputPath = System.getProperty("user.dir") + "\\";
    public static String From;
    public static String To;
    public static SQL SQL;

    public static City cityByName(String name) {
        for (City c : Cities)
            if (c.name.equals(name)) return c;
        return null;
    }

    public static List<City> Cities =  new ArrayList<>();
    public static List<Route> Routes = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        try{
                SQL = new SQL();
                SQL.LoadData();
            }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Application.launch(Login.class, args);

    }

    public static String getCode(){
        int num1 = (int)(Math.random()*10);
        int num2 = (int)(Math.random()*10);
        int Operation = (int)(Math.random()*3);
        code=0;

        if(Operation==0){
            code+=num1+num2;
            return num1+" + "+num2;
        }
        if(Operation==1){
            code+=num1-num2;
            return num1+" - "+num2;
        }
        if(Operation==2){
            code+=num1*num2;
            return num1+" * "+num2;
        }

        return "ERROR";
    }
    public static boolean MatchCode(String get){
        return get.equals(""+code);
    }
}

class user{
    int id;
    String username;
    String password;
    String role;
    user(){}
    user(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}

class City {
    String name;
    double longitude;
    double latitude;
    List<Route> routeList = new ArrayList<>();
    int CityID;

    City() {}
    City(String name, double longitude, double latitude, int CityID) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.CityID = CityID;
    }
}

class Route {
    String from;
    String to;
    int way;        // 1公路 2高铁 3飞机 4航运 5普速
    String number;  // 车次号/航班号 如 G1, MU5102
    String price;
    String duration;     // 耗时
    String departure_time;  // 出发时间
    String arrival_time;    // 到达时间
    String RouteID;
    int travelDay;     // 需要几天到达（0=当天，1=次日...）

    Route(String ID, String from, String to, int way, String number, String price, String duration, String departure_time, String arrival_time) {
        this.RouteID = ID;
        this.from = from;
        this.to = to;
        this.way = way;
        this.number = number;
        this.price = price;
        this.duration = duration;
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
        this.travelDay = calcTravelDay(duration);
    }

    Route() {}

    static int calcTravelDay(String dur) {
        if (dur == null || dur.isEmpty()) return 0;
        double hours = DataManager.parseHours(dur);
        return (int)(hours / 24);
    }
}
