# 全国交通咨询模拟系统

### Java + JavaFX 数据结构课程设计：城市交通网络建模与多目标路径规划

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21-5382A1)
![Algorithms](https://img.shields.io/badge/算法-Dijkstra%20%7C%20Floyd-2F6F8F)

> 学院推选的优秀数据结构课程设计作品。城市建模为图顶点，交通班次建模为带时刻、耗时与费用属性的有向边。

## 项目简介

全国交通咨询模拟系统面向交通线路查询与出行方案比较。用户可以选择起点、终点和路线策略，查看分段班次、出发与到达时刻、换乘等待时间、累计用时和费用；管理员可以维护城市及路线数据。JavaFX 提供桌面交互界面，MySQL 保存城市、班次和账号信息。

项目围绕图结构和最短路径算法展开，使用 Dijkstra 与 Floyd 从单起点查询、全局路径计算等角度求解和比较路径结果。系统提供最短用时、最低费用和较少换乘等查询策略。

本项目用于课程设计和算法演示。票价与班次取决于本地数据库，不提供实时售票或实时交通数据。

## 功能

| 模块 | 说明 |
| --- | --- |
| 路径规划 | 按用时、费用或换乘偏好查询城市间路线 |
| 行程详情 | 展示班次、出发/到达时间、等待时间、总用时与费用 |
| 地图交互 | 在地图上选择城市并查看规划线路 |
| 路线检索 | 按城市或车次关键词筛选已有线路 |
| 数据管理 | 登录后维护城市、线路及账号数据 |
| AI 助手 | 调用兼容 Ollama `/api/generate` 请求/响应格式的文本生成服务 |

## 算法与图模型

- **顶点**：城市。
- **边**：城市间的一班交通服务；边数据包含交通类型、编号、出发与到达时刻、旅行天数及票价。
- **Dijkstra**：用于给定起点的路径搜索；系统按查询目标选择相应代价。
- **Floyd**：通过中间顶点逐步更新城市对之间的路径结果，并用于与 Dijkstra 的结果作比较。
- **换乘计算**：路径代价需要考虑班次衔接和等待时间；跨日行程也需要正确换算。

典型复杂度：Floyd 为 `O(V³)` 时间、`O(V²)` 空间；Dijkstra 的复杂度取决于图存储和选点实现，常见邻接矩阵实现为 `O(V²)`，邻接表加优先队列实现为 `O((V + E) log V)`。`V` 为城市数，`E` 为班次数。

## 技术栈

- Java 17
- JavaFX 21
- MySQL 8+
- Gson（解析地图 GeoJSON）
- MySQL Connector/J
- Ollama-compatible `/api/generate` HTTP adapter

### AI 接口格式

AI 模块通过 HTTP `POST` 向设置的 URL 发送 JSON 请求，字段为 `model`、`prompt` 和 `stream: true`。它逐行读取流式 NDJSON 响应中的 `response` 字段，并在 `done: true` 时结束。若设置了 API Key，会附加 `Authorization: Bearer ...` 请求头。

默认配置为 `http://localhost:11434/api/generate` 和模型名 `gemma4:e4b`；可在程序的 AI 设置界面调整 URL、模型名和可选密钥。因此接口约定兼容 Ollama 格式的服务端；是否能连接取决于目标服务是否实现相同端点与流式字段。

## 项目结构

```text
.
├── database/
│   └── schema.sql
├── src/main/java/
│   ├── ChatPane.java
│   ├── DijRouteFinder.java
│   ├── GUI.java
│   ├── LLM.java
│   ├── Login.java
│   ├── Main.java
│   └── SQL.java
└── pom.xml
```

## 运行方法

### 1. 准备环境

安装 JDK 17 或更高版本、Maven 3.8+ 和 MySQL 8 或更高版本。JavaFX、Gson 和 MySQL Connector/J 由 Maven 根据 `pom.xml` 下载。

### 2. 初始化数据库

在 MySQL 客户端执行：

```sql
SOURCE database/schema.sql;
```

项目原始资料没有附带数据库导出文件或线路样例数据。建表后，需要按 `schema.sql` 中的字段导入城市、路线记录；`city.RouteID` 由当前程序用逗号分隔的线路 ID 维护。

首次运行前，需在本地数据库创建至少一个账号。下面示例中的密码请只在本机替换，不要提交真实账号或密码：

```sql
INSERT INTO `user` (Username, Password, Role)
VALUES ('admin', '请替换为本机临时密码', 'admin');
```

当前课程原型按数据库中的 `Password` 字段进行直接比对，适合本地演示；不要复用真实账户密码，也不要直接部署到公网。

### 3. 配置数据库连接

在启动程序的终端中设置环境变量。数据库密码只保存在本机环境中，不写入源码：

```powershell
$env:TRANSPORT_DB_URL = "jdbc:mysql://localhost:3306/city"
$env:TRANSPORT_DB_USER = "root"
$env:TRANSPORT_DB_PASSWORD = "在此填入本机 MySQL 密码"
```

也可以在 IntelliJ IDEA 的运行配置中添加同名环境变量。连接账户需要有 `city` 数据库的读写权限。

### 4. 启动应用

在仓库根目录执行：

```bash
mvn clean javafx:run
```

程序入口为 `Login`。登录后可使用线路检索、路线规划和数据管理界面。

### 5. 配置 AI 助手（可选）

启动一个实现 Ollama `/api/generate` 格式的服务，然后在 AI 设置界面填写其完整接口 URL 和服务端模型名。若服务端需要鉴权，再填写 API Key；本地 Ollama 通常可留空。AI 与联网搜索功能依赖本机网络/服务配置。

## 当前资料范围

已从项目压缩包整理出 7 个 Java 源文件。资料包没有包含 MySQL 数据导出文件，也没有包含界面读取的 `icon/china.json` GeoJSON 文件；因此仓库提供建表脚本和运行说明，但地图底图及示例城市/线路数据需要另行补充。缺少该 GeoJSON 时，地图底图不会绘出。

仓库未上传课程说明书、答辩材料或包含个人学号的文件。
