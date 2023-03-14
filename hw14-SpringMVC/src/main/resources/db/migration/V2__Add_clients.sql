insert into client (ID, NAME) values (1, 'Axel');
insert into client (ID, NAME) values (2, 'Zorro');
insert into client (ID, NAME) values (3, 'Tourovich');
insert into phoneData (ID, NUMBER) values (1, '+1111');
insert into phoneData (ID, NUMBER) values (2, '+22222');
insert into phoneData (ID, NUMBER) values (3, '+33333');
update phoneData set clientid=1 where id=1;
update phoneData set clientid=2 where id=2;
update phoneData set clientid=3 where id=3;
