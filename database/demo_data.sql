-- Static coursework demo data: 36 cities and 308 route records.
-- This is sample data for demonstrating the application, not a live timetable.
USE city;

INSERT INTO city (name, longitude, latitude) VALUES
('北京',116.40,39.90),('天津',117.20,39.13),('石家庄',114.48,38.03),
('保定',115.48,38.87),('太原',112.53,37.87),('呼和浩特',111.65,40.82),
('郑州',113.65,34.76),('济南',117.00,36.65),('沈阳',123.38,41.80),
('长春',125.35,43.88),('吉林市',126.57,43.87),('哈尔滨',126.63,45.75),
('大连',121.62,38.92),('上海',121.47,31.23),('南京',118.78,32.04),
('合肥',117.27,31.86),('杭州',120.19,30.26),('福州',119.30,26.08),
('南昌',115.89,28.68),('武汉',114.31,30.52),('长沙',112.94,28.22),
('广州',113.23,23.16),('深圳',114.07,22.62),('南宁',108.33,22.84),
('海口',110.35,20.02),('三亚',109.51,18.25),('重庆',106.54,29.59),
('成都',104.06,30.67),('贵阳',106.71,26.57),('昆明',102.73,25.04),
('西安',108.95,34.27),('兰州',103.73,36.03),('西宁',101.74,36.56),
('银川',106.27,38.47),('乌鲁木齐',87.68,43.77),('拉萨',91.11,29.97);

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (1,'北京','天津',2,'C2001',55,'0.5h','06:05','06:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (2,'天津','北京',2,'C2002',55,'0.5h','07:10','07:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (3,'北京','济南',2,'G101',195,'1.5h','07:15','08:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (4,'济南','北京',2,'G102',195,'1.5h','08:20','09:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (5,'天津','济南',2,'G171',140,'1h','08:25','09:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (6,'济南','天津',2,'G172',140,'1h','09:30','10:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (7,'济南','南京',2,'G101',280,'2h','09:35','11:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (8,'南京','济南',2,'G102',280,'2h','10:40','12:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (9,'北京','南京',2,'G101',445,'3.5h','10:45','14:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (10,'南京','北京',2,'G102',445,'3.5h','11:50','15:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (11,'北京','上海',2,'G1',669,'4.5h','07:00','11:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (12,'上海','北京',2,'G2',669,'4.5h','07:00','11:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (13,'南京','上海',2,'G7001',140,'1h','13:05','14:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (14,'上海','南京',2,'G7002',140,'1h','14:10','15:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (15,'上海','杭州',2,'G7301',73,'1h','14:15','15:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (16,'杭州','上海',2,'G7302',73,'1h','15:20','16:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (17,'南京','杭州',2,'G7601',120,'1.5h','15:25','16:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (18,'杭州','南京',2,'G7602',120,'1.5h','16:30','18:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (19,'北京','上海',5,'T109',180,'15h','06:35','21:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (20,'上海','北京',5,'T110',180,'15h','08:40','23:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (21,'北京','南京',5,'T65',150,'11h','09:45','20:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (22,'南京','北京',5,'T66',150,'11h','07:50','18:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (23,'北京','济南',5,'K101',100,'6h','20:55','+1 02:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (24,'济南','北京',5,'K102',100,'6h','06:00','12:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (25,'北京','石家庄',2,'G651',130,'1h','06:05','07:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (26,'石家庄','北京',2,'G652',130,'1h','07:10','08:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (27,'石家庄','郑州',2,'G651',220,'1.5h','07:15','08:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (28,'郑州','石家庄',2,'G652',220,'1.5h','08:20','09:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (29,'北京','郑州',2,'G801',310,'2.5h','13:05','15:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (30,'郑州','北京',2,'G802',310,'2.5h','09:30','12:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (31,'郑州','武汉',2,'G801',240,'2h','09:35','11:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (32,'武汉','郑州',2,'G802',240,'2h','10:40','12:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (33,'武汉','长沙',2,'G1001',160,'1.5h','10:45','12:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (34,'长沙','武汉',2,'G1002',160,'1.5h','11:50','13:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (35,'长沙','广州',2,'G6001',310,'2.5h','12:55','15:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (36,'广州','长沙',2,'G6002',310,'2.5h','13:00','15:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (37,'北京','广州',5,'Z35',250,'22h','13:05','+1 11:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (38,'广州','北京',5,'Z36',250,'22h','14:10','+1 12:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (39,'北京','武汉',5,'Z37',160,'12h','14:15','+1 02:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (40,'武汉','北京',5,'Z38',160,'12h','06:20','18:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (41,'北京','郑州',5,'K179',120,'8h','07:25','15:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (42,'郑州','北京',5,'K180',120,'8h','09:30','17:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (43,'武汉','广州',5,'T95',140,'11h','06:35','17:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (44,'广州','武汉',5,'T96',140,'11h','08:40','19:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (45,'北京','沈阳',2,'G901',430,'4h','18:45','22:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (46,'沈阳','北京',2,'G902',430,'4h','19:50','23:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (47,'沈阳','长春',2,'G8011',145,'1h','20:55','21:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (48,'长春','沈阳',2,'G8012',145,'1h','06:00','07:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (49,'长春','哈尔滨',2,'G901',110,'1h','06:05','07:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (50,'哈尔滨','长春',2,'G902',110,'1h','07:10','08:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (51,'北京','长春',2,'G901',550,'5h','07:15','12:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (52,'长春','北京',2,'G902',550,'5h','08:20','13:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (53,'北京','哈尔滨',2,'G901',580,'6h','08:25','14:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (54,'哈尔滨','北京',2,'G902',580,'6h','09:30','15:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (55,'北京','吉林市',2,'G129',532,'4.5h','09:35','14:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (56,'吉林市','北京',2,'G130',532,'4.5h','10:40','15:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (57,'长春','吉林市',2,'C1201',32,'0.5h','10:45','11:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (58,'吉林市','长春',2,'C1202',32,'0.5h','11:50','12:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (59,'哈尔滨','吉林市',2,'D525',123,'2h','12:55','14:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (60,'吉林市','哈尔滨',2,'D526',123,'2h','13:00','15:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (61,'北京','沈阳',5,'K53',180,'9h','13:05','22:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (62,'沈阳','北京',5,'K54',180,'9h','14:10','23:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (63,'沈阳','长春',5,'K7397',65,'3.5h','14:15','17:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (64,'长春','沈阳',5,'K7398',65,'3.5h','15:20','18:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (65,'长春','哈尔滨',5,'K1393',50,'3h','15:25','18:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (66,'哈尔滨','长春',5,'K1394',50,'3h','16:30','19:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (67,'北京','哈尔滨',5,'T17',200,'12h','06:35','18:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (68,'哈尔滨','北京',5,'T18',200,'12h','08:40','20:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (69,'北京','吉林市',5,'K215',180,'14h','09:45','23:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (70,'吉林市','北京',5,'K216',180,'14h','07:50','21:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (71,'哈尔滨','吉林市',5,'K7333',55,'5h','20:55','+1 01:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (72,'吉林市','哈尔滨',5,'K7334',55,'5h','06:00','11:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (73,'北京','太原',2,'G601',195,'2.5h','06:05','08:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (74,'太原','北京',2,'G602',195,'2.5h','07:10','09:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (75,'太原','西安',2,'G2501',180,'3h','07:15','10:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (76,'西安','太原',2,'G2502',180,'3h','08:20','11:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (77,'西安','成都',2,'G2201',260,'3.5h','08:25','11:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (78,'成都','西安',2,'G2202',260,'3.5h','09:30','13:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (79,'成都','重庆',2,'G8601',150,'1.5h','09:35','11:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (80,'重庆','成都',2,'G8602',150,'1.5h','10:40','12:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (81,'重庆','贵阳',2,'G2801',140,'2h','10:45','12:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (82,'贵阳','重庆',2,'G2802',140,'2h','11:50','13:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (83,'贵阳','昆明',2,'G2901',210,'2h','12:55','14:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (84,'昆明','贵阳',2,'G2902',210,'2h','13:00','15:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (85,'北京','西安',5,'T41',150,'13h','13:05','+1 02:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (86,'西安','北京',5,'T42',150,'13h','14:10','+1 03:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (87,'北京','成都',5,'T7',250,'22h','14:15','+1 12:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (88,'成都','北京',5,'T8',250,'22h','06:20','+1 04:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (89,'西安','成都',5,'K5',110,'12h','07:25','19:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (90,'成都','西安',5,'K6',110,'12h','09:30','21:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (91,'成都','昆明',5,'K113',140,'18h','06:35','+1 00:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (92,'昆明','成都',5,'K114',140,'18h','08:40','+1 02:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (93,'郑州','西安',2,'G2001',230,'2h','18:45','20:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (94,'西安','郑州',2,'G2002',230,'2h','19:50','21:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (95,'西安','兰州',2,'G901',240,'3h','20:55','23:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (96,'兰州','西安',2,'G902',240,'3h','06:00','09:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (97,'兰州','乌鲁木齐',2,'G701',550,'11h','06:05','17:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (98,'乌鲁木齐','兰州',2,'G702',550,'11h','07:10','18:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (99,'西安','兰州',5,'K119',100,'8h','07:15','15:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (100,'兰州','西安',5,'K120',100,'8h','08:20','16:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (101,'兰州','乌鲁木齐',5,'T295',280,'20h','08:25','+1 04:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (102,'乌鲁木齐','兰州',5,'T296',280,'20h','09:30','+1 05:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (103,'北京','乌鲁木齐',5,'Z69',320,'30h','09:35','+1 15:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (104,'乌鲁木齐','北京',5,'Z70',320,'30h','10:40','+1 16:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (105,'杭州','福州',2,'G1601',270,'3h','10:45','13:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (106,'福州','杭州',2,'G1602',270,'3h','11:50','14:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (107,'合肥','南京',2,'G7601',70,'1h','12:55','13:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (108,'南京','合肥',2,'G7602',70,'1h','13:00','14:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (109,'武汉','合肥',2,'G1701',130,'2h','13:05','15:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (110,'合肥','武汉',2,'G1702',130,'2h','14:10','16:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (111,'南昌','长沙',2,'G1301',160,'1.5h','14:15','15:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (112,'长沙','南昌',2,'G1302',160,'1.5h','15:20','16:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (113,'福州','南昌',2,'G1601',160,'2h','15:25','17:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (114,'南昌','福州',2,'G1602',160,'2h','16:30','18:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (115,'上海','福州',5,'K163',120,'12h','06:35','18:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (116,'福州','上海',5,'K164',120,'12h','08:40','20:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (117,'南昌','上海',5,'K287',100,'10h','09:45','19:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (118,'上海','南昌',5,'K288',100,'10h','07:50','17:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (119,'广州','南宁',2,'G2901',170,'3h','20:55','23:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (120,'南宁','广州',2,'G2902',170,'3h','06:00','09:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (121,'广州','南宁',5,'K1233',90,'8h','06:05','14:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (122,'南宁','广州',5,'K1234',90,'8h','07:10','15:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (123,'广州','海口',5,'K511',180,'12h','07:15','19:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (124,'海口','广州',5,'K512',180,'12h','08:20','20:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (125,'重庆','昆明',5,'K167',160,'16h','08:25','+1 00:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (126,'昆明','重庆',5,'K168',160,'16h','09:30','+1 01:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (127,'北京','呼和浩特',2,'G2401',180,'2.5h','09:35','12:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (128,'呼和浩特','北京',2,'G2402',180,'2.5h','10:40','13:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (129,'北京','呼和浩特',5,'K89',90,'8h','10:45','18:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (130,'呼和浩特','北京',5,'K90',90,'8h','11:50','19:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (131,'北京','拉萨',5,'Z21',360,'40h','12:55','+2 04:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (132,'拉萨','北京',5,'Z22',360,'40h','13:00','+2 05:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (133,'西安','银川',2,'G1901',120,'1.5h','13:05','14:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (134,'银川','西安',2,'G1902',120,'1.5h','14:10','15:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (135,'西安','西宁',2,'G2001',140,'2h','14:15','16:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (136,'西宁','西安',2,'G2002',140,'2h','15:20','17:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (137,'北京','保定',2,'G671',25,'0.5h','15:25','15:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (138,'保定','北京',2,'G672',25,'0.5h','16:30','17:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (139,'保定','石家庄',2,'G671',40,'0.5h','16:35','17:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (140,'石家庄','保定',2,'G672',40,'0.5h','17:40','18:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (141,'北京','保定',5,'K7701',10,'2h','18:45','20:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (142,'保定','北京',5,'K7702',10,'2h','19:50','21:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (143,'天津','南京',2,'G121',380,'3h','20:55','23:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (144,'南京','天津',2,'G122',380,'3h','06:00','09:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (145,'天津','上海',2,'G131',510,'4h','06:05','10:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (146,'上海','天津',2,'G132',510,'4h','07:10','11:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (147,'济南','上海',2,'G111',400,'3h','07:15','10:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (148,'上海','济南',2,'G112',400,'3h','08:20','11:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (149,'济南','杭州',2,'G1801',310,'2.5h','08:25','10:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (150,'杭州','济南',2,'G1802',310,'2.5h','09:30','12:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (151,'北京','长沙',2,'G71',650,'5.5h','09:35','15:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (152,'长沙','北京',2,'G72',650,'5.5h','10:40','16:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (153,'北京','武汉',2,'G501',520,'4h','10:45','14:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (154,'武汉','北京',2,'G502',520,'4h','11:50','15:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (155,'石家庄','武汉',2,'G661',370,'3h','12:55','15:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (156,'武汉','石家庄',2,'G662',370,'3h','13:00','16:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (157,'石家庄','长沙',2,'G671',480,'4h','13:05','17:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (158,'长沙','石家庄',2,'G672',480,'4h','14:10','18:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (159,'郑州','长沙',2,'G831',390,'3h','14:15','17:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (160,'长沙','郑州',2,'G832',390,'3h','15:20','18:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (161,'郑州','广州',2,'G841',650,'5.5h','15:25','20:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (162,'广州','郑州',2,'G842',650,'5.5h','16:30','22:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (163,'沈阳','哈尔滨',2,'G701',250,'2h','16:35','18:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (164,'哈尔滨','沈阳',2,'G702',250,'2h','17:40','19:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (165,'天津','沈阳',2,'G1201',320,'3h','18:45','21:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (166,'沈阳','天津',2,'G1202',320,'3h','19:50','22:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (167,'天津','哈尔滨',2,'G1203',500,'5h','20:55','+1 01:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (168,'哈尔滨','天津',2,'G1204',500,'5h','06:00','11:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (169,'沈阳','吉林市',2,'G8001',180,'1.5h','06:05','07:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (170,'吉林市','沈阳',2,'G8002',180,'1.5h','07:10','08:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (171,'大连','沈阳',2,'G8003',180,'2h','07:15','09:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (172,'沈阳','大连',2,'G8004',180,'2h','08:20','10:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (173,'哈尔滨','大连',2,'G705',220,'2.5h','08:25','10:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (174,'大连','哈尔滨',2,'G706',220,'2.5h','09:30','12:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (175,'北京','兰州',2,'G4301',650,'7h','09:35','16:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (176,'兰州','北京',2,'G4302',650,'7h','10:40','17:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (177,'北京','银川',2,'G4303',500,'5h','10:45','15:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (178,'银川','北京',2,'G4304',500,'5h','11:50','16:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (179,'北京','西宁',2,'G4305',600,'7h','12:55','19:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (180,'西宁','北京',2,'G4306',600,'7h','13:00','20:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (181,'兰州','西宁',2,'D2701',60,'1h','13:05','14:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (182,'西宁','兰州',2,'D2702',60,'1h','14:10','15:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (183,'西安','乌鲁木齐',2,'G3201',780,'12h','14:15','+1 02:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (184,'乌鲁木齐','西安',2,'G3202',780,'12h','06:20','18:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (185,'呼和浩特','西安',2,'G4307',300,'4h','15:25','19:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (186,'西安','呼和浩特',2,'G4308',300,'4h','16:30','20:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (187,'北京','重庆',2,'G571',750,'7h','06:35','13:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (188,'重庆','北京',2,'G572',750,'7h','08:40','15:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (189,'北京','贵阳',2,'G2301',800,'8h','09:45','17:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (190,'贵阳','北京',2,'G2302',800,'8h','07:50','15:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (191,'成都','贵阳',2,'G2851',260,'3h','20:55','23:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (192,'贵阳','成都',2,'G2852',260,'3h','06:00','09:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (193,'成都','长沙',2,'G2501',500,'5h','06:05','11:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (194,'长沙','成都',2,'G2502',500,'5h','07:10','12:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (195,'重庆','长沙',2,'G2331',380,'4h','07:15','11:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (196,'长沙','重庆',2,'G2332',380,'4h','08:20','12:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (197,'重庆','武汉',2,'G3101',280,'3h','08:25','11:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (198,'武汉','重庆',2,'G3102',280,'3h','09:30','12:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (199,'重庆','西安',2,'G3203',280,'3h','09:35','12:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (200,'西安','重庆',2,'G3204',280,'3h','10:40','13:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (201,'上海','合肥',2,'G7201',140,'2h','10:45','12:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (202,'合肥','上海',2,'G7202',140,'2h','11:50','13:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (203,'上海','武汉',2,'G1701',330,'4h','12:55','16:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (204,'武汉','上海',2,'G1702',330,'4h','13:00','17:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (205,'上海','长沙',2,'G1301',470,'5h','13:05','18:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (206,'长沙','上海',2,'G1302',470,'5h','14:10','19:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (207,'杭州','武汉',2,'G5801',300,'3.5h','14:15','17:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (208,'武汉','杭州',2,'G5802',300,'3.5h','15:20','18:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (209,'杭州','长沙',2,'G1401',370,'4h','15:25','19:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (210,'长沙','杭州',2,'G1402',370,'4h','16:30','20:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (211,'南京','武汉',2,'G5881',200,'2.5h','16:35','19:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (212,'武汉','南京',2,'G5882',200,'2.5h','17:40','20:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (213,'南京','郑州',2,'G1881',280,'3h','18:45','21:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (214,'郑州','南京',2,'G1882',280,'3h','19:50','22:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (215,'合肥','郑州',2,'G1901',220,'2.5h','20:55','23:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (216,'郑州','合肥',2,'G1902',220,'2.5h','06:00','08:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (217,'南昌','武汉',2,'G2001',110,'1h','06:05','07:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (218,'武汉','南昌',2,'G2002',110,'1h','07:10','08:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (219,'南昌','杭州',2,'G1401',260,'3h','07:15','10:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (220,'杭州','南昌',2,'G1402',260,'3h','08:20','11:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (221,'福州','武汉',2,'G1601',300,'4h','08:25','12:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (222,'武汉','福州',2,'G1602',300,'4h','09:30','13:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (223,'福州','长沙',2,'G1603',360,'4h','09:35','13:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (224,'长沙','福州',2,'G1604',360,'4h','10:40','14:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (225,'广州','深圳',2,'G6201',100,'0.5h','10:45','11:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (226,'深圳','广州',2,'G6202',100,'0.5h','11:50','12:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (227,'广州','贵阳',2,'G3001',315,'4h','12:55','16:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (228,'贵阳','广州',2,'G3002',315,'4h','13:00','17:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (229,'广州','昆明',2,'G3021',480,'6h','13:05','19:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (230,'昆明','广州',2,'G3022',480,'6h','14:10','20:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (231,'广州','成都',2,'G3701',580,'7h','14:15','21:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (232,'成都','广州',2,'G3702',580,'7h','06:20','13:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (233,'广州','重庆',2,'G3711',450,'5h','15:25','20:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (234,'重庆','广州',2,'G3712',450,'5h','16:30','21:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (235,'南宁','昆明',2,'G4001',220,'3h','16:35','19:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (236,'昆明','南宁',2,'G4002',220,'3h','17:40','20:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (237,'南宁','贵阳',2,'G4101',130,'2h','18:45','20:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (238,'贵阳','南宁',2,'G4102',130,'2h','19:50','21:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (239,'海口','南宁',5,'K581',80,'10h','08:55','18:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (240,'南宁','海口',5,'K582',80,'10h','06:00','16:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (241,'北京','广州',3,'CA1301',1200,'3h','06:05','09:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (242,'广州','北京',3,'CA1302',1200,'3h','07:10','10:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (243,'北京','成都',3,'CA4101',1100,'3h','07:15','10:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (244,'成都','北京',3,'CA4102',1100,'3h','08:20','11:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (245,'北京','昆明',3,'MU5701',1300,'3.5h','08:25','11:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (246,'昆明','北京',3,'MU5702',1300,'3.5h','09:30','13:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (247,'北京','乌鲁木齐',3,'CA1901',1500,'4h','09:35','13:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (248,'乌鲁木齐','北京',3,'CA1902',1500,'4h','10:40','14:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (249,'北京','拉萨',3,'CA4401',1800,'4.5h','10:45','15:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (250,'拉萨','北京',3,'CA4402',1800,'4.5h','11:50','16:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (251,'上海','广州',3,'CZ3501',1100,'2.5h','12:55','15:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (252,'广州','上海',3,'CZ3502',1100,'2.5h','13:00','15:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (253,'上海','成都',3,'MU5401',1200,'3h','13:05','16:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (254,'成都','上海',3,'MU5402',1200,'3h','14:10','17:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (255,'上海','昆明',3,'MU5801',1100,'3h','14:15','17:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (256,'昆明','上海',3,'MU5802',1100,'3h','15:20','18:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (257,'广州','成都',3,'CZ3401',1000,'2.5h','15:25','17:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (258,'成都','广州',3,'CZ3402',1000,'2.5h','16:30','19:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (259,'广州','昆明',3,'CZ3421',900,'2h','16:35','18:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (260,'昆明','广州',3,'CZ3422',900,'2h','17:40','19:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (261,'哈尔滨','三亚',3,'CZ6701',2200,'5h','18:45','23:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (262,'三亚','哈尔滨',3,'CZ6702',2200,'5h','19:50','+1 00:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (263,'上海','西安',2,'G3601',600,'6h','20:55','+1 02:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (264,'西安','上海',2,'G3602',600,'6h','06:00','12:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (265,'上海','郑州',2,'G1811',350,'4h','06:05','10:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (266,'郑州','上海',2,'G1812',350,'4h','07:10','11:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (267,'南京','西安',2,'G3603',450,'5h','07:15','12:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (268,'西安','南京',2,'G3604',450,'5h','08:20','13:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (269,'天津','西安',2,'G1701',420,'4.5h','08:25','12:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (270,'西安','天津',2,'G1702',420,'4.5h','09:30','14:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (271,'武汉','西安',2,'G3201',290,'3h','09:35','12:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (272,'西安','武汉',2,'G3202',290,'3h','10:40','13:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (273,'济南','西安',2,'G1801',360,'4h','10:45','14:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (274,'西安','济南',2,'G1802',360,'4h','11:50','15:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (275,'济南','武汉',2,'G1831',350,'3.5h','12:55','16:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (276,'武汉','济南',2,'G1832',350,'3.5h','13:00','16:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (277,'合肥','长沙',2,'G1901',270,'3h','13:05','16:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (278,'长沙','合肥',2,'G1902',270,'3h','14:10','17:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (279,'福州','广州',2,'G1611',360,'4.5h','14:15','18:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (280,'广州','福州',2,'G1612',360,'4.5h','15:20','19:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (281,'南昌','广州',2,'G1401',380,'4h','15:25','19:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (282,'广州','南昌',2,'G1402',380,'4h','16:30','20:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (283,'昆明','重庆',2,'G2801',320,'3.5h','16:35','20:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (284,'重庆','昆明',2,'G2802',320,'3.5h','17:40','21:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (285,'成都','武汉',2,'G3101',360,'4h','18:45','22:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (286,'武汉','成都',2,'G3102',360,'4h','19:50','23:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (287,'成都','郑州',2,'G2201',480,'5h','20:55','+1 01:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (288,'郑州','成都',2,'G2202',480,'5h','06:00','11:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (289,'兰州','成都',2,'G3501',360,'4.5h','06:05','10:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (290,'成都','兰州',2,'G3502',360,'4.5h','07:10','11:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (291,'贵阳','长沙',2,'G1501',220,'2.5h','07:15','09:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (292,'长沙','贵阳',2,'G1502',220,'2.5h','08:20','10:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (293,'太原','郑州',2,'G601',150,'1.5h','08:25','09:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (294,'郑州','太原',2,'G602',150,'1.5h','09:30','11:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (295,'太原','石家庄',2,'G605',70,'1h','09:35','10:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (296,'石家庄','太原',2,'G606',70,'1h','10:40','11:40');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (297,'北京','昆明',5,'T61',300,'36h','10:45','+1 22:45');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (298,'昆明','北京',5,'T62',300,'36h','11:50','+1 23:50');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (299,'上海','昆明',5,'K739',250,'36h','12:55','+2 00:55');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (300,'昆明','上海',5,'K740',250,'36h','13:00','+2 01:00');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (301,'广州','重庆',5,'K811',200,'24h','13:05','+1 13:05');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (302,'重庆','广州',5,'K812',200,'24h','14:10','+1 14:10');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (303,'郑州','重庆',5,'K909',180,'22h','14:15','+1 12:15');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (304,'重庆','郑州',5,'K910',180,'22h','06:20','+1 04:20');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (305,'南京','成都',5,'K282',220,'28h','07:25','+1 11:25');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (306,'成都','南京',5,'K281',220,'28h','09:30','+1 13:30');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (307,'武汉','成都',5,'K390',200,'24h','06:35','+1 06:35');

INSERT INTO route (RouteID, `from`, `to`, way, number, price, time, departure, arrival) VALUES (308,'成都','武汉',5,'K389',200,'24h','08:40','+1 08:40');

-- Populate the comma-separated route IDs used by city detail panels.
UPDATE city AS c
SET RouteID = (
    SELECT GROUP_CONCAT(r.RouteID ORDER BY r.RouteID SEPARATOR ',')
    FROM route AS r
    WHERE r.rom = c.name OR r.	o = c.name
);