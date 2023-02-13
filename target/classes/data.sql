delete from usr;
delete from user_role;

insert into usr(id, name, username, phone, password) values (1, 'admin', 'admin', '111-11-11', '$2a$08$w99869BfUf1w2rxE/u97Zey0hewFPbhTxDfR67HBEuIO1U9hQSXYa');
insert into usr(id, name, username, phone, password) values (2, 'master', 'master', '111-11-12', '$2a$08$h1Z09qQ92pFR0HzfIgmS.ujzG7YxaVDRlDTbLYbUvg.OKrZSQP0fm');
insert into usr(id, name, username, phone, password) values (3, 'user', 'user', '111-11-13', '$2a$08$ClOEEDvPbyXgetNPYfeUCul6vbavjxgkYjJSFAgeBuGFR4JmV8Nk.');
insert into user_role(user_id, roles) values (1, 'ADMIN');
insert into user_role(user_id, roles) values (2, 'MASTER');
insert into user_role(user_id, roles) values (3, 'USER');

insert into master_time(category, time, user_id) values ('Манікюр', '09:00', 2);
insert into master_time(category, time, user_id) values ('Манікюр', '11:00', 2);
insert into master_time(category, time, user_id) values ('Манікюр', '14:00', 2);
insert into master_time(category, time, user_id) values ('Манікюр', '16:00', 2);
insert into master_time(category, time, user_id) values ('Манікюр', '18:00', 2);

insert into master_time(category, time, user_id) values ('Зачіски', '10:00', 2);
insert into master_time(category, time, user_id) values ('Зачіски', '12:00', 2);
insert into master_time(category, time, user_id) values ('Зачіски', '15:00', 2);

insert into master_time(category, time, user_id) values ('Косметологія', '13:00', 2);
insert into master_time(category, time, user_id) values ('Косметологія', '17:00', 2);

insert into master_time(category, time, user_id) values ('Інше', '19:00', 2);
insert into master_time(category, time, user_id) values ('Інше', '20:00', 2);

insert into services(name, category) values ('Нарощення нігтів', 'Манікюр');
insert into services(name, category) values ('Гель-лак', 'Манікюр');
insert into services(name, category) values ('Весільний манікюр', 'Манікюр');
insert into services(name, category) values ('Френч', 'Манікюр');
insert into services(name, category) values ('Манікюр без покриття', 'Манікюр');

insert into services(name, category) values ('Стрижка', 'Зачіски');
insert into services(name, category) values ('Накрутка', 'Зачіски');
insert into services(name, category) values ('Укладка', 'Зачіски');
insert into services(name, category) values ('Плетіння', 'Зачіски');
insert into services(name, category) values ('Фарбування', 'Зачіски');

insert into services(name, category) values ('Чистка обличчя', 'Косметологія');
insert into services(name, category) values ('Масаж обличчя', 'Косметологія');
insert into services(name, category) values ('Маска для обличчя', 'Косметологія');
insert into services(name, category) values ('Пілінг', 'Косметологія');
insert into services(name, category) values ('Корекція брів', 'Косметологія');

insert into services(name, category) values ('Татуаж', 'Інше');
insert into services(name, category) values ('Нарощення вій', 'Інше');
insert into services(name, category) values ('Обгортування шоколадом', 'Інше');
insert into services(name, category) values ('Макіяж', 'Інше');
insert into services(name, category) values ('Педікюр', 'Інше');