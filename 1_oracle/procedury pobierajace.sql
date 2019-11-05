-- a) uczestnicy_wycieczki (id_wycieczki), procedura ma zwracać podobny zestaw danych jak widok wycieczki_osoby

CREATE OR REPLACE TYPE ret_type AS object (
    nazwa_wycieczki     VARCHAR(100),
    kraj                VARCHAR(50),
    "data"              DATE,
    imie                VARCHAR(50),
    nazwisko            VARCHAR(50),
    status              CHAR(1)
);

CREATE OR REPLACE TYPE ret_table IS TABLE OF ret_type;

CREATE OR REPLACE FUNCTION uczestnicy_wycieczki(id INT) return wycieczki_table as result wycieczki_table;
    tmp INT;
    BEGIN
        SELECT COUNT(*) INTO tmp FROM WYCIECZKI w WHERE w.ID_WYCIECZKI = id;
        IF tmp = 0 THEN
            raise_application_error(-20001, 'Wycieczka o podanym id nie istnieje');
        END IF;

        SELECT ret_type(w.NAZWA, w.KRAJ, w.DATA, o.IMIE, o.NAZWISKO, r.STATUS) BULK COLLECT INTO result
        FROM WYCIECZKI w
            JOIN REZERWACJE r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
            JOIN OSOBY o ON r.ID_OSOBY = o.ID_OSOBY
        WHERE w.ID_WYCIECZKI = id AND r.STATUS <> 'A';

        return result;
    END uczestnicy_wycieczki;

-- b) rezerwacje_osoby(id_osoby), procedura ma zwracać podobny zestaw danych jak widok wycieczki_osoby

CREATE OR REPLACE FUNCTION rezerwacje_osoby(id_osoby INT) return wycieczki_table as result wycieczki_table;
    tmp INT;
    BEGIN
        SELECT COUNT(*) INTO tmp FROM OSOBY o WHERE o.ID_OSOBY = rezerwacje_osoby.id_osoby;
        IF tmp = 0 THEN
            raise_application_error(-20002, 'Osoba o podanym id nie istnieje');
        END IF;

        SELECT ret_type(w.NAZWA, w.KRAJ, w.DATA, o.IMIE, o.NAZWISKO, r.STATUS) BULK COLLECT INTO result
        FROM WYCIECZKI w
            JOIN REZERWACJE r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
            JOIN OSOBY o ON r.ID_OSOBY = o.ID_OSOBY
        WHERE o.ID_OSOBY = rezerwacje_osoby.id_osoby AND r.STATUS <> 'A';

        return result;
    END rezerwacje_osoby;

-- c) przyszle_rezerwacje_osoby(id_osoby)

CREATE OR REPLACE FUNCTION przyszle_rezerwacje_osoby(id_osoby INT) return wycieczki_table as result wycieczki_table;
    tmp INT;
    BEGIN
        SELECT COUNT(*) INTO tmp FROM OSOBY o WHERE o.ID_OSOBY = przyszle_rezerwacje_osoby.id_osoby;
        IF tmp = 0 THEN
            raise_application_error(-20002, 'Osoba o podanym id nie istnieje');
        END IF;

        SELECT ret_type(w.NAZWA, w.KRAJ, w.DATA, o.IMIE, o.NAZWISKO, r.STATUS) BULK COLLECT INTO result
        FROM WYCIECZKI w
            JOIN REZERWACJE r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
            JOIN OSOBY o ON r.ID_OSOBY = o.ID_OSOBY
        WHERE o.ID_OSOBY = przyszle_rezerwacje_osoby.id_osoby AND r.STATUS <> 'A' AND CURRENT_DATE < w.DATA;

        return result;
    END przyszle_rezerwacje_osoby;

-- d) dostepne_wycieczki(kraj, data_od, data_do)

CREATE OR REPLACE TYPE wycieczki_type AS object (
  id_wycieczki      NUMBER,
  nazwa             VARCHAR2(100),
  kraj              VARCHAR2(50),
  "data"            DATE,
  opis              VARCHAR2(200),
  liczba_miejsc     NUMBER
);

CREATE OR REPLACE TYPE wycieczki_table IS TABLE OF wycieczki_type;

CREATE OR REPLACE FUNCTION dostepne_wycieczki_proc(kraj VARCHAR2(50), data_od DATE, data_do DATE)
    return wycieczki_table as result wycieczki_table;

    BEGIN
       IF data_od > data_do
           THEN
                raise_application_error(-20003, 'Podany przedział dat jest nieprawidłowy');
       END IF;

        SELECT Wycieczki_type(w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, w.OPIS, w.LICZBA_MIEJSC) BULK COLLECT INTO result
        FROM WYCIECZKI w
        WHERE w.KRAJ = dostepne_wycieczki_proc.kraj
          AND w.DATA >= data_od
          AND w.DATA <= data_do
          AND w.LICZBA_MIEJSC > (SELECT COUNT(*) FROM REZERWACJE r WHERE STATUS <> 'A' AND w.ID_WYCIECZKI = r.ID_WYCIECZKI);

       return result;
    END dostepne_wycieczki_proc;