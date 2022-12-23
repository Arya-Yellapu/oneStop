create table login(
email varchar(60) primary key,
password varchar(100),
oauthprovider varchar(35),
role varchar(35),
firstname varchar(35),
lastname varchar(35),
mobile varchar(35),
lastlogin varchar(35));

create table restaurants(
id int primary key,
restaurant_name varchar(35),
location varchar(35));

create table slots(
id int primary key,
date_field date,
slot varchar(35),
status varchar(35),
reservationid varchar(35),
res_id varchar(35),
constraint constraint_a foreign key(res_id) references restaurants(id));

insert into restaurants values(1,'Restaurant A', 'Location A');

insert into slots(id,date_field,slot,status,res_id) values(1,'2022-12-14','12:00PM','Available',1);

insert into login(email,password,role,firstname,lastname,mobile) values('test@testmail.com','pass bcrypt encoded','USER','name1','name2','1234567890');