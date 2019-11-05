CREATE TABLE REZERWACJE_LOG (
    ID                  INT GENERATED ALWAYS AS IDENTITY NOT NULL ,
    ID_REZERWACJI       INT,
    DATA                DATE,
    STATUS              CHAR(1),
    CONSTRAINT REZERWACJE_LOG_PK PRIMARY KEY (
        ID
        ) ENABLE
);


-- dodanie pola wolnych_miejsc

ALTER TABLE WYCIECZKI
    ADD liczba_wolnych_miejsc INT;