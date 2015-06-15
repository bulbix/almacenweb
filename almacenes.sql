delete from paciente;
delete from cie09;
delete from partidas_ceye;
delete from cat_area_ceye;
delete from articulo_ceye ;
delete from articulo;
delete from cat_area;

insert into paciente values(1,'N','Prado','Luis','N-000000000000','Prado');
insert into cie09 values(1,1,'Diagnostico1');
insert into partidas_ceye values('1','','Partida1');
insert into cat_area_ceye values(1,'C','Area1');
insert into articulo_ceye values(1,'C','Articulo1',0,'1','Presenta1','Unidad1');

insert into articulo values(1,'F','Articulo1',0,'1','Presenta1','Unidad1');
insert into cat_area values(1,'Area1');

select * from partidas_ceye;
select * from perfil;
select * from articulo;
select * from cierre;