-- a) wycieczki_osoby(kraj,data, nazwa_wycieczki, imie, nazwisko,status_rezerwacji)
-- bylo gotowe
CREATE OR REPLACE VIEW wycieczki_osoby
  AS
    SELECT
      w.ID_WYCIECZKI,
      w.NAZWA,
      w.KRAJ,
      w.DATA,
      o.IMIE,
      o.NAZWISKO,
      r.STATUS
    FROM WYCIECZKI w
      JOIN REZERWACJE r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
      JOIN OSOBY o ON r.ID_OSOBY = o.ID_OSOBY;


-- b) wycieczki_osoby_potwierdzone (kraj,data, nazwa_wycieczki, imie, nazwisko,status_rezerwacji)

CREATE OR REPLACE VIEW  wycieczki_osoby_potwierdzone AS
    SELECT w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, o.IMIE, o.NAZWISKO, r.STATUS
    FROM WYCIECZKI w
      JOIN REZERWACJE r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
      JOIN OSOBY o ON r.ID_OSOBY = o.ID_OSOBY
    WHERE r.STATUS = 'Z' OR r.STATUS = 'P';

-- c) wycieczki_przyszle (kraj,data, nazwa_wycieczki, imie, nazwisko,status_rezerwacji)

CREATE OR REPLACE VIEW  wycieczki_przyszle AS
    SELECT w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, o.IMIE, o.NAZWISKO, r.STATUS
    FROM WYCIECZKI w
      JOIN REZERWACJE r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
      JOIN OSOBY o ON r.ID_OSOBY = o.ID_OSOBY
    WHERE w.DATA > CURRENT_DATE;

-- d) wycieczki_miejsca(kraj,data, nazwa_wycieczki,liczba_miejsc, liczba_wolnych_miejsc)

CREATE OR REPLACE VIEW  wycieczki_miejsca AS
    SELECT w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, w.LICZBA_MIEJSC, w.LICZBA_MIEJSC - COUNT(r.ID_OSOBY) as liczba_wolnych_miejsc
    FROM WYCIECZKI w
      LEFT JOIN (SELECT * from REZERWACJE WHERE STATUS <> 'A') r ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
    GROUP BY w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, w.LICZBA_MIEJSC;

-- e) dostępne_wyciezki(kraj,data, nazwa_wycieczki,liczba_miejsc, liczba_wolnych_miejsc)

CREATE OR REPLACE VIEW  dostepne_wycieczki AS
    SELECT w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, w.LICZBA_MIEJSC,
           w.LICZBA_MIEJSC - COUNT(r.ID_OSOBY) as liczba_wolnych_miejsc
    FROM WYCIECZKI w
      LEFT JOIN (SELECT * from REZERWACJE WHERE STATUS <> 'A') r
          ON w.ID_WYCIECZKI = r.ID_WYCIECZKI
    WHERE w.DATA > CURRENT_DATE AND (w.LICZBA_MIEJSC - (SELECT COUNT(*)
        from REZERWACJE rez WHERE STATUS <> 'A' AND rez.ID_WYCIECZKI = w.ID_WYCIECZKI)) > 0
    GROUP BY w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, w.LICZBA_MIEJSC;

-- f) rezerwacje_do_ anulowania – lista niepotwierdzonych rezerwacji które powinne zostać
-- anulowane, rezerwacje przygotowywane są do anulowania na tydzień przed wyjazdem)

CREATE OR REPLACE VIEW  rezerwacje_do_anulowania AS
    SELECT r.NR_REZERWACJI, r.STATUS, o.IMIE, o.NAZWISKO, r.ID_WYCIECZKI
    FROM REZERWACJE r
        JOIN OSOBY o on r.ID_OSOBY = o.ID_OSOBY
        JOIN WYCIECZKI w on r.ID_WYCIECZKI = w.ID_WYCIECZKI
    WHERE r.STATUS = 'N' AND (w.DATA - CURRENT_DATE) < 7;