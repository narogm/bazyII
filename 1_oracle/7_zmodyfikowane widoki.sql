-- d) wycieczki_miejsca(kraj,data, nazwa_wycieczki,liczba_miejsc, liczba_wolnych_miejsc)

CREATE OR REPLACE VIEW  wycieczki_miejsca2 AS
    SELECT w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, w.LICZBA_MIEJSC, w.LICZBA_WOLNYCH_MIEJSC
    FROM WYCIECZKI w
      LEFT JOIN (SELECT * from REZERWACJE WHERE STATUS <> 'A') r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI;

-- e) dostępne_wyciezki(kraj,data, nazwa_wycieczki,liczba_miejsc, liczba_wolnych_miejsc)

CREATE OR REPLACE VIEW  dostepne_wycieczki2 AS
    SELECT w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, w.LICZBA_MIEJSC, w.LICZBA_WOLNYCH_MIEJSC
    FROM WYCIECZKI w
      LEFT JOIN (SELECT * from REZERWACJE WHERE STATUS <> 'A') r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
    WHERE w.DATA > CURRENT_DATE AND w.LICZBA_WOLNYCH_MIEJSC > 0;

-- procedura do wyliczenia wolnych miejsc

CREATE OR REPLACE PROCEDURE przelicz AS
    BEGIN
       UPDATE WYCIECZKI w
            SET LICZBA_WOLNYCH_MIEJSC = LICZBA_MIEJSC - (SELECT COUNT(*) FROM REZERWACJE r
                                                            WHERE w.ID_WYCIECZKI = r.ID_WYCIECZKI
                                                                AND r.STATUS <> 'A');
    END;


-- procedury pobierajace

-- d) dostepne_wycieczki(kraj, data_od, data_do)

CREATE OR REPLACE FUNCTION dostepne_wycieczki_proc2(kraj VARCHAR2(50), data_od DATE, data_do DATE)
    return wycieczki_table as result wycieczki_table;

    BEGIN
       IF data_od > data_do
           THEN
                raise_application_error(-20003, 'Podany przedział dat jest nieprawidłowy');
       END IF;

        SELECT wycieczki_type(w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, w.OPIS, w.LICZBA_MIEJSC) BULK COLLECT INTO result
        FROM WYCIECZKI w
        WHERE w.KRAJ = dostepne_wycieczki_proc2.kraj
          AND w.DATA >= data_od
          AND w.DATA <= data_do
          AND w.LICZBA_WOLNYCH_MIEJSC > 0;

       return result;
    END dostepne_wycieczki_proc2;

-- procedury modyfikujace

CREATE OR REPLACE PROCEDURE dodaj_rezerwacje2(id_wycieczki INT, id_osoby INT) AS
    tmp INT;
    BEGIN
        SELECT COUNT(*) INTO tmp FROM WYCIECZKI w WHERE w.ID_WYCIECZKI = dodaj_rezerwacje2.id_wycieczki;
        IF tmp = 0 THEN
            raise_application_error(-20001, 'Wycieczka o podanym id nie istnieje');
        END IF;

        SELECT COUNT(*) INTO tmp FROM OSOBY o WHERE o.ID_OSOBY = dodaj_rezerwacje2.id_osoby;
        IF tmp = 0 THEN
            raise_application_error(-20002, 'Osoba o podanym id nie istnieje');
        END IF;

        SELECT COUNT(*) INTO tmp FROM REZERWACJE r
            WHERE r.ID_WYCIECZKI = dodaj_rezerwacje2.id_wycieczki
                AND r.ID_OSOBY = dodaj_rezerwacje2.id_osoby;
        IF tmp > 0 THEN
            raise_application_error(-20004, 'Rezerwacja o podanych parametrach juz istnieje');
        END IF;

        SELECT COUNT(*) INTO tmp FROM WYCIECZKI_PRZYSZLE wp
            WHERE wp.ID_WYCIECZKI = dodaj_rezerwacje2.id_wycieczki;
        IF tmp = 0 THEN
            raise_application_error(-20010, 'Nie mozna dodac rezerwacji do wycieczki, ktora sie juz odbyla');
        END IF;

        SELECT LICZBA_WOLNYCH_MIEJSC INTO tmp FROM WYCIECZKI w
            WHERE w.ID_WYCIECZKI = dodaj_rezerwacje2.id_wycieczki;

            IF tmp = 0 THEN
                raise_application_error(-20006, 'Brak wolnych miejsc');
            END IF;

        INSERT INTO REZERWACJE(id_wycieczki, id_osoby, STATUS)
        VALUES (dodaj_rezerwacje2.id_wycieczki, dodaj_rezerwacje2.id_osoby, 'N');
    END dodaj_rezerwacje2;


--------

CREATE OR REPLACE PROCEDURE zmien_status_rezerwacji2(id_rezerwacji INT, status CHAR) AS
    tmp INT;

    BEGIN
        SELECT COUNT(*) INTO tmp FROM REZERWACJE r WHERE r.NR_REZERWACJI = id_rezerwacji;
        IF tmp = 0 THEN
            raise_application_error(-20005, 'Rezerwacja o podanym id nie istnieje');
        END IF;

        IF status <> 'A'
            THEN
            SELECT LICZBA_WOLNYCH_MIEJSC INTO tmp FROM WYCIECZKI w
              JOIN REZERWACJE r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
            WHERE r.NR_REZERWACJI = id_rezerwacji;

            IF tmp = 0 THEN
                raise_application_error(-20006, 'Brak wolnych miejsc');
            END IF;
        END IF;

        UPDATE REZERWACJE SET STATUS = zmien_status_rezerwacji2.status
            WHERE NR_REZERWACJI = id_rezerwacji;

    END zmien_status_rezerwacji2;


----

CREATE PROCEDURE zmien_liczbe_miejsc2(id_wycieczki INT, liczba_miejsc INT) AS
    tmp INT;
    BEGIN
        SELECT COUNT(*) INTO tmp FROM WYCIECZKI w WHERE w.ID_WYCIECZKI = zmien_liczbe_miejsc2.id_wycieczki;

        IF tmp = 0 THEN
            raise_application_error(-20001, 'Wycieczka o podanym id nie istnieje');
        END IF;

        SELECT LICZBA_MIEJSC - LICZBA_WOLNYCH_MIEJSC INTO tmp FROM WYCIECZKI w
            WHERE w.ID_WYCIECZKI = zmien_liczbe_miejsc2.id_wycieczki;

        IF liczba_miejsc < tmp OR liczba_miejsc < 0
            THEN
                raise_application_error(-20007, 'Podano nieprawidlowa wartosc dotyczaca liczby miejsc');
        END IF;

        UPDATE WYCIECZKI SET LICZBA_MIEJSC = zmien_liczbe_miejsc2.liczba_miejsc
            WHERE ID_WYCIECZKI = zmien_liczbe_miejsc2.id_wycieczki;
    END;