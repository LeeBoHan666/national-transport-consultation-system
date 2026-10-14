import com.google.gson.*;
import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.paint.Color;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.beans.property.SimpleStringProperty;
import java.sql.*;


public class GUI extends Application {
    // 经纬度范围（和 cn.svg 的投影对齐）
    static final double MIN_LON = 69.0;
    static final double MAX_LON = 138.0;
    static final double MIN_LAT = 17.5;
    static final double MAX_LAT = 54.0;
    static final double mapH = 590;   // 保持 1000:738 ≈ 800:590 的比例
    static final double mapW = 800;

    Button FromButton, ToButton;
    Runnable render;
    List<Route> activeRoutes = null;  // 当前选中方案，null=显示全部
    double scale = 1.0, offsetX = 0, offsetY = 0;
    double sx = 1, sy = 1;   // Canvas 实际尺寸 / 地图原始尺寸
    double lastMouseX, lastMouseY;
    boolean hasDragged = false;
    List<double[][]> provinces = new ArrayList<>();  // 省界多边形

    public void start(Stage stage) throws Exception
    {


        BorderPane MainPanel = new BorderPane();
        Scene MainScene = new Scene(MainPanel,1200,880);

        VBox MenuBox = new VBox(25);
        Button MainButton = new Button("✈ 主界面");
        Button ChatButton = new Button("💬 聊天");
        Button SearchButton=new Button("🔍 检索");
        Button SQLButton = new Button("🗄 数据库");
        Button SetButton = new Button("⚙ 设置");

        MainButton.setPrefSize(150,40);
        ChatButton.setPrefSize(150,40);
        SearchButton.setPrefSize(150,40);
        SQLButton.setPrefSize(150,40);
        SetButton.setPrefSize(150,40);
        MainButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                "-fx-text-fill:white;-fx-background-radius: 6px;");
        ChatButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                "-fx-text-fill:white;-fx-background-radius: 6px;");
        SearchButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                "-fx-text-fill:white;-fx-background-radius: 6px;");
        SQLButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                "-fx-text-fill:white;-fx-background-radius: 6px;");
        SetButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                "-fx-text-fill:white;-fx-background-radius: 6px;");

        MenuBox.setStyle("-fx-background-color:  #222831 ;  ");

        Label HelloLabel = new Label("\n🚆\n"+SQL.getUsername()+" , 您好\n ——————");
        HelloLabel.setStyle("-fx-text-fill:white;-fx-font-size: 30;-fx-text-alignment: center;");

        if(SQL.getRole().equals("admin"))
            MenuBox.getChildren().addAll(HelloLabel,MainButton,ChatButton,SearchButton,SQLButton,SetButton);

        else
            MenuBox.getChildren().addAll(HelloLabel,MainButton,ChatButton,SearchButton,SetButton);

        VBox.setMargin(MainButton, new Insets(20, 30, 0, 30));
        VBox.setMargin(ChatButton, new Insets(10, 30, 0, 30));
        VBox.setMargin(SearchButton, new Insets(10, 30, 0, 30));
        VBox.setMargin(SQLButton, new Insets(10, 30, 0, 30));
        VBox.setMargin(SetButton, new Insets(10, 30, 0, 30));
        MainPanel.setLeft(MenuBox);


        VBox MainPane = new VBox(15);

        MainPane.setPadding(new Insets(15, 20, 25, 20));
        Label MainLabel = new Label("路线查询");
        MainLabel.setStyle("-fx-font-size: 25;-fx-font-weight: bold;");
        MainPane.getChildren().add(MainLabel);


        Pane greyLine = new Pane();
        greyLine.setPrefHeight(1);
        greyLine.setStyle("-fx-background-color: #dddddd;");
        MainPane.getChildren().add(greyLine);

        HBox InputBox = new HBox(10);
        InputBox.setMaxWidth(Double.MAX_VALUE);

        FromButton = new Button("From");
        FromButton.setPrefSize(200,30);
        FromButton.setStyle("-fx-background-color: white;-fx-border-radius: 6px;" +
                "-fx-border-color: silver;-fx-background-radius: 6px;-fx-font-size: 16");
        FromButton.setOnAction(event -> {
           Popup getFromPopup = new Popup();
           getFromPopup.setAutoHide(true);
           VBox FromBox = new VBox(12);
           FromBox.setAlignment(Pos.CENTER);
           FromBox.setPadding(new Insets(10, 25, 10, 25));
           FromBox.setPrefSize(300,350);
           FromBox.setStyle("-fx-background-color: white;-fx-border-color: #ddd;-fx-background-radius: 6px;" +
                   "-fx-border-radius: 6px;-fx-border-width: 1px");

           TextField FromTextField = new TextField();
           FromTextField.setMaxWidth(Double.MAX_VALUE);
           FromTextField.setPrefHeight(35);
           FromTextField.setPromptText("搜索城市...");
           FromTextField.setStyle("-fx-background-color: white; -fx-font-size: 14; -fx-border-color: #00adb5; -fx-border-radius: 4;");
           FromBox.getChildren().add(FromTextField);

           TableView<City> fromTable = new TableView<>();
           fromTable.setMaxWidth(Double.MAX_VALUE);
           fromTable.setPrefHeight(200);
           fromTable.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1;");
           fromTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

           TableColumn<City, String> colName1 = new TableColumn<>("城市");
           colName1.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().name));
           fromTable.getColumns().add(colName1);

           ObservableList<City> fromCities = FXCollections.observableArrayList(Main.Cities);
           fromTable.setItems(fromCities);
           FromBox.getChildren().add(fromTable);

           // 点表格行 → 填入搜索框
           fromTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
               if (newVal != null) FromTextField.setText(newVal.name);
           });

           FromTextField.textProperty().addListener((obs, oldVal, newVal) -> {
               if (newVal == null || newVal.isEmpty()) {
                   fromTable.setItems(fromCities);
               } else {
                   ObservableList<City> filtered = FXCollections.observableArrayList();
                   for (City c : Main.Cities) {
                       if (c.name.contains(newVal)) filtered.add(c);
                   }
                   fromTable.setItems(filtered);
               }
           });

           HBox ChoiceBox = new HBox(10);
           ChoiceBox.setAlignment(Pos.CENTER);
           Button CancelButton = new Button("取消");
           CancelButton.setPrefSize(120,35);
           Button SureButton = new Button("确定");
           SureButton.setPrefSize(120,35);
           SureButton.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; -fx-font-weight: bold;");

           CancelButton.setOnAction(e->{ getFromPopup.hide(); });
           SureButton.setOnAction(e ->{
               String name = FromTextField.getText().trim();
               if (!name.isEmpty()) {
                   Main.From = name;
                   FromButton.setText(Main.From);
               }
               getFromPopup.hide();
           });

           ChoiceBox.getChildren().addAll(CancelButton,SureButton);
           FromBox.getChildren().add(ChoiceBox);
           getFromPopup.getContent().add(FromBox);
           getFromPopup.show(stage);
        });

        Button SwapButton = new Button("🔀");
        SwapButton.setPrefSize(100,30);
        SwapButton.setStyle("-fx-background-color: white;-fx-border-radius: 6px;" +
                "-fx-border-color: white;-fx-background-radius: 6px;-fx-font-size: 16;");


        ToButton = new Button("To");

        SwapButton.setOnAction(e -> {
            String temp = Main.From;
            Main.From = Main.To;
            Main.To = temp;
            FromButton.setText(Main.From != null ? Main.From : "From");
            ToButton.setText(Main.To != null ? Main.To : "To");
        });



        ToButton.setPrefSize(200,30);
        ToButton.setStyle("-fx-background-color: white;-fx-border-radius: 6px;" +
                "-fx-border-color: silver;-fx-background-radius: 6px;-fx-font-size: 16;");
        ToButton.setOnAction(event -> {

           Popup getToPopup = new Popup();
           getToPopup.setAutoHide(true);
           VBox ToBox = new VBox(12);
           ToBox.setAlignment(Pos.CENTER);
           ToBox.setPadding(new Insets(10, 25, 10, 25));
           ToBox.setPrefSize(300,350);
           ToBox.setStyle("-fx-background-color: white;-fx-border-color: #ddd;-fx-background-radius: 6px;" +
                   "-fx-border-radius: 6px;-fx-border-width: 1px");

           TextField ToTextField = new TextField();
           ToTextField.setMaxWidth(Double.MAX_VALUE);
           ToTextField.setPrefHeight(35);
           ToTextField.setPromptText("搜索城市...");
           ToTextField.setStyle("-fx-background-color: white; -fx-font-size: 14; -fx-border-color: #00adb5; -fx-border-radius: 4;");
           ToBox.getChildren().add(ToTextField);

           TableView<City> toTable = new TableView<>();
           toTable.setMaxWidth(Double.MAX_VALUE);
           toTable.setPrefHeight(200);
           toTable.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1;");
           toTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

           TableColumn<City, String> colName2 = new TableColumn<>("城市");
           colName2.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().name));
           toTable.getColumns().add(colName2);

           ObservableList<City> toCities = FXCollections.observableArrayList(Main.Cities);
           toTable.setItems(toCities);
           ToBox.getChildren().add(toTable);

           toTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
               if (newVal != null) ToTextField.setText(newVal.name);
           });

           ToTextField.textProperty().addListener((obs, oldVal, newVal) -> {
               if (newVal == null || newVal.isEmpty()) {
                   toTable.setItems(toCities);
               } else {
                   ObservableList<City> filtered = FXCollections.observableArrayList();
                   for (City c : Main.Cities) {
                       if (c.name.contains(newVal)) filtered.add(c);
                   }
                   toTable.setItems(filtered);
               }
           });

           HBox ChoiceBox = new HBox(10);
           ChoiceBox.setAlignment(Pos.CENTER);
           Button CancelButton = new Button("取消");
           CancelButton.setPrefSize(120,30);
           Button SureButton = new Button("确定");
           SureButton.setPrefSize(120,30);
           SureButton.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; -fx-font-weight: bold;");

           CancelButton.setOnAction(e->{ getToPopup.hide(); });
           SureButton.setOnAction(e ->{
               String name = ToTextField.getText().trim();
               if (!name.isEmpty()) {
                   Main.To = name;
                   ToButton.setText(Main.To);
               }
               getToPopup.hide();
           });

           ChoiceBox.getChildren().addAll(CancelButton,SureButton);
           ToBox.getChildren().add(ChoiceBox);
           getToPopup.getContent().add(ToBox);
           getToPopup.show(stage);
        });

        Button CheckButton = new Button("查询");
        CheckButton.setPrefSize(100,30);
        CheckButton.setStyle("-fx-background-color: #00adb5;-fx-text-fill: white;-fx-border-radius: 6px;");






        InputBox.getChildren().addAll(FromButton,SwapButton,ToButton);
        Pane p1=new Pane();
        HBox.setHgrow(p1, Priority.ALWAYS);
        InputBox.getChildren().addAll(p1,CheckButton);
        MainPane.getChildren().add(InputBox);

        Pane MapPane = new Pane();
        MapPane.setMinHeight(mapH);
        Canvas MapCanvas = new Canvas(mapW, mapH);
        MapCanvas.widthProperty().bind(MapPane.widthProperty());
        MapCanvas.heightProperty().bind(MapPane.heightProperty());

        loadProvinces();

        render = () -> {
            double cw = MapCanvas.getWidth();
            double ch = MapCanvas.getHeight();
            if (cw <= 0 || ch <= 0) { cw = mapW; ch = mapH; }
            sx = cw / mapW;   // 自适应缩放（保存为字段，点击要用）
            sy = ch / mapH;

            GraphicsContext gc = MapCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, cw, ch);

            gc.save();
            gc.scale(sx * scale, sy * scale);
            gc.translate(offsetX, offsetY);

            gc.setFill(Color.web("#f0f0e8"));
            gc.fillRect(0, 0, mapW, mapH);

            for (double[][] poly : provinces) {
                gc.setFill(Color.web("#d8e8d8"));
                gc.setStroke(Color.web("#c0d0c0"));
                gc.setLineWidth(0.5 / scale);
                gc.fillPolygon(poly[0], poly[1], poly[0].length);
                gc.strokePolygon(poly[0], poly[1], poly[0].length);
            }


            // 当前选中方案的路线集合（用于过滤）
            // 当前选中方案的路线集合（按车次号+起止匹配）
          Set<String> activeSet = null;
            if (activeRoutes != null) {
                activeSet = new HashSet<>();
                for (Route r : activeRoutes)
                    activeSet.add(r.number + "|" + r.from + "|" + r.to);
            }

            Set<String> drawn = new HashSet<>();
            for(Route r : Main.Routes){
                if (activeSet != null && !activeSet.contains(r.number + "|" + r.from + "|" + r.to)) continue;

                City fromCity = Main.cityByName(r.from);
                City toCity = Main.cityByName(r.to);
                if (fromCity == null || toCity == null) continue;

                String key = r.from.compareTo(r.to) < 0
                    ? r.from + "→" + r.to
                    : r.to + "→" + r.from;
                if (drawn.contains(key)) continue;
                drawn.add(key);

                if (activeSet != null) {
                    gc.setStroke(Color.web("#00adb5"));
                    gc.setLineWidth(2.5 / scale);
                } else {
                    if (r.way == 2)      gc.setStroke(Color.web("#00adb5"));
                    else if (r.way == 3) gc.setStroke(Color.web("#e8a400"));
                    else if (r.way == 4) gc.setStroke(Color.web("#4488cc"));
                    else if (r.way == 5) gc.setStroke(Color.web("#8899aa"));
                    else                 gc.setStroke(Color.web("#55aa55"));
                    gc.setLineWidth(0.8 / scale);
                }
                gc.strokeLine(
                    toX(fromCity.longitude), toY(fromCity.latitude),
                    toX(toCity.longitude),   toY(toCity.latitude)
                );
            }



            for(City c : Main.Cities){
                double cx = toX(c.longitude);
                double cy = toY(c.latitude);
                boolean hl = c.name.equals(Main.From) || c.name.equals(Main.To);

                if (hl) {
                    gc.setFill(Color.web("#00adb5", 0.4));
                    gc.fillOval(cx - 10, cy - 10, 20, 20);
                    gc.setFill(Color.web("#00adb5"));
                    gc.fillOval(cx - 5, cy - 5, 10, 10);
                    gc.setFill(Color.web("#2c3e50"));
                    gc.setFont(javafx.scene.text.Font.font("Microsoft YaHei", 11));
                    gc.fillText(c.name, cx + 7, cy - 4);
                } else {
                    gc.setFill(Color.rgb(230,160,160,0.4));
                    gc.fillOval(cx - 5, cy - 5, 10, 10);
                    gc.setFill(Color.RED);
                    gc.fillOval(cx - 3, cy - 3, 6, 6);
                    gc.setFill(Color.web("#333"));
                    gc.setFont(javafx.scene.text.Font.font("Microsoft YaHei", 9));
                    gc.fillText(c.name, cx + 5, cy + 3);
                }
            }

            gc.restore();

            int lx = 12, ly = 12;
            gc.setFont(javafx.scene.text.Font.font("Microsoft YaHei", 12));
            String[][] legend = {
                {"#55aa55", "公路"}, {"#00adb5", "高铁"}, {"#e8a400", "飞机"},
                {"#4488cc", "航运"}, {"#8899aa", "普速"}
            };
            for (int i = 0; i < legend.length; i++) {
                int y = ly + i * 18;
                gc.setStroke(Color.web(legend[i][0]));
                gc.setLineWidth(3);
                gc.strokeLine(lx, y + 6, lx + 20, y + 6);
                gc.setFill(Color.web("#333"));
                gc.fillText(legend[i][1], lx + 26, y + 10);
            }
        };

        MapCanvas.setOnScroll(e -> {
            double mx = e.getX();
            double my = e.getY();
            double ns = Math.max(0.5, Math.min(5.0, scale * (e.getDeltaY() > 0 ? 1.2 : 0.8)));
            if (ns == scale) return;
            offsetX += mx / sx * (1.0 / ns - 1.0 / scale);
            offsetY += my / sy * (1.0 / ns - 1.0 / scale);
            scale = ns;
            render.run();
        });

        MapCanvas.setOnMousePressed(e -> {
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
            hasDragged = false;
        });
        MapCanvas.setOnMouseDragged(e -> {
            offsetX += (e.getSceneX() - lastMouseX) / (sx * scale);
            offsetY += (e.getSceneY() - lastMouseY) / (sy * scale);
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
            hasDragged = true;
            render.run();
        });
        MapCanvas.setOnMouseReleased(e -> {
            if (hasDragged) return;
            // screen = (world + offset) * scale  →  world = screen / scale - offset
            double wx = e.getX() / (sx * scale) - offsetX;
            double wy = e.getY() / (sy * scale) - offsetY;
            City hit = findCityAt(wx, wy);
            if (hit != null) showCityPopup(hit, stage);
        });

        MapCanvas.setVisible(true);
        MapPane.setMaxWidth(Double.MAX_VALUE);
        MapPane.setPrefHeight(mapH);
        MapPane.setMinHeight(mapH);
        MapPane.setMaxHeight(mapH);

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.widthProperty().bind(MapPane.widthProperty());
        clip.heightProperty().bind(MapPane.heightProperty());
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        MapPane.setClip(clip);

        MapPane.getChildren().add(MapCanvas);

        HBox mapWrapper = new HBox(MapPane);
        mapWrapper.setAlignment(Pos.CENTER);
        mapWrapper.setMaxWidth(Double.MAX_VALUE);
        mapWrapper.setPrefHeight(mapH + 4);
        mapWrapper.setMinHeight(mapH + 4);
        mapWrapper.setMaxHeight(mapH + 4);
        HBox.setHgrow(MapPane, Priority.ALWAYS);
        mapWrapper.setStyle("-fx-border-width: 2; -fx-border-color: #aaa; -fx-border-radius: 8; -fx-background-color: transparent;");
        MainPane.getChildren().add(mapWrapper);

        // 等布局完成（MapPane 有尺寸了）再首次渲染
        MapPane.layoutBoundsProperty().addListener((obs, old, bounds) -> {
            if (bounds.getWidth() > 0 && bounds.getHeight() > 0) {
                render.run();
            }
        });

        // 查询结果
        HBox ResultBox = new HBox(10);
        ResultBox.setAlignment(Pos.CENTER_LEFT);
        ResultBox.setMaxWidth(Double.MAX_VALUE);
        ResultBox.setPadding(new Insets(10, 2, 4, 2));

        VBox cardTime = makeResultCard("⏱ 最短耗时",  "#00adb5", "—");
        VBox cardPrice = makeResultCard("💰 最低价格",  "#e8a400", "—");
        VBox cardTrans = makeResultCard("🔄 最少换乘",  "#4488cc", "—");
        HBox.setHgrow(cardTime,  Priority.ALWAYS);
        HBox.setHgrow(cardPrice, Priority.ALWAYS);
        HBox.setHgrow(cardTrans, Priority.ALWAYS);

        ResultBox.getChildren().addAll(cardTime, cardPrice, cardTrans);
        MainPane.getChildren().add(ResultBox);

        CheckButton.setOnAction(e -> {
            if (Main.From == null || Main.From.isEmpty() || Main.To == null || Main.To.isEmpty()) return;

            // 确保 DataManager 使用最新的路线数据（静态单例在类加载时可能捕获空数据）
            DataManager.getInstance().refresh();

            RouteResult dijR1 = RouteFinder.findRoute(Main.From, Main.To, "fastest", (Schedule.Type) null);
            RouteResult dijR2 = RouteFinder.findRoute(Main.From, Main.To, "cheapest", (Schedule.Type) null);
            RouteResult dijR3 = RouteFinder.findRoute(Main.From, Main.To, "convenient", (Schedule.Type) null);

            // ── 最少换乘回退：Dijkstra 仅搜索直达，若无直达则用 Floyd 全局最少边数路径 ──
            boolean convenientFromFloyd = false;
            if (!dijR3.isReachable()) {
                FloydValidator fv = new FloydValidator();
                RouteResult floydTrans = fv.query(Main.From, Main.To, "convenient");
                if (floydTrans.isReachable()) {
                    dijR3 = floydTrans;
                    convenientFromFloyd = true;
                }
            }

            RouteQueryResult r1 = DataManager.toResult(dijR1, Main.From, Main.To, DataManager.MODE_TIME);
            RouteQueryResult r2 = DataManager.toResult(dijR2, Main.From, Main.To, DataManager.MODE_PRICE);
            RouteQueryResult r3 = DataManager.toResult(dijR3, Main.From, Main.To, DataManager.MODE_TRANS);

            int day1 = (int)(r1.totalTime / 24);
            int day2 = (int)(r2.totalTime / 24);
            int day3 = (int)(r3.totalTime / 24);
            String d1 = day1 > 0 ? "  +" + day1 + "天" : "";
            String d2 = day2 > 0 ? "  +" + day2 + "天" : "";
            String d3 = day3 > 0 ? "  +" + day3 + "天" : "";

            String transTitle = convenientFromFloyd ? "🔄 最少换乘·F" : "🔄 最少换乘";

            fillCard(cardTime,  r1, "⏱ 最短耗时", fmtDuration(r1.totalTime) + d1,
                    "¥" + (int)r1.totalPrice, r1.transfers + "换乘", "#00adb5", stage);
            fillCard(cardPrice, r2, "💰 最低价格",
                    "¥" + (int)r2.totalPrice, fmtDuration(r2.totalTime) + d2, r2.transfers + "换乘", "#e8a400", stage);
            fillCard(cardTrans, r3, transTitle,
                    r3.transfers + "次换乘", fmtDuration(r3.totalTime) + d3, "¥" + (int)r3.totalPrice, "#4488cc", stage);

            // Floyd-Warshall 交叉验证（输出到控制台）
            FloydValidator.printValidation(Main.From, Main.To);
        });

        ChatPane chatUI = new ChatPane();

        VBox SearchPane = new VBox(10);
        SearchPane.setPadding(new Insets(15, 20, 25, 20));
        SearchPane.setMaxWidth(Double.MAX_VALUE);
        SearchPane.setStyle("-fx-background-color: #f5f5f5;");

        Label searchTitle = new Label("🔍 车次 / 城市检索");
        searchTitle.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        SearchPane.getChildren().add(searchTitle);


        SearchPane.getChildren().add(getGreyLine());

        // 检索栏
        HBox searchBar = new HBox(8);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        TextField routeSearch = new TextField();
        routeSearch.setPromptText("输入车次号 如 G1 ...");
        routeSearch.setPrefHeight(38);
        routeSearch.setStyle("-fx-font-size: 14; -fx-background-radius: 8; -fx-border-radius: 8; " +
            "-fx-border-color: #00adb5; -fx-background-color: white; -fx-padding: 0 12;");
        HBox.setHgrow(routeSearch, Priority.ALWAYS);

        TextField citySearch = new TextField();
        citySearch.setPromptText("输入城市名 如 北京 ...");
        citySearch.setPrefHeight(38);
        citySearch.setPrefWidth(200);
        citySearch.setStyle("-fx-font-size: 14; -fx-background-radius: 8; -fx-border-radius: 8; " +
            "-fx-border-color: #00adb5; -fx-background-color: white; -fx-padding: 0 12;");

        Button searchClear = new Button("清除");
        searchClear.setPrefSize(60, 38);
        searchClear.setStyle("-fx-background-color: #e0e0e0; -fx-font-size: 13; -fx-background-radius: 8;");
        searchClear.setOnAction(e -> {
            routeSearch.clear(); citySearch.clear();
        });

        searchBar.getChildren().addAll(routeSearch, citySearch, searchClear);
        SearchPane.getChildren().add(searchBar);

        // 结果表格
        TableView<Route> resultTable = new TableView<>();
        resultTable.setMaxWidth(Double.MAX_VALUE);
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        resultTable.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; " +
            "-fx-background-radius: 8; -fx-border-radius: 8;");
        VBox.setVgrow(resultTable, Priority.ALWAYS);

        TableColumn<Route, String> rColID = new TableColumn<>("ID");
        rColID.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().RouteID));
        TableColumn<Route, String> rColFrom = new TableColumn<>("出发");
        rColFrom.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().from));
        TableColumn<Route, String> rColTo = new TableColumn<>("到达");
        rColTo.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().to));
        TableColumn<Route, String> rColNum = new TableColumn<>("车次");
        rColNum.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().number));
        TableColumn<Route, String> rColWay = new TableColumn<>("方式");
        rColWay.setCellValueFactory(cell -> {
            String w = switch (cell.getValue().way) {
                case 1 -> "公路"; case 2 -> "高铁"; case 3 -> "飞机"; case 4 -> "航运"; default -> "普速";
            };
            return new javafx.beans.property.SimpleStringProperty(w);
        });

        TableColumn<Route, String> rColPrice = new TableColumn<>("价格");
        rColPrice.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty("¥" + cell.getValue().price));
        TableColumn<Route, String> rColTime = new TableColumn<>("耗时");
        rColTime.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().duration));
        TableColumn<Route, String> rColDep = new TableColumn<>("出发时间");
        rColDep.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
            cell.getValue().departure_time != null ? cell.getValue().departure_time : ""));
        TableColumn<Route, String> rColArr = new TableColumn<>("到达时间");
        rColArr.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
            cell.getValue().arrival_time != null ? cell.getValue().arrival_time : ""));

        resultTable.getColumns().addAll(rColID, rColFrom, rColTo, rColNum, rColWay, rColPrice, rColTime, rColDep, rColArr);
        SearchPane.getChildren().add(resultTable);

        // 检索逻辑
        ObservableList<Route> allRt = FXCollections.observableArrayList(Main.Routes);
        resultTable.setItems(allRt);

        Runnable doSearch = () -> {
            String rq = routeSearch.getText().trim();
            String cq = citySearch.getText().trim();
            if (rq.isEmpty() && cq.isEmpty()) {
                resultTable.setItems(allRt);
                return;
            }
            List<Route> filtered = new ArrayList<>();
            for (Route r : Main.Routes) {
                boolean matchNum  = rq.isEmpty() || r.number.contains(rq);
                boolean matchCity = cq.isEmpty() || r.from.contains(cq) || r.to.contains(cq);
                if (matchNum && matchCity)
                    filtered.add(r);
            }
            resultTable.setItems(FXCollections.observableArrayList(filtered));
        };

        routeSearch.textProperty().addListener((obs, o, n) ->
                doSearch.run());
        citySearch.textProperty().addListener((obs, o, n) ->
                doSearch.run());



        VBox SQLPane = new VBox(10);
        SQLPane.setPadding(new Insets(15, 20, 25, 20));
        SQLPane.setMaxWidth(Double.MAX_VALUE);
        SQLPane.setStyle("-fx-background-color: #f5f5f5;");

        Label SQLTitle = new Label("🗄 数据库管理");
        SQLTitle.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        SQLPane.getChildren().add(SQLTitle);


        SQLPane.getChildren().add(getGreyLine());

        // ==== 统计图表行 ====
        HBox chartRow = new HBox(12);
        chartRow.setAlignment(Pos.CENTER_LEFT);

        // 饼图 — 交通方式分布
        PieChart pieChart = new PieChart();
        pieChart.setPrefSize(220, 180);
        pieChart.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
            "-fx-border-color: #ddd; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 8;");
        pieChart.setLegendVisible(false);
        pieChart.setTitle("交通方式分布");
        int c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0;
        for (Route r : Main.Routes) {
            switch (r.way) { case 1->c1++; case 2->c2++; case 3->c3++; case 4->c4++; case 5->c5++; }
        }
        if (c1 > 0) pieChart.getData().add(new PieChart.Data("公路("+c1+")", c1));
        if (c2 > 0) pieChart.getData().add(new PieChart.Data("高铁("+c2+")", c2));
        if (c3 > 0) pieChart.getData().add(new PieChart.Data("飞机("+c3+")", c3));
        if (c4 > 0) pieChart.getData().add(new PieChart.Data("航运("+c4+")", c4));
        if (c5 > 0) pieChart.getData().add(new PieChart.Data("普速("+c5+")", c5));

        // 柱状图 — 出发城市Top5
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setPrefSize(400, 180);
        barChart.setTitle("出发城市 Top10");
        barChart.setLegendVisible(false);
        barChart.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
            "-fx-border-color: #ddd; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 8;");
        Map<String, Integer> cityCount = new LinkedHashMap<>();

        for (Route r : Main.Routes) cityCount.merge(r.from, 1, Integer::sum);
        cityCount.entrySet().stream()
            .sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(e -> barChart.getData().add(
                new XYChart.Series<>("", FXCollections.observableArrayList(
                    new XYChart.Data<>(e.getKey(), e.getValue())
                ))
            ));

        chartRow.getChildren().addAll(pieChart, barChart);
        SQLPane.getChildren().add(chartRow);

        // ==== 检索 ====
        HBox searchRow = new HBox(8);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        TextField sqlSearch = new TextField();
        sqlSearch.setPromptText("搜索车次号 / 城市 ...");
        sqlSearch.setPrefHeight(35);
        sqlSearch.setStyle("-fx-font-size: 14; -fx-background-radius: 8; -fx-border-radius: 8; " +
            "-fx-border-color: #00adb5; -fx-background-color: white; -fx-padding: 0 12;");
        HBox.setHgrow(sqlSearch, Priority.ALWAYS);

        Button clearBtn = new Button("清除");
        clearBtn.setPrefSize(60, 35);
        clearBtn.setStyle("-fx-background-color: #e0e0e0; -fx-font-size: 13; -fx-background-radius: 8;");
        clearBtn.setOnAction(e -> sqlSearch.clear());

        searchRow.getChildren().addAll(sqlSearch, clearBtn);
        SQLPane.getChildren().add(searchRow);

        // ==== 左表列表 + 右数据区 ====
        ListView<String> tableList = new ListView<>();
        tableList.setPrefWidth(120);
        tableList.getItems().addAll("city", "route", "user");
        tableList.getSelectionModel().selectFirst();
        tableList.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ddd;");

        TableView<Map<String, Object>> sqlTable = new TableView<>();
        sqlTable.setMaxWidth(Double.MAX_VALUE);
        sqlTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        sqlTable.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; " +
            "-fx-background-radius: 8; -fx-border-radius: 8;");

        VBox leftBox = new VBox(6, new Label("表列表"), tableList);
        leftBox.setPrefWidth(130);
        VBox.setVgrow(tableList, Priority.ALWAYS);

        SplitPane split = new SplitPane(leftBox, sqlTable);
        split.setDividerPositions(0.18);
        VBox.setVgrow(split, Priority.ALWAYS);
        SQLPane.getChildren().add(split);

        // 加载表数据
        List<Map<String, Object>> allRows = new ArrayList<>();
        Runnable loadTable = () -> {
            String tbl = tableList.getSelectionModel().getSelectedItem();

            if (tbl == null)
                return;

            sqlTable.getColumns().clear();
            sqlTable.getItems().clear();
            allRows.clear();
            try {
                ResultSet rs = SQL.con.createStatement().executeQuery("SELECT * FROM " + tbl);
                ResultSetMetaData meta = rs.getMetaData();

                int cc = meta.getColumnCount();
                String[] colNames = new String[cc];
                for (int i = 0; i < cc; i++) {
                    colNames[i] = meta.getColumnName(i + 1);
                    final int idx = i;

                    TableColumn<Map<String, Object>, String> tc = new TableColumn<>(colNames[i]);
                    tc.setCellValueFactory(cell -> {
                        Object v = cell.getValue().get(colNames[idx]);
                        return new javafx.beans.property.SimpleStringProperty(v == null ? "" : v.toString());
                    });
                    sqlTable.getColumns().add(tc);
                }

                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 0; i < cc; i++)
                        row.put(colNames[i], rs.getObject(i + 1));
                    allRows.add(row);
                }

                sqlTable.setItems(FXCollections.observableArrayList(allRows));
                rs.close();
            } catch (Exception ignored) {}
        };

        tableList.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            sqlSearch.clear();
            loadTable.run();
        });
        loadTable.run();

        // 检索过滤
        sqlSearch.textProperty().addListener((obs, o, n) -> {
            if (n == null || n.isEmpty()) {
                sqlTable.setItems(FXCollections.observableArrayList(allRows));
            } else {
                List<Map<String, Object>> filtered = new ArrayList<>();
                for (Map<String, Object> row : allRows) {
                    for (Object v : row.values()) {
                        if (v != null && v.toString().contains(n)) {
                            filtered.add(row);
                            break;
                        }
                    }
                }
                sqlTable.setItems(FXCollections.observableArrayList(filtered));
            }
        });

        // ==== 操作栏 ====
        HBox actionBar = new HBox(8);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        Button refreshBtn = new Button("刷新");
        refreshBtn.setPrefSize(70, 35);
        refreshBtn.setStyle("-fx-background-color: #e0e0e0; -fx-font-size: 13; -fx-background-radius: 8;");
        refreshBtn.setOnAction(e -> loadTable.run());

        Button insertBtn = new Button("新增行");
        insertBtn.setPrefSize(80, 35);
        insertBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 8;");

        insertBtn.setOnAction(e -> {
            String tbl = tableList.getSelectionModel().getSelectedItem();
            if (tbl == null) return;
            try {
                ResultSet rs = SQL.con.createStatement().executeQuery(
                        "SELECT * FROM " + tbl + " LIMIT 0");

                ResultSetMetaData meta = rs.getMetaData();
                int cc = meta.getColumnCount();
                rs.close();

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("新增行 → " + tbl);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10); grid.setVgap(8);
                TextField[] fields = new TextField[cc];
                for (int i = 0; i < cc; i++) {
                    grid.add(new Label(meta.getColumnName(i + 1)), 0, i);
                    fields[i] = new TextField();
                    fields[i].setPrefWidth(220);
                    grid.add(fields[i], 1, i);
                }
                dialog.getDialogPane().setContent(grid);

                dialog.showAndWait().ifPresent(btn -> {
                    if (btn != ButtonType.OK) return;
                    try {
                        StringBuilder cols = new StringBuilder(), vals = new StringBuilder();
                        for (int i = 0; i < cc; i++) {
                            String val = fields[i].getText().trim();
                            if (val.isEmpty())
                                continue;
                            if (cols.length() > 0)
                            {
                                cols.append(","); vals.append(",");
                            }
                            cols.append(meta.getColumnName(i + 1));
                            vals.append("'").append(val.replace("'", "''")).append("'");
                        }
                        if (cols.length() > 0) {
                            SQL.con.createStatement().executeUpdate(
                                "INSERT INTO " + tbl + " (" + cols + ") VALUES (" + vals + ")");
                            loadTable.run();
                        }
                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("新增失败");
                        alert.setHeaderText(null);
                        alert.setContentText(ex.getMessage());
                        alert.showAndWait();
                    }
                });
            } catch (Exception ex) {}
        });

        Button deleteBtn = new Button("删除行");
        deleteBtn.setPrefSize(80, 35);
        deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 8;");
        deleteBtn.setOnAction(e -> {
            Map<String, Object> sel = sqlTable.getSelectionModel().getSelectedItem();
            if (sel == null)
                return;
            String tbl = tableList.getSelectionModel().getSelectedItem();
            if (tbl == null)
                return;

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("确认删除");
            confirm.setHeaderText("确定要删除该行数据吗？");

            confirm.showAndWait().ifPresent(btn -> {
                if (btn != ButtonType.OK)
                    return;
                try {
                    String pkCol = sel.keySet().iterator().next();
                    String pkVal = sel.get(pkCol) == null ? "" : sel.get(pkCol).toString();
                    SQL.con.createStatement().executeUpdate(
                        "DELETE FROM " + tbl + " WHERE " + pkCol + " = '" + pkVal.replace("'", "''") + "'");
                    loadTable.run();
                } catch (Exception ex) {}
            });
        });

        Pane actionSpacer = new Pane();
        HBox.setHgrow(actionSpacer, Priority.ALWAYS);

        actionBar.getChildren().addAll(refreshBtn, insertBtn, deleteBtn, actionSpacer);
        SQLPane.getChildren().add(actionBar);

        // ==== SQL 控制台 ====
        HBox sqlBar = new HBox(8);
        sqlBar.setAlignment(Pos.CENTER_LEFT);
        TextField sqlField = new TextField();
        sqlField.setPromptText("输入 SQL 语句...");
        sqlField.setPrefHeight(35);
        sqlField.setStyle("-fx-font-size: 13; -fx-background-radius: 8; -fx-border-radius: 8; " +
            "-fx-border-color: #ddd; -fx-background-color: white; -fx-padding: 0 10;");
        HBox.setHgrow(sqlField, Priority.ALWAYS);

        Button execBtn = new Button("执行");
        execBtn.setPrefSize(80, 35);
        execBtn.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 8;");

        execBtn.setOnAction(e -> {
            String sqlText = sqlField.getText().trim();
            if (sqlText.isEmpty())
                return;

            try {
                String lower = sqlText.toLowerCase();
                if (lower.startsWith("select") || lower.startsWith("show") || lower.startsWith("describe")) {
                    ResultSet rs = SQL.con.createStatement().executeQuery(sqlText);
                    ResultSetMetaData meta = rs.getMetaData();
                    int cc = meta.getColumnCount();
                    String[] colNames = new String[cc];
                    sqlTable.getColumns().clear();

                    for (int i = 0; i < cc; i++) {
                        colNames[i] = meta.getColumnLabel(i + 1);
                        final int idx = i;

                        TableColumn<Map<String, Object>, String> tc = new TableColumn<>(colNames[i]);
                        tc.setCellValueFactory(cell -> {
                            Object v = cell.getValue().get(colNames[idx]);
                            return new javafx.beans.property.SimpleStringProperty(v == null ? "" : v.toString());
                        });
                        sqlTable.getColumns().add(tc);
                    }
                    List<Map<String, Object>> rows = new ArrayList<>();

                    while (rs.next()) {
                        Map<String, Object> row = new LinkedHashMap<>();
                        for (int i = 0; i < cc; i++)
                            row.put(colNames[i], rs.getObject(i + 1));
                        rows.add(row);
                    }
                    sqlTable.setItems(FXCollections.observableArrayList(rows));
                    rs.close();
                } else {
                    int affected = SQL.con.createStatement().executeUpdate(sqlText);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("执行成功");
                    alert.setHeaderText(null);
                    alert.setContentText("影响行数: " + affected);
                    alert.showAndWait();
                    loadTable.run();
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL 错误");
                alert.setHeaderText(null);
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        sqlField.setOnAction(e -> execBtn.fire());
        sqlBar.getChildren().addAll(sqlField, execBtn);
        SQLPane.getChildren().add(sqlBar);

        VBox SetPane = new VBox(12);
        SetPane.setPadding(new Insets(15, 20, 25, 20));
        SetPane.setMaxWidth(Double.MAX_VALUE);
        SetPane.setStyle("-fx-background-color: #f5f5f5;");

        Label SetLabel = new Label("⚙ 设置");
        SetLabel.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        SetPane.getChildren().add(SetLabel);

        Pane setSep = new Pane();
        setSep.setPrefHeight(1);
        setSep.setStyle("-fx-background-color: #ddd;");
        SetPane.getChildren().add(setSep);

        // ==== LLM 模型设置 ====
        Label llmTitle = new Label("LLM 模型");
        llmTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #555;");
        SetPane.getChildren().add(llmTitle);

        VBox llmCard = new VBox(8);
        llmCard.setPadding(new Insets(14));
        llmCard.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
            "-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-border-width: 1;");

        GridPane llmGrid = new GridPane();
        llmGrid.setHgap(12); llmGrid.setVgap(10);
        llmGrid.getColumnConstraints().addAll(new ColumnConstraints(50), new ColumnConstraints());

        Label urlLabel = new Label("URL");
        urlLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        llmGrid.add(urlLabel, 0, 0);
        TextField URLField = new TextField(Main.URL);
        URLField.setPrefHeight(35);
        URLField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        llmGrid.add(URLField, 1, 0);

        Label modelLabel = new Label("Model");
        modelLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        llmGrid.add(modelLabel, 0, 1);
        TextField ModelField = new TextField(Main.Model);
        ModelField.setPrefHeight(35);
        ModelField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        llmGrid.add(ModelField, 1, 1);

        Label apiLabel = new Label("API");
        apiLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        llmGrid.add(apiLabel, 0, 2);
        TextField APIField = new TextField();
        APIField.setPrefHeight(35);
        APIField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        llmGrid.add(APIField, 1, 2);

        HBox llmBtnRow = new HBox(10);
        Button ModelSureBtn = new Button("保存");
        ModelSureBtn.setPrefSize(80, 35);
        ModelSureBtn.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 8;");
        ModelSureBtn.setOnAction(e -> {
            Main.URL = URLField.getText();
            Main.Model = ModelField.getText();
            Main.API = APIField.getText();
        });
        Button ModelResetBtn = new Button("重置 Ollama");
        ModelResetBtn.setPrefSize(110, 35);
        ModelResetBtn.setStyle("-fx-background-color: #e0e0e0; -fx-font-size: 13; -fx-background-radius: 8;");
        ModelResetBtn.setOnAction(e -> {
            URLField.setText("http://localhost:11434/api/generate");
            ModelField.setText("gemma4:e4b");
            APIField.clear();
        });
        llmBtnRow.getChildren().addAll(ModelSureBtn, ModelResetBtn);
        llmGrid.add(llmBtnRow, 1, 3);
        llmCard.getChildren().add(llmGrid);
        SetPane.getChildren().add(llmCard);

        // ==== 数据库连接（仅管理员） ====
        if (SQL.getRole().equals("admin")) {
        Label dbTitle = new Label("数据库连接");
        dbTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #555;");
        SetPane.getChildren().add(dbTitle);

        VBox dbCard = new VBox(8);
        dbCard.setPadding(new Insets(14));
        dbCard.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
            "-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-border-width: 1;");

        GridPane dbGrid = new GridPane();
        dbGrid.setHgap(12); dbGrid.setVgap(10);
        dbGrid.getColumnConstraints().addAll(
            new ColumnConstraints(50), new ColumnConstraints(200), new ColumnConstraints(50), new ColumnConstraints());

        Label dbHostLbl = new Label("主机");
        dbHostLbl.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        dbGrid.add(dbHostLbl, 0, 0);
        TextField dbHostField = new TextField("localhost");
        dbHostField.setPrefHeight(35);
        dbHostField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        dbGrid.add(dbHostField, 1, 0);

        Label dbPortLbl = new Label("端口");
        dbPortLbl.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        dbGrid.add(dbPortLbl, 2, 0);
        TextField dbPortField = new TextField("3306");
        dbPortField.setPrefHeight(35);
        dbPortField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        dbGrid.add(dbPortField, 3, 0);

        Label dbNameLbl = new Label("库名");
        dbNameLbl.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        dbGrid.add(dbNameLbl, 0, 1);
        TextField dbNameField = new TextField("city");
        dbNameField.setPrefHeight(35);
        dbNameField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        dbGrid.add(dbNameField, 1, 1);

        Label dbUserLbl = new Label("用户");
        dbUserLbl.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        dbGrid.add(dbUserLbl, 2, 1);
        TextField dbUserField = new TextField("root");
        dbUserField.setPrefHeight(35);
        dbUserField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        dbGrid.add(dbUserField, 3, 1);

        Label dbPwdLbl = new Label("密码");
        dbPwdLbl.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        dbGrid.add(dbPwdLbl, 0, 2);
        PasswordField dbPwdField = new PasswordField();
        dbPwdField.setText("123456");
        dbPwdField.setPrefHeight(35);
        dbPwdField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        dbGrid.add(dbPwdField, 1, 2);

        Label dbStatusLbl = new Label("");
        dbStatusLbl.setStyle("-fx-font-size: 12; -fx-text-fill: #999;");
        dbGrid.add(dbStatusLbl, 3, 2);

        HBox dbBtnRow = new HBox(10);
        Button dbTestBtn = new Button("测试连接");
        dbTestBtn.setPrefSize(90, 35);
        dbTestBtn.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 8;");
        dbTestBtn.setOnAction(e -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection testCon = DriverManager.getConnection(
                    "jdbc:mysql://" + dbHostField.getText() + ":" + dbPortField.getText() + "/" + dbNameField.getText(),
                    dbUserField.getText(), dbPwdField.getText());
                testCon.close();
                dbStatusLbl.setText("✔ 连接成功");
                dbStatusLbl.setStyle("-fx-font-size: 12; -fx-text-fill: #28a745; -fx-font-weight: bold;");
            } catch (Exception ex) {
                dbStatusLbl.setText("✘ " + ex.getMessage());
                dbStatusLbl.setStyle("-fx-font-size: 12; -fx-text-fill: #dc3545;");
            }
        });

        dbBtnRow.getChildren().add(dbTestBtn);
        dbGrid.add(dbBtnRow, 1, 3);
        dbCard.getChildren().add(dbGrid);
        SetPane.getChildren().add(dbCard);

        // ==== 用户管理（仅管理员） ====
        Label userTitle = new Label("用户管理");
        userTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #555;");
        SetPane.getChildren().add(userTitle);

        VBox userCard = new VBox(8);
        userCard.setPadding(new Insets(14));
        userCard.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
            "-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-border-width: 1;");

        TableView<user> userTable = new TableView<>();
        userTable.setMaxWidth(Double.MAX_VALUE);
        userTable.setPrefHeight(150);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1;");

        HBox userBtnRow = new HBox(8);
        Button addUserBtn = new Button("新增用户");
        addUserBtn.setPrefSize(90, 32);
        addUserBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; " +
            "-fx-font-size: 12; -fx-font-weight: bold; -fx-background-radius: 6;");

        Button delUserBtn = new Button("删除用户");
        delUserBtn.setPrefSize(90, 32);
        delUserBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; " +
            "-fx-font-size: 12; -fx-font-weight: bold; -fx-background-radius: 6;");

        userBtnRow.getChildren().addAll(addUserBtn, delUserBtn);

        // 加载用户列表
        Runnable loadUsers = () -> {
            userTable.getColumns().clear();
            userTable.getItems().clear();
            try {
                TableColumn<user, String> colID = new TableColumn<>("ID");
                colID.setCellValueFactory(cell ->
                    new SimpleStringProperty(String.valueOf(cell.getValue().id)));

                TableColumn<user, String> colUser = new TableColumn<>("Username");
                colUser.setCellValueFactory(cell ->
                    new SimpleStringProperty(cell.getValue().username));

                TableColumn<user, String> colPwd = new TableColumn<>("Password");
                colPwd.setCellValueFactory(cell ->
                    new SimpleStringProperty(cell.getValue().password));

                TableColumn<user, String> colRole = new TableColumn<>("Role");
                colRole.setCellValueFactory(cell ->
                    new SimpleStringProperty(cell.getValue().role));

                userTable.getColumns().addAll(colID, colUser, colPwd, colRole);

                List<user> users = new ArrayList<>();
                ResultSet rs = SQL.con.createStatement().executeQuery("SELECT * FROM user");
                while (rs.next()) {
                    users.add(new user(
                        rs.getInt("ID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Role")
                    ));
                }
                userTable.setItems(FXCollections.observableArrayList(users));
                rs.close();
            } catch (Exception ignored) {}
        };
        loadUsers.run();

        addUserBtn.setOnAction(e -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("新增用户");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(8);

            TextField userField = new TextField();
            PasswordField pwdField = new PasswordField();
            TextField roleField = new TextField();
            roleField.setPromptText("admin 或 user");

            grid.add(new Label("Username"), 0, 0);
            grid.add(userField, 1, 0);
            grid.add(new Label("Password"), 0, 1);
            grid.add(pwdField, 1, 1);
            grid.add(new Label("Role"), 0, 2);
            grid.add(roleField, 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.showAndWait().ifPresent(btn -> {
                if (btn != ButtonType.OK) return;
                try {
                    String username = userField.getText().trim();
                    String password = pwdField.getText().trim();
                    String role = roleField.getText().trim();
                    if (username.isEmpty() || password.isEmpty()) return;
                    if (role.isEmpty()) role = "user";
                    SQL.con.createStatement().executeUpdate(
                        "INSERT INTO user (Username, Password, Role) VALUES ('"
                        + username.replace("'", "''") + "', '"
                        + password.replace("'", "''") + "', '"
                        + role.replace("'", "''") + "')");
                    loadUsers.run();
                } catch (Exception ex) {}
            });
        });

        delUserBtn.setOnAction(e -> {
            user sel = userTable.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("确认删除");
            confirm.setHeaderText("确定要删除用户 " + sel.username + " 吗？");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn != ButtonType.OK) return;
                try {
                    SQL.con.createStatement().executeUpdate(
                        "DELETE FROM user WHERE ID = " + sel.id);
                    loadUsers.run();
                } catch (Exception ex) {}
            });
        });

        userCard.getChildren().addAll(userTable, userBtnRow);
        SetPane.getChildren().add(userCard);
        }

        // ==== 输出路径 ====
        Label pathTitle = new Label("输出路径");
        pathTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #555;");
        SetPane.getChildren().add(pathTitle);

        VBox pathCard = new VBox(8);
        pathCard.setPadding(new Insets(14));
        pathCard.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
            "-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-border-width: 1;");

        HBox pathRow = new HBox(8);
        TextField PathField = new TextField(Main.OutputPath);
        PathField.setPrefHeight(35);
        PathField.setStyle("-fx-font-size: 13; -fx-background-radius: 6; -fx-border-radius: 6; " +
            "-fx-border-color: #ddd; -fx-padding: 0 10;");
        HBox.setHgrow(PathField, Priority.ALWAYS);

        Button BrowseBtn = new Button("浏览");
        BrowseBtn.setPrefSize(70, 35);
        BrowseBtn.setStyle("-fx-background-color: #e0e0e0; -fx-font-size: 13; -fx-background-radius: 8;");
        BrowseBtn.setOnAction(e -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("选择输出目录");
            File dir = dc.showDialog(stage);
            if (dir != null)
                PathField.setText(dir.getAbsolutePath());
        });

        Button PathOkBtn = new Button("确认");
        PathOkBtn.setPrefSize(70, 35);
        PathOkBtn.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 8;");
        PathOkBtn.setOnAction(e -> {
            Main.OutputPath = PathField.getText().trim();
            if (!Main.OutputPath.endsWith("\\") && !Main.OutputPath.endsWith("/"))
                Main.OutputPath += "\\";
            File f = new File(Main.OutputPath);
            Alert alert = new Alert
                    (f.exists() && f.isDirectory() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(f.exists() && f.isDirectory() ? "成功" : "错误");
            alert.setHeaderText(null);
            alert.setContentText(f.exists() && f.isDirectory() ? "路径有效" : "路径错误");
            alert.showAndWait();
        });

        pathRow.getChildren().addAll(PathField, BrowseBtn, PathOkBtn);
        pathCard.getChildren().add(pathRow);
        SetPane.getChildren().add(pathCard);


        StackPane StackPane = new StackPane();
        StackPane.setAlignment(Pos.TOP_LEFT);
        MainPane.setMaxWidth(Double.MAX_VALUE);
        chatUI.setMaxWidth(Double.MAX_VALUE);
        SearchPane.setMaxWidth(Double.MAX_VALUE);
        SQLPane.setMaxWidth(Double.MAX_VALUE);
        SetPane.setMaxWidth(Double.MAX_VALUE);
        StackPane.getChildren().addAll(MainPane,chatUI,SearchPane,SQLPane,SetPane);
        chatUI.setVisible(false);
        SQLPane.setVisible(false);
        SearchPane.setVisible(false);
        SetPane.setVisible(false);
        MainPanel.setCenter(StackPane);

        MainButton.setOnAction(e -> {
            MainButton.setStyle("-fx-background-color: #00adb5 ;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            ChatButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SearchButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SQLButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SetButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            MainPane.setVisible(true);
            chatUI.setVisible(false);
            SQLPane.setVisible(false);
            SearchPane.setVisible(false);
            SetPane.setVisible(false);
        });
        ChatButton.setOnAction(e -> {
            MainButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            ChatButton.setStyle("-fx-background-color: #00adb5;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SearchButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SQLButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SetButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            MainPane.setVisible(false);
            chatUI.setVisible(true);
            SQLPane.setVisible(false);
            SearchPane.setVisible(false);
            SetPane.setVisible(false);

        });
        SearchButton.setOnAction(e -> {
            MainButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            ChatButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SearchButton.setStyle("-fx-background-color: #00adb5;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SQLButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SetButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            MainPane.setVisible(false);
            chatUI.setVisible(false);
            SearchPane.setVisible(true);
            SQLPane.setVisible(false);
            SetPane.setVisible(false);
        });
        SQLButton.setOnAction(e -> {
            MainButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            ChatButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SearchButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SQLButton.setStyle("-fx-background-color: #00adb5;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SetButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            MainPane.setVisible(false);
            chatUI.setVisible(false);
            SearchPane.setVisible(false);
            SQLPane.setVisible(true);
            SetPane.setVisible(false);
        });
        SetButton.setOnAction(e -> {
            MainButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            ChatButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SearchButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SQLButton.setStyle("-fx-background-color: #393e46;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            SetButton.setStyle("-fx-background-color: #00adb5;-fx-font-size: 20;" +
                    "-fx-text-fill:white;-fx-background-radius: 6px;");
            MainPane.setVisible(false);
            chatUI.setVisible(false);
            SQLPane.setVisible(false);
            SearchPane.setVisible(false);
            SetPane.setVisible(true);
        });

        stage.setScene(MainScene);
        stage.show();
    }



    void loadProvinces() {
        try {
            String json = Files.readString(Path.of("icon/china.json"));
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonArray features = root.getAsJsonArray("features");

            for (int i = 0; i < features.size(); i++) {
                JsonObject feat = features.get(i).getAsJsonObject();
                JsonObject geom = feat.getAsJsonObject("geometry");
                String type = geom.get("type").getAsString();
                JsonArray coords = geom.getAsJsonArray("coordinates");

                if (type.equals("Polygon")) {
                    extractRings(coords);
                } else if (type.equals("MultiPolygon")) {
                    for (int j = 0; j < coords.size(); j++) {
                        extractRings(coords.get(j).getAsJsonArray());
                    }
                }
            }
        } catch (IOException e) {

        }
    }

    void extractRings(JsonArray rings) {
        for (int r = 0; r < rings.size(); r++) {
            JsonArray ring = rings.get(r).getAsJsonArray();
            int n = ring.size();
            double[] xs = new double[n];
            double[] ys = new double[n];

            for (int p = 0; p < n; p++) {
                JsonArray pt = ring.get(p).getAsJsonArray();
                xs[p] = toX(pt.get(0).getAsDouble());
                ys[p] = toY(pt.get(1).getAsDouble());
            }

            if (n >= 3) {
                provinces.add(new double[][]{xs, ys});
            }
        }
    }

    Pane getGreyLine(){
        Pane GreyLine=new Pane();
        GreyLine.setStyle("-fx-background-color: #dddddd;");
        GreyLine.setPrefHeight(1);
        GreyLine.setMaxWidth(Double.MAX_VALUE);
        return GreyLine;
    }

    static double toX(double lon) {
        return (lon - MIN_LON) / (MAX_LON - MIN_LON) * mapW;
    }
    static double toY(double lat) {
        return mapH - (lat - MIN_LAT) / (MAX_LAT - MIN_LAT) * mapH;
    }


    City findCityAt(double wx, double wy) {
        City best = null;
        double bestD2 = 100;  // 10×10
        for (City c : Main.Cities) {
            double dx = wx - toX(c.longitude);
            double dy = wy - toY(c.latitude);
            double d2 = dx * dx + dy * dy;
            if (d2 < bestD2) { bestD2 = d2; best = c; }
        }
        return best;
    }


    // 高德风格结果卡片
    VBox makeResultCard(String title, String color, String initMain) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPrefHeight(125);
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1;");

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-size: 11; -fx-text-fill: #999;");

        Label mainLbl = new Label(initMain);
        mainLbl.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label subLbl = new Label("—");
        subLbl.setStyle("-fx-font-size: 11; -fx-text-fill: #999;");

        card.getChildren().addAll(titleLbl, mainLbl, subLbl);
        return card;
    }

    void fillCard(VBox card, RouteQueryResult r, String title, String main, String sub1, String sub2, String color, Stage stage) {
        Label mainLbl = (Label) card.getChildren().get(1);
        Label subLbl = (Label) card.getChildren().get(2);

        if (!r.ok) {
            mainLbl.setText("—");
            mainLbl.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #ccc;");
            subLbl.setText("无法到达");
            subLbl.setStyle("-fx-font-size: 11; -fx-text-fill: #ccc;");
            card.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1;");
            card.setOnMouseClicked(null);
            return;
        }

        mainLbl.setText(main);
        mainLbl.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        subLbl.setText(sub1 + "  ·  " + sub2);
        subLbl.setStyle("-fx-font-size: 11; -fx-text-fill: #999;");
        card.setStyle("-fx-background-color: white; -fx-border-color: " + color + "; " +
            "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 2;");
        card.setOnMouseClicked(ev -> {
            activeRoutes = r.routes;
            render.run();
            showRouteDetail(r, title, stage);
        });
    }

    void showCityPopup(City city, Stage stage) {
        Popup popup = new Popup();
        popup.setAutoHide(true);

        VBox box = new VBox(6);
        box.setPadding(new Insets(12));
        box.setPrefWidth(440);
        box.setStyle("-fx-background-color: white;");


        HBox topRow = new HBox(8);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label(city.name);
        nameLabel.setStyle("-fx-font-size: 17; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Pane spring = new Pane();
        HBox.setHgrow(spring, Priority.ALWAYS);

        Button setFromBtn = new Button("设为出发");
        setFromBtn.setPrefSize(80, 30);
        setFromBtn.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 12; -fx-font-weight: bold; -fx-background-radius: 6;");
        setFromBtn.setOnAction(e -> {
            Main.From = city.name;
            FromButton.setText(Main.From);
            popup.hide();
            render.run();
        });

        Button setToBtn = new Button("设为到达");
        setToBtn.setPrefSize(80, 30);
        setToBtn.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; " +
            "-fx-font-size: 12; -fx-font-weight: bold; -fx-background-radius: 6;");
        setToBtn.setOnAction(e -> {
            Main.To = city.name;
            ToButton.setText(Main.To);
            popup.hide();
            render.run();
        });

        topRow.getChildren().addAll(nameLabel, spring, setFromBtn, setToBtn);
        box.getChildren().add(topRow);


        Label coordLabel = new Label("经度 " + String.format("%.4f", city.longitude) +
            "   纬度 " + String.format("%.4f", city.latitude));
        coordLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #999;");
        box.getChildren().add(coordLabel);


        Pane sep = new Pane();
        sep.setPrefHeight(1);
        sep.setStyle("-fx-background-color: #ddd;");
        box.getChildren().add(sep);


        List<Route> fromHere = new ArrayList<>();
        for (Route r : Main.Routes) {
            if (r.from.equals(city.name)) fromHere.add(r);
        }

        Label routeTitle = new Label("出发路线（" + fromHere.size() + "条）");
        routeTitle.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #555;");
        box.getChildren().add(routeTitle);


        for (Route r : fromHere) {
            HBox row = new HBox(6);
            row.setAlignment(Pos.CENTER_LEFT);


            String colorHex;
            if      (r.way == 1) colorHex = "#55aa55";
            else if (r.way == 2) colorHex = "#00adb5";
            else if (r.way == 3) colorHex = "#e8a400";
            else if (r.way == 4) colorHex = "#4488cc";
            else                 colorHex = "#8899aa";

            Pane dot = new Pane();
            dot.setPrefSize(10, 10);
            dot.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 2;");

            String wayName;
            if      (r.way == 1) wayName = "公路";
            else if (r.way == 2) wayName = "高铁";
            else if (r.way == 3) wayName = "飞机";
            else if (r.way == 4) wayName = "航运";
            else                 wayName = "普速";

            Label line = new Label(wayName + " → " + r.to +
                "  " + r.number + "  ¥" + r.price + "  " + r.duration);
            line.setStyle("-fx-font-size: 11; -fx-text-fill: #555;");

            row.getChildren().addAll(dot, line);
            box.getChildren().add(row);
        }

        ScrollPane sp = new ScrollPane(box);
        sp.setMaxHeight(400);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-border-color: #ccc; " +
            "-fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1; -fx-padding: 0;");

        popup.getContent().add(sp);
        popup.show(stage);
    }

    // 格式化耗时: 1.5h → "1h30m"
    String fmtDuration(double h) {
        int hh = (int) h, mm = (int) Math.round((h - hh) * 60);
        return mm == 0 ? hh + "h" : hh + "h" + mm + "m";
    }

    // 展示详细路径弹窗
    void showRouteDetail(RouteQueryResult r, String title, Stage stage) {
        if (r == null || !r.ok) return;
        Popup popup = new Popup();
        popup.setAutoHide(true);

        VBox box = new VBox(6);
        box.setPadding(new Insets(12));
        box.setPrefWidth(460);
        box.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label(title + "  ·  " + r.from + " → " + r.to);
        titleLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        box.getChildren().add(titleLabel);

        Label sumLabel = new Label(fmtDuration(r.totalTime) + "  ¥" + (int)r.totalPrice + "  换乘" + r.transfers + "次");
        sumLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #999;");
        box.getChildren().add(sumLabel);

        Pane sep = new Pane();
        sep.setPrefHeight(1);
        sep.setStyle("-fx-background-color: #ddd;");
        box.getChildren().add(sep);

        for (Route rt : r.routes) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);

            String colorHex = switch (rt.way) {
                case 1 -> "#55aa55"; case 2 -> "#00adb5"; case 3 -> "#e8a400";
                case 4 -> "#4488cc"; default -> "#8899aa";
            };
            Pane dot = new Pane();
            dot.setPrefSize(10, 10);
            dot.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 2;");

            String wayN = switch (rt.way) {
                case 1 -> "公路"; case 2 -> "高铁"; case 3 -> "飞机";
                case 4 -> "航运"; default -> "普速";
            };

            String dayStr = rt.travelDay > 0 ? "  +" + rt.travelDay + "天" : "";
            Label line = new Label(rt.from + "  --" + rt.number + "-->  " + rt.to +
                "    " + wayN + "  ¥" + rt.price + "  " + rt.duration + dayStr);
            line.setStyle("-fx-font-size: 12; -fx-text-fill: #555;");

            row.getChildren().addAll(dot, line);
            box.getChildren().add(row);
        }

        ScrollPane sp = new ScrollPane(box);
        sp.setMaxHeight(400);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-border-color: #ccc; " +
            "-fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1; -fx-padding: 0;");

        popup.getContent().add(sp);
        popup.show(stage);

    }

}
