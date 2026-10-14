import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;


public class ChatPane extends VBox {

    private VBox msgBox;
    private ScrollPane scrollPane;
    private TextField inputField;
    private Button sendBtn;
    private ToggleButton dbToggle, webToggle;
    private LLM llm;
    private boolean waiting = false;

    public ChatPane() {
        llm = new LLM();

        setSpacing(10);
        setPadding(new Insets(15, 20, 15, 20));
        setStyle("-fx-background-color: #f5f5f5;");
        setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("💬 AI 助手");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        getChildren().add(title);

        Pane sep1 = new Pane();
        sep1.setPrefHeight(1);
        sep1.setMaxWidth(Double.MAX_VALUE);
        sep1.setStyle("-fx-background-color: #ddd;");
        getChildren().add(sep1);

        //开关行：数据库/联网搜索
        HBox toggleRow = new HBox(10);
        toggleRow.setAlignment(Pos.CENTER_LEFT);

        dbToggle = new ToggleButton("🗄 数据库");
        dbToggle.setPrefSize(110, 32);
        dbToggle.setSelected(true);
        dbToggle.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 16;");
        dbToggle.selectedProperty().addListener((obs, old, sel) -> {
            if (sel) dbToggle.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
                "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 16;");
            else     dbToggle.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #777; " +
                "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 16;");
        });

        webToggle = new ToggleButton("🌐 联网搜索");
        webToggle.setPrefSize(120, 32);
        webToggle.setSelected(true);
        webToggle.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 16;");
        webToggle.selectedProperty().addListener((obs, old, sel) -> {
            if (sel) webToggle.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
                "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 16;");
            else     webToggle.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #777; " +
                "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 16;");
        });

        Pane toggleSpacer = new Pane();
        toggleRow.getChildren().addAll(dbToggle, webToggle, toggleSpacer);
        getChildren().add(toggleRow);

        // ---- 消息区 ----
        msgBox = new VBox(10);
        msgBox.setPadding(new Insets(8, 4, 8, 4));

        scrollPane = new ScrollPane(msgBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        getChildren().add(scrollPane);

        // 欢迎消息
        Label welcome = new Label("你好！我是交通出行助手。\n可以帮你规划路线、查询车次、比较价格。");
        welcome.setWrapText(true);
        welcome.setMaxWidth(450);
        welcome.setPadding(new Insets(10, 16, 10, 16));
        welcome.setStyle("-fx-background-color: white; -fx-text-fill: #333; " +
            "-fx-font-size: 14; -fx-background-radius: 4 16 16 16; -fx-border-color: #e8e8e8; -fx-border-width: 1;");
        HBox welcomeRow = new HBox(welcome);
        welcomeRow.setAlignment(Pos.CENTER_LEFT);
        msgBox.getChildren().add(welcomeRow);

        // ---- 输入区 ----
        HBox inputRow = new HBox(8);
        inputRow.setAlignment(Pos.CENTER);

        inputField = new TextField();
        inputField.setPromptText("输入消息...");
        inputField.setPrefHeight(42);
        inputField.setStyle("-fx-font-size: 14; -fx-background-radius: 20; -fx-border-radius: 20; " +
            "-fx-border-color: #ddd; -fx-background-color: white; -fx-padding: 0 16;");
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputField.setOnAction(e -> send());

        sendBtn = new Button("发送");
        sendBtn.setPrefSize(70, 42);
        sendBtn.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 14; -fx-font-weight: bold; -fx-background-radius: 20;");
        sendBtn.setOnAction(e -> send());

        inputRow.getChildren().addAll(inputField, sendBtn);
        getChildren().add(inputRow);
    }

    void send() {
        String text = inputField.getText().trim();
        if (text.isEmpty() || waiting) return;
        inputField.clear();

        // 用户气泡（右对齐，青色）
        Label userBubble = new Label(text);
        userBubble.setWrapText(true);
        userBubble.setMaxWidth(400);
        userBubble.setPadding(new Insets(10, 16, 10, 16));
        userBubble.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 14; -fx-background-radius: 16 4 16 16;");
        HBox userRow = new HBox(userBubble);
        userRow.setAlignment(Pos.CENTER_RIGHT);
        msgBox.getChildren().add(userRow);

        waiting = true;
        sendBtn.setDisable(true);
        inputField.setDisable(true);

        // 机器人占位气泡
        Label botBubble = new Label("思考中...");
        botBubble.setWrapText(true);
        botBubble.setMaxWidth(450);
        botBubble.setPadding(new Insets(10, 16, 10, 16));
        botBubble.setStyle("-fx-background-color: white; -fx-text-fill: #333; " +
            "-fx-font-size: 14; -fx-background-radius: 4 16 16 16; -fx-border-color: #e8e8e8; -fx-border-width: 1;");
        HBox botRow = new HBox(botBubble);
        botRow.setAlignment(Pos.CENTER_LEFT);
        msgBox.getChildren().add(botRow);

        boolean doDB  = dbToggle.isSelected();
        boolean doWeb = webToggle.isSelected();

        new Thread(() -> {
            // 数据库搜索（分词匹配）
            String dbResult = "";
            if (doDB) {
                dbResult = searchDB(text);
            }

            // 联网搜索
            String webResult = "";
            if (doWeb) {
                Platform.runLater(() -> botBubble.setText("🔍 联网搜索中..."));
                webResult = LLM.searchWeb(text + " 12306 火车票 时刻表");
            }

            // —— 检测用户偏好的交通方式 ——
            int wayPref = detectWayFilter(text);
            String wayPrefName = switch (wayPref) {
                case 1 -> "公路"; case 2 -> "高铁"; case 3 -> "飞机";
                case 4 -> "航运"; case 5 -> "普速"; default -> "";
            };

            // —— 组装 prompt（只用DB检索结果，LLM自己分析路线） ——
            StringBuilder prompt = new StringBuilder();
            prompt.append("你是「畅行交通」AI助手。下面是数据库中匹配到的交通班次，请你根据这些数据分析并推荐最佳出行方案。\n");

            if (!wayPrefName.isEmpty()) {
                prompt.append("【重要】用户只想看").append(wayPrefName).append("，")
                     .append("请只介绍").append(wayPrefName).append("的路线，忽略其他交通方式。")
                     .append("高铁车次以G或D开头，普速列车以Z、T、K开头，飞机航班号通常为字母+数字。\n");
            }
            prompt.append("要求：逐条列出车次/航班号、出发到达时间、票价、耗时。")
                 .append("如果涉及换乘请说明换乘方案。")
                 .append("必须给出具体可执行的方案。\n\n");

            if (!dbResult.isEmpty()) {
                prompt.append("## 数据库匹配结果\n").append(dbResult).append("\n\n");
            }
            if (!webResult.isEmpty()) {
                prompt.append("## 联网搜索结果\n").append(webResult).append("\n\n");
            }

            prompt.append("用户问题：").append(text);

            Platform.runLater(() -> botBubble.setText("思考中..."));
            StringBuilder full = new StringBuilder();

            try {
                llm.chatStream(prompt.toString(), token -> {
                    full.append(token);
                    Platform.runLater(() -> botBubble.setText(full.toString().trim()));
                });
            } catch (Exception ex) {
                Platform.runLater(() -> botBubble.setText("抱歉，连接失败: " + ex.getMessage()));
            }
            Platform.runLater(() -> {
                waiting = false;
                sendBtn.setDisable(false);
                inputField.setDisable(false);
                inputField.requestFocus();

            });
        }).start();
    }

    /** 从用户输入中检测偏好的交通方式（0=不限） */
    int detectWayFilter(String query) {
        if (query.contains("高铁") || query.contains("动车")) return 2;
        if (query.contains("普速") || query.contains("普通") || query.contains("慢车") || query.contains("火车")) return 5;
        if (query.contains("飞机") || query.contains("航班")) return 3;
        if (query.contains("公路") || query.contains("大巴") || query.contains("客车")) return 1;
        if (query.contains("航运") || query.contains("轮船")) return 4;
        return 0; // 不限
    }

    /** 从数据库搜索匹配路线（分词匹配，最多15条） */
    String searchDB(String query) {
        // 将用户输入拆成关键词，逐个匹配城市名和车次号
        String[] keywords = query.split("[\\s，。！？、,!.?：:；;]+");
        int wayFilter = detectWayFilter(query);   // 0=不限, 其他=只要该类型
        Set<String> seen = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (Route r : Main.Routes) {
            if (count >= 15) break;

            // 交通方式过滤：如果用户指定了类型，只返回该类型
            if (wayFilter != 0 && r.way != wayFilter) continue;

            // 去重：同一 from→to+车次 只出现一次
            String key = r.from + "→" + r.to + "|" + r.number;
            if (seen.contains(key)) continue;

            boolean match = false;
            for (String kw : keywords) {
                if (kw.isEmpty()) continue;
                // 城市名（关键词包含城市名即可命中）、车次号匹配
                if (kw.contains(r.from) || kw.contains(r.to) || r.number.contains(kw)) {
                    match = true;
                    break;
                }
            }

            if (match) {
                seen.add(key);
                String wayName;
                if      (r.way == 1) wayName = "公路";
                else if (r.way == 2) wayName = "高铁";
                else if (r.way == 3) wayName = "飞机";
                else if (r.way == 4) wayName = "航运";
                else                 wayName = "普速";
                sb.append("- ").append(r.from).append("→").append(r.to).append("  ")
                  .append(wayName).append(" ").append(r.number)
                  .append("  ¥").append(r.price).append("  ").append(r.duration);
                if (r.departure_time != null) sb.append("  ").append(r.departure_time).append("发");
                sb.append("\n");
                count++;
            }
        }
        return sb.toString();
    }

    }


