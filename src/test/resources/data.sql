delete from userroles;
delete from roles;
delete from projects;
delete from users;

insert into roles(roleid,name) values (1,'user'),(2,'admin');

insert into users(userid,username,type,password) values (1,'AshenPhoenix', 'admin', '$2a$10$zZBBKLwBngQ0A./w4WZiSOVMVoNWOTeXgbJgkSgcqAWMExa7XvGhi'),
                                                        (2,'Sample Mentor','mentor','$2a$10$.Pp/t/IQi4K6sKzPmO446OqHLoeqK1bWVCck3vtrT7cP5Zk7slq0K'),
                                                        (3,'Demo Admin', 'admin', '$2a$10$AJTiVjbklBk8mOyrYn25Tub8jJFx9r1C6GbOJ5Kh17H5Q4sxl2K2S');

insert into userroles(userid,roleid) values(1,2),(2,1),(3,2),(1,1),(3,1);

insert into projects(projectid, amount, description, email,name,phone,status,owner_userid) values ( 1,500000,'This project is a test of the backend', 'brendlorend@gmail.com','Demo Project', '555555555','starting',1 );

alter sequence hibernate_sequence restart with 20;