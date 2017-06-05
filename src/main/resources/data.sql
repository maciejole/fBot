insert into PRODUCTS (id, name ,description,IMAGE_URL )
values
(1, 'pralka','Indesit IWSD51051CECOPL to wolnostojąca pralka','https://8.allegroimg.com/s1024/056bae/281901a64b50bc69ba57e208ffd8/Pralka-Indesit-IWSD51051CECOPL-281901a64b50bc69ba57e208ffd8'),
(2, 'pralka','Cechy wolnostojącej pralki ładowanej od góry Whirlpool: kolor biały.  ','http://www.whirlpool.pl/digitalassets/Picture/web1000x1000/859334649050_1000x1000_perspective.jpg' ),
(3, 'pralka','Electrolux EWF11274BW to nowoczesna pralka wolnostojąca, ładowana od frontu','https://e.allegroimg.com/s1024/007778/178c78194be8a525900078c45b7e/Pralka-Electrolux-EWF11274BW-przod'),
(4, 'kuchnia','Amica 57GEH2.33ZpPF(W) to biała wolnostojąca kuchnia gazowo-elektryczna','https://2.allegroimg.com/s1024/006114/14d8fc794f9aa09946acc5c30e72/Kuchnia-wolnostojaca-Amica-57GEH2.33ZpPFW-57GEH2.33ZpPFW'),
(5, 'lodówka','Biała chłodziarko-zamrażarka Amica FK205.4 ma zamrażarkę ','https://d.allegroimg.com/s1024/004902/eafa87eb44479fbada01afad438d/Chlodziarko-zamrazarka-Amica-FK205.4-przod'),
(6, 'lodówka','Chłodziarko-zamrażarka Amica FK338.6GBF','https://2.allegroimg.com/s1024/00d435/572099be41efa18803c58b6aa0a2/Chlodziarko-zamrazarka-Amica-FK338.6GBF-Lodowka'),
(7, 'lodówka','Biała chłodziarko-zamrażarka Amica FK205.4 ','https://d.allegroimg.com/s1024/004902/eafa87eb44479fbada01afad438d/Chlodziarko-zamrazarka-Amica-FK205.4-przod'),
(8, 'lodówka','Chłodziarko-zamrażarka Amica FK3296.4F to model wolnostojący','https://6.allegroimg.com/s1024/0015d6/f9edfa9641ed9f06639d88057506/Chlodziarko-zamrazarka-Amica-FK3296.3F-FK3296.3F'),
(9, 'lodówka','Chłodziarka Amica FM1044 to wolnostojące urządzenie z komorą niskich temperatur','https://8.allegroimg.com/s1024/0042ac/757a038e43adae2a24108ecedd98/Chlodziarka-Amica-FM104.4-Chlodziarka'),
(10, 'lodówka','Manta CLF050 to podblatowa chłodziarko-zamrażarka wolnostojąca o wysokości 49cm','https://3.allegroimg.com/s1024/00d44a/09ecb53b43f3a5f823df04cac473/Chlodziarko-zamrazarka-Manta-CLF050-CLF050');

insert into KEYWORDS (id, word)
values
(1, 'TOP3'),
(2, 'TOP2'),
(3, 'TOP1'),
(4, 'Najtańszy'),
(5, 'Najdroższy'),
(6, 'Najwydajniejszy'),
(7, 'Innowacyjy'),
(8, 'Funkcjonalny'),
(9, 'Wielozadaniowy'),
(10, 'Edycja limitowana'),
(11, 'Hit sezonu'),
(12, 'Rabat 10%'),
(13, 'Wyprzedaż'),
(14, 'Gwarancja 5 lat'),
(15, 'Cicha praca');

insert into PRODUCTS_KEYWORDS (PRODUCT_ID,KEYWORDS_ID)
values
(1,1),
(2,2),
(3,3),
(4,4),
(3,5),
(3,6),
(2,7),
(5,8),
(6,9),
(6,10),
(7,11),
(8,12),
(9,13),
(10,14),
(5,15);

insert into Users (id, first_name, last_name, email, password, phone_number, credentials_non_expired, account_non_expired, account_non_locked, enabled)
VALUES
(1, 'Admin', 'Adminski', 'admin', 'admin', '+48789987789', true, true, true, true),
(2, 'Anna', 'Adamska', 'adamska@example.com', 'haslo123', '+48789987789', true, true, true, true),
(3, 'Jan', 'Kowalski', 'kowalski@example.com', 'haslo123', '+48789987789', true, true, true, true),
(4, 'Zofia', 'Kowalska', 'kowalska@example.com', 'haslo123', '+48789987789', true, true, true, true),
(5, 'Ewa', 'Kowalska', 'kowalskaEwa@example.com', 'haslo123', '+48789987789', true, true, true, true),
(6, 'Adam', 'Adamski', 'adamski@example.com', 'haslo123', '+48789987789', true, true, true, true),
(7, 'Jan', 'Nowak', 'messenger.recommendations2017@gmail.com', 'haslo123', '+48789987789', true, true, true, true),
(8, 'test', 'test', 'test', 'test', +48111111111, true, true, true, true);

insert into ROLES (id, authority) VALUES
  (1, 'ROLE_ADMIN'),
  (2, 'ROLE_USER'),
  (3, 'ROLE_ACTUATOR');

INSERT INTO USERS_ROLES (USER_ID, ROLES_ID)
    VALUES (1,1), (2,2), (3,2), (4,2), (5,2), (6,1), (7,2), (8,3);