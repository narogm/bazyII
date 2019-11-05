INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Adam', 'Kowalski', '87654321', 'tel: 6623');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Jan', 'Nowak', '12345678', 'tel: 2312, dzwonić po 18.00');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Wojciech', 'Jaworski', '12341234', 'tel: 22985');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Wladyslaw', 'Dabrowski', '43214321', 'tel: 76875');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Urszula', 'Tomaszewska', '65020608', 'tel: 42942');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Tymoteusz', 'Majewski', '91093006', 'tel: 66811');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Roman', 'Sokolowski', '48070447', 'tel: 24762');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Marcelina', 'Nowakowska', '46120480', 'tel: 68366');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Izabela', 'Zawadzka', '58030300', 'tel: 90719');

INSERT INTO osoby (imie, nazwisko, pesel, kontakt)
VALUES('Sobieslaw', 'Kucharski', '63071544', 'tel: 22443');



-----------------------------------

INSERT INTO wycieczki (nazwa, kraj, data, opis, liczba_miejsc)
VALUES ('Wycieczka do Paryza','Francja',TO_DATE('2019-01-01','YYYY-MM-DD'),'Ciekawa wycieczka ...',3);
INSERT INTO wycieczki (nazwa, kraj, data, opis, liczba_miejsc)
VALUES ('Piękny Kraków','Polska',TO_DATE('2020-02-03','YYYY-MM-DD'),'Najciekawa wycieczka ...',2);
INSERT INTO wycieczki (nazwa, kraj, data, opis, liczba_miejsc)
VALUES ('Wieliczka','Polska',TO_DATE('2017-03-03','YYYY-MM-DD'),'Zadziwiająca kopalnia ...',2);
INSERT INTO wycieczki (nazwa, kraj, data, opis, liczba_miejsc)
VALUES ('Wieliczka2','Polska',TO_DATE('2019-10-27','YYYY-MM-DD'),'Zadziwiająca kopalnia ...',2);

----------------------------------------------------

INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (1,1,'N');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (2,4,'P');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (1,2,'Z');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (3,3,'A');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (4,7,'N');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (2,8,'P');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (1,5,'Z');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (3,6,'P');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (4,10,'Z');
INSERT INTO rezerwacje(id_wycieczki, id_osoby, status)
VALUES (2,1,'A');