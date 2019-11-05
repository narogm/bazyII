-- a) dodaj_rezerwacje(id_wycieczki, id_osoby), procedura powinna kontrolować czy wycieczka
-- jeszcze się nie odbyła, i czy sa wolne miejsca

CREATE OR REPLACE PROCEDURE dodaj_rezerwacje(id_wycieczki INT, id_osoby INT) AS
    tmp INT;
    BEGIN
        SELECT COUNT(*) INTO tmp FROM WYCIECZKI w WHERE w.ID_WYCIECZKI = dodaj_rezerwacje.id_wycieczki;
        IF tmp = 0 THEN
            raise_application_error(-20001, 'Wycieczka o podanym id nie istnieje');
        END IF;

        SELECT COUNT(*) INTO tmp FROM OSOBY o WHERE o.ID_OSOBY = dodaj_rezerwacje.id_osoby;
        IF tmp = 0 THEN
            raise_application_error(-20002, 'Osoba o podanym id nie istnieje');
        END IF;

        SELECT COUNT(*) INTO tmp FROM REZERWACJE r
            WHERE r.ID_WYCIECZKI = dodaj_rezerwacje.id_wycieczki
                AND r.ID_OSOBY = dodaj_rezerwacje.id_osoby;
        IF tmp > 0 THEN
            raise_application_error(-20004, 'Rezerwacja o podanych parametrach juz istnieje');
        END IF;

        SELECT COUNT(*) INTO tmp FROM WYCIECZKI_PRZYSZLE wp
            WHERE wp.ID_WYCIECZKI = dodaj_rezerwacje.id_wycieczki;
        IF tmp = 0 THEN
            raise_application_error(-20010, 'Nie mozna dodac rezerwacji do wycieczki, ktora sie juz odbyla');
        END IF;

        SELECT COUNT(*) INTO tmp FROM DOSTEPNE_WYCIECZKI dw
            WHERE dw.ID_WYCIECZKI = dodaj_rezerwacje.id_wycieczki;

            IF tmp = 0 THEN
                raise_application_error(-20006, 'Brak wolnych miejsc');
            END IF;

        INSERT INTO REZERWACJE(id_wycieczki, id_osoby, STATUS) VALUES (dodaj_rezerwacje.id_wycieczki, dodaj_rezerwacje.id_osoby, 'N');
    END dodaj_rezerwacje;

-- b) zmien_status_rezerwacji(id_rezerwacji, status), procedura kontrolować czy możliwa jest
-- zmiana statusu, np. zmiana statusu już anulowanej wycieczki (przywrócenie do stanu
-- aktywnego nie zawsze jest możliwe)

CREATE OR REPLACE PROCEDURE zmien_status_rezerwacji(id_rezerwacji INT, status CHAR) AS
    tmp INT;
    BEGIN
        SELECT COUNT(*) INTO tmp FROM REZERWACJE r WHERE r.NR_REZERWACJI = id_rezerwacji;
        IF tmp = 0 THEN
            raise_application_error(-20005, 'Rezerwacja o podanym id nie istnieje');
        END IF;

        IF status <> 'A'
            THEN
            SELECT COUNT(*) INTO tmp FROM DOSTEPNE_WYCIECZKI dw
              JOIN REZERWACJE r ON dw.ID_WYCIECZKI = r.ID_WYCIECZKI
            WHERE r.NR_REZERWACJI = id_rezerwacji;

            IF tmp = 0 THEN
                raise_application_error(-20006, 'Brak wolnych miejsc');
            END IF;
        END IF;

        UPDATE REZERWACJE SET STATUS = zmien_status_rezerwacji.status
            WHERE NR_REZERWACJI = id_rezerwacji;

    END zmien_status_rezerwacji;

-- c) zmien_liczbe_miejsc(id_wycieczki, liczba_miejsc), nie wszystkie zmiany liczby miejsc są
-- dozwolone, nie można zmniejszyć liczby miesc na wartość poniżej liczby zarezerwowanych miejsc

CREATE PROCEDURE zmien_liczbe_miejsc(id_wycieczki INT, liczba_miejsc INT) AS
    tmp INT;
    BEGIN
        SELECT COUNT(*) INTO tmp FROM WYCIECZKI w WHERE w.ID_WYCIECZKI = zmien_liczbe_miejsc.id_wycieczki;

        IF tmp = 0 THEN
            raise_application_error(-20001, 'Wycieczka o podanym id nie istnieje');
        END IF;

        SELECT LICZBA_MIEJSC - LICZBA_WOLNYCH_MIEJSC INTO tmp FROM WYCIECZKI_MIEJSCA w
            WHERE w.ID_WYCIECZKI = zmien_liczbe_miejsc.id_wycieczki;

        IF liczba_miejsc < tmp OR liczba_miejsc < 0
            THEN
                raise_application_error(-20007, 'Podano nieprawidlowa wartosc dotyczaca liczby miejsc');
        END IF;

        UPDATE WYCIECZKI SET LICZBA_MIEJSC = zmien_liczbe_miejsc.liczba_miejsc
            WHERE ID_WYCIECZKI = zmien_liczbe_miejsc.id_wycieczki;
    END;