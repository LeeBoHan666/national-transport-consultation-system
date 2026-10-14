import java.util.*;

class Schedule {

    /** 交通工具类型枚举 */
    public enum Type {
        TRAIN("火车"),
        FLIGHT("飞机");

        private final String displayName;
        Type(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    //————————成员变量——————————

    private String id;          // 班次编号，如 "G1", "CA1501"
    private Type type;          // 交通工具类型
    private String fromCity;    // 出发城市
    private String toCity;      // 到达城市
    private int departureTime;  // 发车时间（分钟，0-1439）。如 06:30 = 390
    private int arrivalTime;    // 到达时间（分钟，0-1439）。如 11:24 = 684
    private int travelDays;     // 额外运行天数（0=当日达，1=次日达）
    private double cost;        // 票价（元）

    //——————————构造方法——————————

    public Schedule() {}

    public Schedule(String id, Type type, String fromCity, String toCity,
                    int departureTime, int arrivalTime, double cost) {
        this(id, type, fromCity, toCity, departureTime, arrivalTime, 0, cost);
    }

    public Schedule(String id, Type type, String fromCity, String toCity,
                    int departureTime, int arrivalTime, int travelDays, double cost) {
        this.id = id;
        this.type = type;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.travelDays = travelDays;
        this.cost = cost;
    }

    //——————————核心时间计算——————————

    public int getTravelMinutes() {
        int base;
        if (arrivalTime >= departureTime) {
            base = arrivalTime - departureTime;
        } else {
            base = (1440 - departureTime) + arrivalTime;
        }
        return base + travelDays * 1440;
    }

    public int getWaitMinutes(int clockTime) {
        if (departureTime >= clockTime) {
            return departureTime - clockTime;
        } else {
            return (1440 - clockTime) + departureTime;
        }
    }

    //————————————时间标准化工具——————————————

    /** 将绝对时间转义为钟表时间 */
    public static String formatTime(int minutes) {
        int hours = (minutes / 60) % 24;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);//不足两位时自动补0
    }

    /** 字符串时间转义为标准时间戳 */
    public static int parseTime(String timeStr) {
        String[] parts = timeStr.trim().split(":");
        if (parts.length != 2)
            throw new IllegalArgumentException("时间格式错误，请使用 HH:MM 格式");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59)
            throw new IllegalArgumentException("时间超出范围（00:00-23:59）");
        return hours * 60 + minutes;
    }

    //——————————getters & setters——————————

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public String getFromCity() { return fromCity; }
    public void setFromCity(String fromCity) { this.fromCity = fromCity; }

    public String getToCity() { return toCity; }
    public void setToCity(String toCity) { this.toCity = toCity; }

    public int getDepartureTime() { return departureTime; }
    public void setDepartureTime(int departureTime) { this.departureTime = departureTime; }

    public int getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    public int getTravelDays() { return travelDays; }
    public void setTravelDays(int days) { this.travelDays = days; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    //——————————方法重载——————————
    /** 将数据以标准字符串输出 */
    @Override
    public String toString() {
        return String.format("%s %s: %s → %s, 发 %s, 到 %s, ¥%.2f",
                type.getDisplayName(), id, fromCity, toCity,
                formatTime(departureTime), formatTime(arrivalTime), cost);
    }
    //——————————判断类是否相等——————————
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) && type == schedule.type;
    }
    //——————重写哈希编码规则，以便hashcode类型数据（list、set、hashmap）比较————————
    @Override
    public int hashCode() { return Objects.hash(id, type); }
}

class RouteStep {

    /** 使用的交通班次 */
    private Schedule schedule;

    /** 出发的绝对分钟（从行程起点起算，可能 >= 1440） */
    private int departureAbsolute;

    /** 到达的绝对分钟 */
    private int arrivalAbsolute;

    /** 出发前在本站的等候分钟数（第一步为 0） */
    private int waitMinutes;

    public RouteStep() {}

    public RouteStep(Schedule schedule, int departureAbsolute,
                     int arrivalAbsolute, int waitMinutes) {
        this.schedule = schedule;
        this.departureAbsolute = departureAbsolute;
        this.arrivalAbsolute = arrivalAbsolute;
        this.waitMinutes = waitMinutes;
    }

    /** 出发是第几天（从 0 开始） */
    public int getDepartureDay() { return departureAbsolute / 1440; }

    /** 到达是第几天 */
    public int getArrivalDay()   { return arrivalAbsolute / 1440; }

    /** 出发的墙钟时刻（0-1439），如 06:30 = 390 */
    public int getDepartureClockTime() { return departureAbsolute % 1440; }

    /** 到达的墙钟时刻，如 11:24 = 684 */
    public int getArrivalClockTime()   { return arrivalAbsolute % 1440; }

    /** 本段纯旅行时间（分钟），从 Schedule 获取 */
    public int getTravelMinutes()      { return schedule.getTravelMinutes(); }

    // ===== Getters & Setters =====

    public Schedule getSchedule()              { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }

    public int getDepartureAbsolute()              { return departureAbsolute; }
    public void setDepartureAbsolute(int abs)      { this.departureAbsolute = abs; }

    public int getArrivalAbsolute()                { return arrivalAbsolute; }
    public void setArrivalAbsolute(int abs)        { this.arrivalAbsolute = abs; }

    public int getWaitMinutes()                    { return waitMinutes; }
    public void setWaitMinutes(int minutes)         { this.waitMinutes = minutes; }
}

class RouteResult {

    private List<RouteStep> steps;
    private int totalTimeMinutes;
    private double totalCost;

    public RouteResult() { this.steps = new ArrayList<>(); }

    public RouteResult(List<RouteStep> steps, int totalTimeMinutes, double totalCost) {
        this.steps = steps;
        this.totalTimeMinutes = totalTimeMinutes;
        this.totalCost = totalCost;
    }

    /** 是否找到了可行路线 */
    public boolean isReachable() {
        return steps != null && !steps.isEmpty();
    }

    //————————时间格式化——————————

    public int getTotalDays() { return totalTimeMinutes / 1440; }
    public int getRemainingHours() { return (totalTimeMinutes % 1440) / 60; }
    public int getRemainingMinutes() { return totalTimeMinutes % 60; }

    /** 时间标准化 */
    public String getFormattedTotalTime() {
        int days = totalTimeMinutes / 1440;
        int hours = (totalTimeMinutes % 1440) / 60;
        int mins = totalTimeMinutes % 60;
        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("天 ");
        if (hours > 0 || days > 0) sb.append(hours).append("小时 ");
        if (mins > 0 || sb.length() == 0) sb.append(mins).append("分钟");
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        if (!isReachable()) return "未找到可行路线";

        StringBuilder sb = new StringBuilder();
        sb.append("总时间：").append(getFormattedTotalTime()).append("\n");
        sb.append("总费用：¥").append(String.format("%.2f", totalCost)).append("\n");
        sb.append("中转次数：").append(steps.size() - 1).append("\n\n");

        int stepNum = 1;
        for (int i = 0; i < steps.size(); i++) {
            RouteStep step = steps.get(i);
            Schedule s = step.getSchedule();

            if (i > 0 && step.getWaitMinutes() > 0) {
                sb.append("   中转等待：").append(formatMinutes(step.getWaitMinutes())).append("\n\n");
            }

            sb.append("第").append(stepNum).append("步：")
              .append(s.getType().getDisplayName()).append(" ").append(s.getId()).append("\n");
            sb.append("  第").append(step.getDepartureDay()).append("天 ")
              .append(Schedule.formatTime(step.getDepartureClockTime()))
              .append(" 从 ").append(s.getFromCity()).append(" 出发\n");
            sb.append("  第").append(step.getArrivalDay()).append("天 ")
              .append(Schedule.formatTime(step.getArrivalClockTime()))
              .append(" 到达 ").append(s.getToCity()).append("\n");
            sb.append("  行程时间：").append(formatMinutes(step.getTravelMinutes()))
              .append("，票价：¥").append(String.format("%.2f", s.getCost())).append("\n");
            stepNum++;
        }
        return sb.toString();
    }

    private static String formatMinutes(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        if (hours > 0) return hours + "小时" + (mins > 0 ? mins + "分钟" : "");
        return mins + "分钟";
    }

    //———————— Getters & Setters————————

    public List<RouteStep> getSteps()           { return steps; }
    public void setSteps(List<RouteStep> steps)  { this.steps = steps; }

    public int getTotalTimeMinutes()               { return totalTimeMinutes; }
    public void setTotalTimeMinutes(int minutes)    { this.totalTimeMinutes = minutes; }

    public double getTotalCost()                   { return totalCost; }
    public void setTotalCost(double cost)           { this.totalCost = cost; }
}

class RouteQueryResult {
    public boolean ok;
    public String from, to;
    public int mode;
    public double totalTime;   // 总耗时（小时）
    public double totalPrice;  // 总费用（元）
    public int transfers;      // 换乘次数
    public List<Route> routes; // 路线步骤列表

    public RouteQueryResult() {
        this.routes = new ArrayList<>();
    }
}

class
DataManager {

    // 成员变量
    public static final int MODE_TIME = 0; // 时间最短
    public static final int MODE_PRICE = 1; // 价格最低
    public static final int MODE_TRANS = 2; // 换乘最少

    private static DataManager instance = new DataManager();

    private Set<String> cities;
    private List<Schedule> trainSchedules;
    private List<Schedule> flightSchedules;
    private List<Schedule> allSchedules;

    private DataManager() {
        initFromTransport();
    }

    /** 初始化 */
    public static DataManager getInstance() { return instance; }


    public void refresh() {
        initFromTransport();
    }


    private void initFromTransport() {
        // ── 城市集合 ──
        cities = new LinkedHashSet<>();
        if (Main.Cities != null) {
            for (City c : Main.Cities) {
                cities.add(c.name);
            }
        }

        // ── 班次列表 ──
        trainSchedules = new ArrayList<>();
        flightSchedules = new ArrayList<>();
        allSchedules = new ArrayList<>();

        if (Main.Routes != null) {
            for (Route r : Main.Routes) {
                Schedule s = convertRoute(r);
                if (s.getType() == Schedule.Type.FLIGHT) {
                    flightSchedules.add(s);
                } else {
                    trainSchedules.add(s);
                }
                allSchedules.add(s);
            }
        }
    }


    /** 获取所有城市名 */
    public Set<String> getCities() { return cities; }

    /** 获取（火车 + 航班）的合并列表 */
    public List<Schedule> getAllSchedules() { return allSchedules; }

    /** 获取火车班次 */
    public List<Schedule> getTrainSchedules() { return trainSchedules; }

    /** 获取仅航班班次 */
    public List<Schedule> getFlightSchedules() { return flightSchedules; }

    //——————————转换函数——————————

    static Schedule convertRoute(Route r) {
        Schedule s = new Schedule();

        // ID & 类型
        s.setId(r.number != null ? r.number : "");
        s.setType(convertWay(r.way));

        // 起终点
        s.setFromCity(r.from);
        s.setToCity(r.to);

        // 票价（String → double）
        s.setCost(parsePrice(r.price));

        // 发车时间（"HH:MM" → int 分钟）
        s.setDepartureTime(parseHHMM(r.departure_time));

        // 到达时间（处理 "+N HH:MM" 格式）
        int[] arr = parseArrivalTime(r.arrival_time);
        s.setArrivalTime(arr[0]);
        s.setTravelDays(arr[1]);

        return s;
    }


    static Schedule.Type convertWay(int way) {
        return way == 3 ? Schedule.Type.FLIGHT : Schedule.Type.TRAIN;
    }


    static int parseHHMM(String s) {
        if (s == null || s.isEmpty()) return 0;
        try {
            String[] parts = s.trim().split(":");
            if (parts.length != 2) return 0;
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    static int[] parseArrivalTime(String s) {
        int travelDays = 0;
        String timePart = (s != null) ? s.trim() : "";

        if (timePart.isEmpty()) return new int[]{0, 0};

        // 检测 "+N " 前缀（如 "+1 06:00"）
        if (timePart.startsWith("+")) {
            int spaceIdx = timePart.indexOf(' ');
            if (spaceIdx > 0) {
                try {
                    travelDays = Integer.parseInt(timePart.substring(1, spaceIdx));
                } catch (NumberFormatException e) {
                    travelDays = 0;
                }
                timePart = timePart.substring(spaceIdx + 1).trim();
            }
        }

        return new int[]{parseHHMM(timePart), travelDays};
    }

    static double parsePrice(String price) {
        if (price == null || price.isEmpty()) return 0;
        try {
            return Double.parseDouble(price);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    //————————反向转换（RouteResult至RouteQueryResult（供 GUI 使用））————————

    static RouteQueryResult toResult(RouteResult dijResult, String from, String to, int mode) {
        RouteQueryResult r = new RouteQueryResult();
        r.from = from;
        r.to = to;
        r.mode = mode;

        if (!dijResult.isReachable()) {
            r.ok = false;
            return r;
        }

        r.ok = true;
        r.totalTime = dijResult.getTotalTimeMinutes() / 60.0;   // 分钟 → 小时
        r.totalPrice = dijResult.getTotalCost();
        r.transfers = Math.max(0, dijResult.getSteps().size() - 1);

        r.routes = new ArrayList<>();
        for (RouteStep step : dijResult.getSteps()) {
            r.routes.add(scheduleToRoute(step.getSchedule()));
        }

        return r;
    }

    static Route scheduleToRoute(Schedule s) {
        Route r = new Route();
        r.from = s.getFromCity();
        r.to = s.getToCity();
        r.number = s.getId();
        r.way = (s.getType() == Schedule.Type.FLIGHT) ? 3 : 2;
        r.price = String.valueOf((int) Math.round(s.getCost()));
        r.duration = formatDurationMin(s.getTravelMinutes());
        r.departure_time = Schedule.formatTime(s.getDepartureTime());
        if (s.getTravelDays() > 0) {
            r.arrival_time = "+" + s.getTravelDays() + " " + Schedule.formatTime(s.getArrivalTime());
        } else {
            r.arrival_time = Schedule.formatTime(s.getArrivalTime());
        }
        r.travelDay = s.getTravelDays();
        return r;
    }

    /** Duration标准化 */
    static String formatDurationMin(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;
        return m == 0 ? h + "h" : h + "h" + m + "m";
    }
    /** 时间转换，如转“4h54m"为”4.9“小时  */
    public static double parseHours(String s) {
        if (s == null || s.isEmpty()) return 1;
        s = s.toLowerCase().replace("h", "").replace("m", ":").trim();
        if (s.contains(":")) {
            String[] p = s.split(":");
            double h = p[0].isEmpty() ? 0 : Double.parseDouble(p[0]);
            double m = p.length > 1 ? Double.parseDouble(p[1]) : 0;
            return h + m / 60;
        }
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return 1; }
    }
}

class RouteFinder {

    //  公开接口

    public static RouteResult findRoute(String from, String to, String strategy,
                                         Set<Schedule.Type> transportTypes) {
        // 空值保护
        if (from == null || to == null);

        // 同城：零耗时零费用
        if (from.equals(to)) return unreachable();

        // 综合模式：null/空 → 查询全部两种交通工具（TRAIN + FLIGHT）
        Set<Schedule.Type> types = (transportTypes == null || transportTypes.isEmpty())
                ? EnumSet.allOf(Schedule.Type.class)
                : transportTypes;

        // 策略分发
        return switch (strategy) {
            case "cheapest"   -> findCheapestRoute(from, to, types);
            case "convenient" -> findConvenientRoute(from, to, types);
            default           -> findFastestRoute(from, to, types);
        };
    }

    public static RouteResult findRoute(String from, String to, String strategy,
                                         Schedule.Type transportType) {
        Set<Schedule.Type> types = (transportType == null)
                ? EnumSet.allOf(Schedule.Type.class)
                : EnumSet.of(transportType);
        return findRoute(from, to, strategy, types);
    }

    //  策略一：最快到达

    private static RouteResult findFastestRoute(String from, String to,
                                                 Set<Schedule.Type> types) {
        DataManager dm = DataManager.getInstance();
        List<Schedule> schedules = filterByTypes(dm.getAllSchedules(), types);
        Set<String> cities = dm.getCities();

        //——————————Dijkstra——————————
        Map<String, Integer> dist = new HashMap<>();         // dist[v]: 总耗时（优化目标）
        Map<String, Integer> arrivalClock = new HashMap<>(); // arrivalClock[v]: 到达时刻（用于计算等候）
        Map<String, String> prev = new HashMap<>();          // prev[v]: 前驱城市（路径回溯用）
        Map<String, Schedule> usedSchedule = new HashMap<>();// usedSchedule[v]: 到达 v 所用的班次
        Set<String> visited = new HashSet<>();               // 已确定最短路径的节点集合

        // 初始化：所有城市距离为无穷大，起点为 0
        for (String c : cities) dist.put(c, Integer.MAX_VALUE);
        dist.put(from, 0);
        arrivalClock.put(from, 0);

        //——————————Dijkstra——————————
        while (true) {
            // 选未访问节点中 dist 最小的（简单 O(V) 查找）
            String u = minKey(dist, visited);
            if (u == null || u.equals(to)) break;
            visited.add(u);

            // 遍历 u 出发的所有边
            for (Schedule s : schedules) {
                if (!s.getFromCity().equals(u)) continue;
                String v = s.getToCity();
                if (!dist.containsKey(v) || visited.contains(v)) continue;

                int newDist, newClock;

                if (u.equals(from)) {
                    // ─── 第一步：不计算等候 ───
                    newDist = s.getTravelMinutes();
                    newClock = s.getDepartureTime() + newDist;
                } else {
                    // ─── 中转步：计算等候时间 ───
                    int clockU = arrivalClock.get(u) % 1440;
                    int wait = s.getWaitMinutes(clockU);
                    newDist = dist.get(u) + wait + s.getTravelMinutes();
                    newClock = arrivalClock.get(u) + wait + s.getTravelMinutes();
                }

                // Dijkstra 松弛操作：找到更短路径则更新
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    arrivalClock.put(v, newClock);
                    prev.put(v, u);
                    usedSchedule.put(v, s);
                }
            }
        }

        if (!prev.containsKey(to)) return unreachable();
        return buildPath(from, to, prev, usedSchedule, dist, arrivalClock, false);
    }

    //  策略二：最省钱到达（Dijkstra，费用权重）

    private static RouteResult findCheapestRoute(String from, String to,
                                                  Set<Schedule.Type> types) {
        DataManager dm = DataManager.getInstance();
        List<Schedule> schedules = filterByTypes(dm.getAllSchedules(), types);
        Set<String> cities = dm.getCities();

        // Dijkstra 状态表（费用权重）
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Map<String, Schedule> usedSchedule = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String c : cities) dist.put(c, Double.MAX_VALUE);
        dist.put(from, 0.0);

        while (true) {
            String u = minKeyDouble(dist, visited);
            if (u == null || u.equals(to)) break;
            visited.add(u);

            for (Schedule s : schedules) {
                if (!s.getFromCity().equals(u)) continue;
                String v = s.getToCity();
                if (!dist.containsKey(v) || visited.contains(v)) continue;

                double nd = dist.get(u) + s.getCost();
                if (nd < dist.get(v)) {
                    dist.put(v, nd);
                    prev.put(v, u);
                    usedSchedule.put(v, s);
                }
            }
        }

        if (!prev.containsKey(to)) return unreachable();
        return buildPath(from, to, prev, usedSchedule, dist, true);
    }

    //  策略三：最少换乘（Dijkstra，换乘次数优先 → 总耗时决胜）

    private static RouteResult findConvenientRoute(String from, String to,
                                                   Set<Schedule.Type> types) {
        DataManager dm = DataManager.getInstance();
        List<Schedule> schedules = filterByTypes(dm.getAllSchedules(), types);
        Set<String> cities = dm.getCities();

        // —— Dijkstra 状态表 ——
        // 换乘次数 = 经过的边数 - 1（第一步 transfer=0，第二步起每步 +1）
        Map<String, Integer> disttrans = new HashMap<>();      // 到达 v 的最少换乘次数
        Map<String, Integer> totalTime = new HashMap<>();       // 到达 v 的总耗时（旅行+等候）
        Map<String, Integer> arrivalClock = new HashMap<>();    // 到达 v 的绝对时刻
        Map<String, String> prev = new HashMap<>();             // 前驱城市
        Map<String, Schedule> usedSchedule = new HashMap<>();   // 到达 v 所用的班次
        Set<String> visited = new HashSet<>();

        // 初始化
        for (String c : cities) {
            disttrans.put(c, Integer.MAX_VALUE);
            totalTime.put(c, Integer.MAX_VALUE);
        }
        disttrans.put(from, 0);
        totalTime.put(from, 0);
        arrivalClock.put(from, 0);

        while (true) {
            // 选取换乘最少中总耗时最短的未访问节点
            String u = null;
            int bestTrans = Integer.MAX_VALUE;
            int bestTime = Integer.MAX_VALUE;
            for (var e : disttrans.entrySet()) {
                String city = e.getKey();
                if (visited.contains(city)) continue;
                int t = e.getValue();               //换乘次数
                int tm = totalTime.get(city);       //换乘总耗时
                if (t < bestTrans || (t == bestTrans && tm < bestTime)) {
                    bestTrans = t;
                    bestTime = tm;
                    u = city;
                }
            }
            // 循环终止器
            if (u == null || u.equals(to)) break;
            visited.add(u);

            // 遍历 u 出发的所有边
            for (Schedule s : schedules) {
                if (!s.getFromCity().equals(u)) continue;
                String v = s.getToCity();
                if (!disttrans.containsKey(v) || visited.contains(v)) continue;

                int newTime, newClock, newTrans;

                if (u.equals(from)) {
                    // 第一步：不等候
                    newTrans = 0;
                    newTime = s.getTravelMinutes();
                    newClock = s.getDepartureTime() + newTime;
                } else {
                    // 中转步：换乘次数 +1，计算等候
                    newTrans = disttrans.get(u) + 1;
                    int clockU = arrivalClock.get(u) % 1440;
                    int wait = s.getWaitMinutes(clockU);
                    newTime = totalTime.get(u) + wait + s.getTravelMinutes();
                    newClock = arrivalClock.get(u) + wait + s.getTravelMinutes();
                }

                // 松弛：优先比较换乘次数，同次数比总耗时
                if (newTrans < disttrans.get(v)
                        || (newTrans == disttrans.get(v) && newTime < totalTime.get(v))) {
                    disttrans.put(v, newTrans);
                    totalTime.put(v, newTime);
                    arrivalClock.put(v, newClock);
                    prev.put(v, u);
                    usedSchedule.put(v, s);
                }
            }
        }

        if (!prev.containsKey(to)) return unreachable();
        return buildPath(from, to, prev, usedSchedule, totalTime, arrivalClock, false);
    }

    // 路径重建
    private static RouteResult buildPath(String from, String to,
            Map<String, String> prev, Map<String, Schedule> usedSchedule,
            Map<String, Integer> dist, Map<String, Integer> arrivalClock,
            boolean distIsCost) {

        // --- 从终点回溯到起点，构建城市序列 ---
        LinkedList<String> cities = new LinkedList<>();
        String cur = to;
        while (cur != null && !cur.equals(from)) {
            cities.addFirst(cur);
            cur = prev.get(cur);
        }
        cities.addFirst(from);

        List<RouteStep> steps = new ArrayList<>();
        double totalCost = 0;

        // --- 逐段重建 ---
        for (int i = 0; i < cities.size() - 1; i++) {
            String c = cities.get(i);
            String n = cities.get(i + 1);
            Schedule s = usedSchedule.get(n);

            if (c.equals(from)) {
                // 第一步：不等候，直接用班次时刻
                steps.add(new RouteStep(s, s.getDepartureTime(),
                        s.getDepartureTime() + s.getTravelMinutes(), 0));
            } else {
                // 中转步：用前一步的实际到达时刻计算等候
                RouteStep prevStep = steps.get(steps.size() - 1);
                int prevArrivalAbs = prevStep.getArrivalAbsolute();
                int clockU = prevArrivalAbs % 1440;
                int wait = s.getWaitMinutes(clockU);
                int depAbs = prevArrivalAbs + wait;
                int arrAbs = depAbs + s.getTravelMinutes();
                steps.add(new RouteStep(s, depAbs, arrAbs, wait));
            }
            totalCost += s.getCost();
        }

        RouteResult r = new RouteResult();
        r.setSteps(steps);
        r.setTotalTimeMinutes(dist.get(to));
        r.setTotalCost(totalCost);
        return r;
    }

    private static RouteResult buildPath(String from, String to,
            Map<String, String> prev, Map<String, Schedule> usedSchedule,
            Map<String, Double> dist, boolean distIsCost) {

        // 回溯构建城市序列（同最快路线）
        LinkedList<String> cities = new LinkedList<>();
        String cur = to;
        while (cur != null && !cur.equals(from)) {
            cities.addFirst(cur);
            cur = prev.get(cur);
        }
        cities.addFirst(from);

        List<RouteStep> steps = new ArrayList<>();
        double totalCost = 0;

        for (int i = 0; i < cities.size() - 1; i++) {
            String c = cities.get(i);
            String n = cities.get(i + 1);
            Schedule s = usedSchedule.get(n);

            if (c.equals(from)) {
                steps.add(new RouteStep(s, s.getDepartureTime(),
                        s.getDepartureTime() + s.getTravelMinutes(), 0));
            } else {
                RouteStep prevStep = steps.get(steps.size() - 1);
                int prevArrivalAbs = prevStep.getArrivalAbsolute();
                int clockU = prevArrivalAbs % 1440;
                int wait = s.getWaitMinutes(clockU);
                int depAbs = prevArrivalAbs + wait;
                int arrAbs = depAbs + s.getTravelMinutes();
                steps.add(new RouteStep(s, depAbs, arrAbs, wait));
            }
            totalCost += s.getCost();
        }

        // 总时间 = 最后到达 - 第一出发（纯旅行+等候）
        int totalTime = steps.isEmpty() ? 0
                : steps.get(steps.size() - 1).getArrivalAbsolute()
                - steps.get(0).getDepartureAbsolute();

        RouteResult r = new RouteResult();
        r.setSteps(steps);
        r.setTotalTimeMinutes(totalTime);
        r.setTotalCost(totalCost);
        return r;
    }

    //  Dijkstra 辅助方法

    /** 简单查找未访问节点中距离最小的 */
    private static String minKey(Map<String, Integer> dist, Set<String> visited) {
        String best = null;
        int min = Integer.MAX_VALUE;
        for (var e : dist.entrySet()) {
            if (!visited.contains(e.getKey()) && e.getValue() < min) {
                min = e.getValue();
                best = e.getKey();
            }
        }
        return best;
    }

    /** 简单查找未访问节点中距离最小的*/
    private static String minKeyDouble(Map<String, Double> dist, Set<String> visited) {
        String best = null;
        double min = Double.MAX_VALUE;
        for (var e : dist.entrySet()) {
            if (!visited.contains(e.getKey()) && e.getValue() < min) {
                min = e.getValue();
                best = e.getKey();
            }
        }
        return best;
    }

    /** 按类型过滤班次列表 */
    private static List<Schedule> filterByTypes(List<Schedule> all, Set<Schedule.Type> types) {
        if (types == null || types.isEmpty()
                || types.equals(EnumSet.allOf(Schedule.Type.class)))
            return all;
        List<Schedule> result = new ArrayList<>();
        for (Schedule s : all)
            if (types.contains(s.getType())) result.add(s);
        return result;
    }

    /** 构造"不可达"结果 */
    private static RouteResult unreachable() {
        RouteResult r = new RouteResult();
        r.setTotalTimeMinutes(-1);
        r.setTotalCost(-1);
        return r;
    }
}

class FloydValidator {

    private int n;                         // 城市数量
    private Map<String, Integer> index;    // 城市名>矩阵下标
    private String[] names;                // 矩阵下标>城市名
    private List<Schedule> allSchedules;   // 所有班次（用于边查找）

    // ──——距离矩阵——──
    private double[][] distTime;    // 最快：纯旅行时间（分钟），不含等候
    private double[][] distPrice;   // 最省钱：票价（元）
    private double[][] distTrans;   // 最少换乘：边数 + 旅行时间 （换乘优先，时间决胜）

    // ──——路径回溯矩阵——──
    private int[][] nextTime;
    private int[][] nextPrice;
    private int[][] nextTrans;

    // ──——构造方法：从 DataManager 获取数据——──

    public FloydValidator() {
        DataManager dm = DataManager.getInstance();
        Set<String> cities = dm.getCities();
        this.allSchedules = dm.getAllSchedules();
        this.n = cities.size();
        this.index = new HashMap<>();
        this.names = new String[n];

        int i = 0;
        for (String city : cities) {
            index.put(city, i);
            names[i] = city;
            i++;
        }

        initMatrices();
        fillEdges();
        floydChange(distTime, nextTime);
        floydChange(distPrice, nextPrice);
        floydChange(distTrans, nextTrans);
    }

    // ──——矩阵初始化——──

    private void initMatrices() {
        distTime = new double[n][n];
        distPrice = new double[n][n];
        distTrans = new double[n][n];
        nextTime = new int[n][n];
        nextPrice = new int[n][n];
        nextTrans = new int[n][n];

        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                distTime[u][v] = (u == v) ? 0 : Double.MAX_VALUE;
                distPrice[u][v] = (u == v) ? 0 : Double.MAX_VALUE;
                distTrans[u][v] = (u == v) ? 0 : Double.MAX_VALUE;
                nextTime[u][v] = -1;
                nextPrice[u][v] = -1;
                nextTrans[u][v] = -1;
            }
        }
    }

    // ──——填入直连边：对每对城市取最优班次——──

    private void fillEdges() {
        for (Schedule s:allSchedules) {
            Integer ui=index.get(s.getFromCity());
            Integer vi=index.get(s.getToCity());
            if (ui==null||vi==null) continue;
            int u=ui,v=vi;

            double time=s.getTravelMinutes();
            double price=s.getCost();

            // 最快：取最短旅行时间的班次
            if (time<distTime[u][v]) {
                distTime[u][v]=time;
                nextTime[u][v]=v;
            }
            // 最省钱：取最低票价的班次
            if (price<distPrice[u][v]) {
                distPrice[u][v]=price;
                nextPrice[u][v]=v;
            }
            // 最少换乘：复合权重 = 1.0 + travelTime/1e6（换乘优先，时间决胜）
            double compWeight = 1.0 + time / 1_000_000.0;
            if (compWeight < distTrans[u][v]) {
                distTrans[u][v] = compWeight;
                nextTrans[u][v] = v;
            }
        }
    }

    /** Floyd，double 权重（时间、费用） */
    private void floydChange(double[][] dist, int[][] next) {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] == Double.MAX_VALUE) continue;
                for (int j = 0; j < n; j++) {
                    if (dist[k][j] == Double.MAX_VALUE) continue;
                    double nd = dist[i][k] + dist[k][j];
                    if (nd < dist[i][j]) {
                        dist[i][j] = nd;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    //──——查询：返回Floyd计算出的RouteResult(cheapest无法正确计算)——──
    public RouteResult query(String from, String to, String strategy) {
        Integer ui = index.get(from);
        Integer vi = index.get(to);
        if (ui == null || vi == null) return unreachable();
        int u = ui, v = vi;

        // 同城
        if (u == v) return unreachable();

        int[][] next = switch (strategy) {
            case "cheapest"   -> nextPrice;
            case "convenient" -> nextTrans;
            default           -> nextTime;
        };

        // 可达性检查
        double d = switch (strategy) {
            case "cheapest"   -> distPrice[u][v];
            case "convenient" -> distTrans[u][v];
            default           -> distTime[u][v];
        };
        if (d >= Double.MAX_VALUE / 2) return unreachable();

        // ── 回溯路径：沿 next 矩阵收集途径城市的班次 ──
        List<Schedule> schedulePath = new ArrayList<>();
        int cur = u;
        int safety = 0;

        while (cur != v && safety < n + 10) {
            int nxt = next[cur][v];
            if (nxt < 0 || nxt == cur) break;   // 无效或自环

            Schedule shortestEdge = findShortestEdge(names[cur], names[nxt], strategy);
            if (shortestEdge == null) {              // 找不到对应班次 → 不可达
                return unreachable();
            }
            schedulePath.add(shortestEdge);
            cur = nxt;
            safety++;
        }

        if (schedulePath.isEmpty() || cur != v) return unreachable();

        // ── 构建 RouteStep 序列（计算实际等候时间） ──
        List<RouteStep> steps = new ArrayList<>();
        for (int i = 0; i < schedulePath.size(); i++) {
            Schedule s = schedulePath.get(i);
            if (i == 0) {
                steps.add(new RouteStep(s, s.getDepartureTime(),
                        s.getDepartureTime() + s.getTravelMinutes(), 0));
            } else {
                RouteStep prev = steps.get(i - 1);
                int prevArr = prev.getArrivalAbsolute();
                int wait = s.getWaitMinutes(prevArr % 1440);
                int depAbs = prevArr + wait;
                int arrAbs = depAbs + s.getTravelMinutes();
                steps.add(new RouteStep(s, depAbs, arrAbs, wait));
            }
        }

        // ── 汇总 ──
        int totalTime = steps.get(steps.size() - 1).getArrivalAbsolute()
                      - steps.get(0).getDepartureAbsolute();
        double totalCost = 0;
        for (Schedule s : schedulePath) totalCost += s.getCost();

        RouteResult rr = new RouteResult();
        rr.setSteps(steps);
        rr.setTotalTimeMinutes(totalTime);
        rr.setTotalCost(totalCost);
        return rr;
    }

    /** 在两城市间找最优班次（按策略） */
    private Schedule findShortestEdge(String from, String to, String strategy) {
        Schedule best = null;
        double bestVal = Double.MAX_VALUE;

        for (Schedule s : allSchedules) {
            if (!s.getFromCity().equals(from) || !s.getToCity().equals(to)) continue;
            double val = switch (strategy) {
                case "cheapest"   -> s.getCost();
                case "convenient" -> s.getTravelMinutes();  // 换乘次数相同时选时间最短
                default           -> s.getTravelMinutes();
            };
            if (val < bestVal) { bestVal = val; best = s; }
        }
        return best;
    }

    private RouteResult unreachable() {
        RouteResult r = new RouteResult();
        r.setTotalTimeMinutes(-1);
        r.setTotalCost(-1);
        return r;
    }

    // ── 交叉验证 ──

    public String validate(String from, String to, String strategy) {
        try {
            return doValidate(from, to, strategy);
        } catch (Exception e) {
            return "====== 验证异常 ======\n"
                 + "策略: " + strategy + ", " + from + " > " + to + "\n"
                 + "错误: " + e.getClass().getSimpleName() + ": " + e.getMessage() + "\n";
        }
    }

    private String doValidate(String from, String to, String strategy) {
        RouteResult dijk = RouteFinder.findRoute(from, to, strategy, (Set<Schedule.Type>) null);
        RouteResult floyd = query(from, to, strategy);

        String label = switch (strategy) {
            case "cheapest" -> "💰 最省钱"; case "convenient" -> "🔄 最少换乘"; default -> "⏱ 最快到达";
        };

        StringBuilder rpt = new StringBuilder();
        rpt.append("====== ").append(label).append(" 交叉验证 ======\n");

        // ── Floyd 结果 ──
        int fTrans = 0;
        if (!floyd.isReachable()) {
            rpt.append("Floyd   : × 无法到达\n");
        } else {
            fTrans = Math.max(0, floyd.getSteps().size() - 1);
            rpt.append("Floyd   : 总耗时=").append(floyd.getFormattedTotalTime())
               .append(", 费用=¥").append(String.format("%.0f", floyd.getTotalCost()))
               .append(", 换乘=").append(fTrans).append("次\n");
        }

        // ── Dijkstra 结果 ──
        int dTrans = 0;
        if (!dijk.isReachable()) {
            rpt.append("Dijkstra: × 无法到达\n");
        } else {
            dTrans = Math.max(0, dijk.getSteps().size() - 1);
            rpt.append("Dijkstra: 总耗时=").append(dijk.getFormattedTotalTime())
               .append(", 费用=¥").append(String.format("%.0f", dijk.getTotalCost()))
               .append(", 换乘=").append(dTrans).append("次\n");
        }

        // ── 逐策略比较 ──
        if (floyd.isReachable() && dijk.isReachable()) {
            rpt.append("------------------------------\n");

            if (strategy.equals("cheapest")) {
                boolean costOk = Math.abs(floyd.getTotalCost() - dijk.getTotalCost()) < 0.01;
                rpt.append(costOk ? "√ 费用一致，算法正确\n"
                                  : "× 费用不一致！偏差=¥"
                                    + String.format("%.2f", Math.abs(floyd.getTotalCost() - dijk.getTotalCost())) + "\n");
                rpt.append("   费用偏差 = ¥").append(String.format("%.2f",
                        Math.abs(floyd.getTotalCost() - dijk.getTotalCost()))).append("\n");

            } else if (strategy.equals("fastest")) {
                // Floyd 优化纯旅行时间；Dijkstra 优化旅行+实际等候。
                // 正确比较：抽取纯旅行时间（不含等候）对比。
                int floydPure = pureTravelTime(floyd);
                int dijkPure = pureTravelTime(dijk);

                rpt.append("   纯旅行时间: Floyd=").append(fmtMin(floydPure))
                   .append(", Dijkstra=").append(fmtMin(dijkPure)).append("\n");
                rpt.append("   含等候总时: Floyd=").append(floyd.getFormattedTotalTime())
                   .append(", Dijkstra=").append(dijk.getFormattedTotalTime()).append("\n");

                if (floydPure <= dijkPure) {
                    rpt.append("√ Floyd 纯旅行时间 ≤ Dijkstra\n");
                } else {
                    rpt.append("× Floyd 纯旅行时间 > Dijkstra（异常）\n");
                    rpt.append("   纯旅行时间差=").append(floydPure - dijkPure).append("分钟\n");
                    rpt.append("   可能原因：fillEdges 未取到真正最短班次，或数据不一致\n");
                }

            } else { // convenient
                // Floyd 最小化边数（复合权重 1.0+time/1e6），Dijkstra 最小化边数+总耗时
                // Floyd 无法建模等候时间，因此同一换乘数下总耗时可能高于 Dijkstra（属正常）
                int floydPure = pureTravelTime(floyd);
                int dijkPure = pureTravelTime(dijk);

                rpt.append("   Floyd 最少边数=").append(fTrans).append("跳")
                   .append(", Dijkstra=").append(dTrans).append("换乘\n");
                rpt.append("   纯旅行时间: Floyd=").append(fmtMin(floydPure))
                   .append(", Dijkstra=").append(fmtMin(dijkPure)).append("\n");
                rpt.append("   含等候总时: Floyd=").append(floyd.getFormattedTotalTime())
                   .append(", Dijkstra=").append(dijk.getFormattedTotalTime()).append("\n");

                if (fTrans < dTrans) {
                    rpt.append("√ Floyd 边数更少（Dijkstra 未找到更优中转）\n");
                } else if (fTrans == dTrans) {
                    rpt.append("√ 最少换乘次数一致（Floyd=验证下界，Dijkstra=实际可行解）\n");
                } else {
                    rpt.append("⚠ Floyd 边数 > Dijkstra（异常）\n");
                }
            }
        } else if (floyd.isReachable() != dijk.isReachable()) {
            rpt.append("------------------------------\n");
            if (strategy.equals("convenient") && !dijk.isReachable()) {
                rpt.append("√ 正常：Dijkstra 未找到可达路径，而 Floyd 可搜索中转\n");
            } else {
                rpt.append("⚠ Floyd 与 Dijkstra 可达性不一致，请检查\n");
            }
        }

        return rpt.toString();
    }

    /** 从 RouteResult 中提取纯旅行时间（不含中转等候） */
    private static int pureTravelTime(RouteResult r) {
        int total = 0;
        for (RouteStep step : r.getSteps()) {
            total += step.getSchedule().getTravelMinutes();
        }
        return total;
    }

    /** 分钟标准化为"XhYm"简单格式 */
    private static String fmtMin(int m) {
        int h = m / 60;
        int min = m % 60;
        return min == 0 ? h + "h" : h + "h" + min + "m";
    }

    private static final int BOX_W = 39;

    public String validateAll(String from, String to) {
        StringBuilder sb = new StringBuilder();

        // 顶部边框
        sb.append('+');
        for (int i = 0; i < BOX_W; i++) sb.append('=');
        sb.append("+\n");

        // 标题行
        sb.append("|  Floyd & Dijkstra 交叉验证报告");
        int titleLen = "Floyd & Dijkstra 交叉验证报告".length();
        int titlec = 6;
        int titleDisp = titleLen + titlec;
        int pad = BOX_W - 2 - titleDisp;
        for (int i = 0; i < pad; i++) sb.append(' ');
        sb.append("  |\n");

        String route = from + " > " + to;
        sb.append("|           ").append(route);

        int routec= count(from) + count(to);
        int actualDisp = route.length() + routec;
        int rpad = BOX_W - 12 - actualDisp;        
        if (rpad < 0) rpad = 0;
        for (int i = 0; i < rpad; i++) sb.append(' ');
        sb.append("  |\n");

        // 底部边框
        sb.append('+');
        for (int i = 0; i < BOX_W; i++) sb.append('=');
        sb.append("+\n\n");

        sb.append(validate(from, to, "fastest")).append("\n");
        sb.append(validate(from, to, "cheapest")).append("\n");
        sb.append(validate(from, to, "convenient"));
        return sb.toString();
    }

    /** 粗略统计字符串中的字符数 */
    private static int count(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0x4E00 && c <= 0x9FFF) n++;
        }
        return n;
    }

    /** 初始化失败 */
    public static void printValidation(String from, String to) {
        try {
            System.out.println(new FloydValidator().validateAll(from, to));
        } catch (Exception e) {
            System.out.println("+=======================================+");
            System.out.println("|  Floyd 验证器初始化失败                |");
            String msg = e.getClass().getSimpleName() + ": "
                    + (e.getMessage() != null ? e.getMessage() : "");
            System.out.println("|  " + msg);
            System.out.println("+=======================================+");
        }
    }
}
