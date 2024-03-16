drop database if exists ATI;
create database if not exists ATI;

USE ATI;

/*
• 	-TipoMerce (Codice, Nome, Peso)
•	Consegna (DataConsegna, CodiceMerce, Città, Zona, Targa, Matricola)
•	-Mezzo (Targa, Tipologia, Marca, CaricoMax, Consumo)
•	-TrasportabilitàMezzo (NomeCategoria, Targa)
•	-Categoria (NomeCategoria)
•	-Trasportatore (Matricola, NumeroConsegne, TariffaGiorno)
•	-Dirigente (Matricola, StipendioBase, Bonus, DataPromozione, CodiceSede)
•	-Impiegato (Matricola, TariffaGiorno, CodiceSede)
•	-Dipendente (Matricola, Nome, Cognome, Stipendio, DataNascita, AttualeDipendente)
•	RegistroPresenze (Matricola, Data)
•	-Sede (CodiceSede, Via, Città, CAP, Email, Telefono)
•	CentroDistribuzione (Zona, Città, Distanza)
*/


CREATE TABLE TipoMerce(
	Codice char(8) not null,
    Nome varchar(255) not null,
    Peso double not null,
    PRIMARY KEY (Codice)
);

CREATE TABLE Sede(
	CodiceSede char(7) not null,
	Via varchar(255) not null, 
	Citta varchar(255) not null,
	CAP varchar(5) not null,
	Email varchar(255) not null,
	Telefono varchar(255) not null, 
	PRIMARY KEY(CodiceSede)
);


CREATE TABLE CentroDistribuzione(
	Citta varchar(127) not null,
	Zona varchar(127) not null, 
	Distanza int not null,
	PRIMARY KEY(Citta, Zona)
);

CREATE TABLE Mezzo(
	Targa varchar(8) not null,
	Tipologia varchar(127) not null,
	CaricoMax double not null,
	Marca varchar(127) not null,
	Consumo double not null,
	PRIMARY KEY (Targa)
);


CREATE TABLE Categoria(
	NomeCategoria varchar(127) not null,
	PRIMARY KEY (NomeCategoria)
);


CREATE TABLE TrasportabilitaMezzo(
	NomeCategoria varchar(127) not null,
	Targa varchar(8) not null,
	FOREIGN KEY (NomeCategoria) REFERENCES Categoria(NomeCategoria),
	FOREIGN KEY (Targa) REFERENCES Mezzo(Targa),
	PRIMARY KEY (Targa, NomeCategoria)
);


CREATE TABLE Dipendente(
	Matricola char(6) not null,
	Cognome varchar(255) not null,
	Nome varchar(255) not null,	
	DataNascita date not null, 
	AttualeDipendente BOOLEAN not null DEFAULT true,
	#Stipendio double AS, 
	PRIMARY KEY (Matricola)
);

CREATE TABLE Impiegato(
	Matricola char(6) not null, 
	TariffaGiorno double not null,
	CodiceSede char(7) not null,
	PRIMARY KEY(Matricola),
	FOREIGN KEY(Matricola) REFERENCES Dipendente(Matricola),
	FOREIGN KEY(CodiceSede) REFERENCES Sede(CodiceSede) 
);

CREATE TABLE Trasportatore(
	Matricola char(6) not null, 
	NumeroConsegne int not null DEFAULT 0,
	TariffaGiorno double not null,
	PRIMARY KEY(Matricola),
	FOREIGN KEY(Matricola) REFERENCES Dipendente(Matricola)
);

CREATE TABLE Dirigente(
	Matricola char(6) not null,
	StipendioBase double not null,
	#Bonus double, 
	DataPromozione date not null, 
	CodiceSede char(7) not null,
	PRIMARY KEY(Matricola),
	FOREIGN KEY(Matricola) REFERENCES Dipendente(Matricola),
	FOREIGN KEY(CodiceSede) REFERENCES Sede(CodiceSede) 
);
/*
DELIMITER //
DROP TRIGGER IF EXISTS BonusCheck //
CREATE TRIGGER BonusCheck BEFORE INSERT ON Dirigente
FOR EACH ROW
BEGIN
IF YEAR(New.DataPromozione) - YEAR(Today) > 10
SET Bonus = 0.1;
ELSE
SET Bonus = ( YEAR(New.DataPromozione) - YEAR(Today) )/ 100
END IF;
END //
DELIMITER;
*/ 


CREATE TABLE Consegna(
	DataConsegna date not null,
	CodiceMerce char(8) not null,
	Citta varchar(127) not null,
	Zona varchar(127) not null, 
	Targa varchar(8) not null,
	Matricola char(6) not null,
	PRIMARY KEY(CodiceMerce, Citta, Zona, DataConsegna),
	FOREIGN KEY(Matricola) REFERENCES Dipendente(Matricola),
	FOREIGN KEY(CodiceMerce) REFERENCES TipoMerce(Codice),
	FOREIGN KEY(Citta, Zona) REFERENCES CentroDistribuzione(Citta, Zona),
	FOREIGN KEY(Targa) REFERENCES Mezzo(Targa)
);

CREATE TABLE RegistroPresenze(
	Matricola char(6) not null,
	Data date not null,
	PRIMARY KEY(Matricola, Data),
	FOREIGN KEY(Matricola) REFERENCES Dipendente(Matricola)
);


-- Aggiorna Numero Consegne in Insert
DROP TRIGGER IF EXISTS AggiornaConsegneInsert;
delimiter //
CREATE TRIGGER AggiornaConsegneInsert 
AFTER INSERT ON Consegna
FOR EACH ROW
BEGIN
	IF NEW.Matricola IS NOT NULL THEN
		UPDATE Trasportatore SET NumeroConsegne = NumeroConsegne + 1 WHERE Matricola = NEW.Matricola;
	END IF;
END;
//
delimiter ;

-- Aggiorna Numero Consegne in Delete
DROP TRIGGER IF EXISTS AggiornaConsegneRimuovi;
delimiter //
CREATE TRIGGER AggiornaConsegneRimuovi 
AFTER DELETE ON Consegna
FOR EACH ROW
BEGIN
	IF OLD.Matricola IS NOT NULL THEN
		UPDATE Trasportatore SET NumeroConsegne = NumeroConsegne - 1 WHERE Matricola = OLD.Matricola;
	END IF;
END;
//
delimiter ;

-- Aggiorna Numero Consegne in Update
DROP TRIGGER IF EXISTS AggiornaConsegneUpdate;
delimiter //
CREATE TRIGGER AggiornaConsegneUpdate 
AFTER UPDATE ON Consegna
FOR EACH ROW
BEGIN
	IF NEW.Matricola IS NOT NULL THEN
		UPDATE Trasportatore SET NumeroConsegne = NumeroConsegne + 1 WHERE Matricola = NEW.Matricola;
	END IF;

	IF OLD.Matricola IS NOT NULL THEN
		UPDATE Trasportatore SET NumeroConsegne = NumeroConsegne - 1 WHERE Matricola = OLD.Matricola;
	END IF;
END;
//
delimiter ;


/* NON POSSIBILE IN MYSQL
-- Aggiorna Numero Consegne in Update
DROP TRIGGER IF EXISTS AggiornaConsegneAll;
delimiter //
CREATE TRIGGER AggiornaConsegneAll 
AFTER INSERT, UPDATE, DELETE ON Consegna -- NON POSSIBILE TRIGGER MULTIPLO
FOR EACH ROW
BEGIN
	IF NEW.Matricola IS NOT NULL THEN
		UPDATE Trasportatore SET NumeroConsegne = NumeroConsegne + 1 WHERE Matricola = NEW.Matricola;
	END IF;
	IF OLD.Matricola IS NOT NULL THEN
		UPDATE Trasportatore SET NumeroConsegne = NumeroConsegne - 1 WHERE Matricola = OLD.Matricola;
	END IF;
END;
//
delimiter ;
*/

-- Controlla se il Codice Merce è compatibile con il mezzo assegnato:
-- Controllo su Tipologia (Treno, Autotreno, Motrice)
-- Controllo su Categoria di Merce trasportata con Trasportabilità Mezzo
DROP TRIGGER IF EXISTS CheckConsegnaMezzo;
DELIMITER //
CREATE TRIGGER CheckConsegnaMezzo
BEFORE INSERT ON Consegna
FOR EACH ROW
BEGIN
    DECLARE tipoMezzo VARCHAR(50);
    DECLARE trasportabilita VARCHAR(50);

    IF NEW.CodiceMerce REGEXP '^T-' THEN
        SET tipoMezzo = (SELECT Tipologia FROM Mezzo m WHERE m.Targa = NEW.Targa);
        IF tipoMezzo IS NULL OR tipoMezzo <> 'Treno' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipologia Mezzo non Compatibile con Merce';
        END IF;
    ELSEIF NEW.CodiceMerce REGEXP '^A-' THEN
        SET tipoMezzo = (SELECT Tipologia FROM Mezzo m WHERE m.Targa = NEW.Targa);
        IF tipoMezzo IS NULL OR tipoMezzo <> 'Autotreno' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipologia Mezzo non Compatibile con Merce';
        END IF;
    ELSEIF NEW.CodiceMerce REGEXP '^M-' THEN
        SET tipoMezzo = (SELECT Tipologia FROM Mezzo m WHERE m.Targa = NEW.Targa);
        IF tipoMezzo IS NULL OR tipoMezzo <> 'Motrice' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipologia Mezzo non Compatibile con Merce';
        END IF;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Formato Codice Mezzo Errato';
    END IF;
	
	IF NEW.CodiceMerce REGEXP '^.{2}AL' THEN
		SET trasportabilita = 'Alimentare';
	ELSEIF NEW.CodiceMerce REGEXP '^.{2}TO' THEN
		SET trasportabilita = 'Tossico';
	ELSEIF NEW.CodiceMerce REGEXP '^.{2}FR' THEN
		SET trasportabilita = 'Fragile';
	ELSEIF NEW.CodiceMerce REGEXP '^.{2}ST' THEN
		SET trasportabilita = 'Stabile';
	ELSEIF NEW.CodiceMerce REGEXP '^.{2}IN' THEN
		SET trasportabilita = 'Infiammabile';
	ELSE SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Trigger: Codice Merce non Valido';
	
	END IF;
	
	IF trasportabilita NOT IN (SELECT NomeCategoria FROM TrasportabilitaMezzo WHERE Targa = NEW.Targa ) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Trasportabilità Mezzo non Compatibile con Merce';
	END IF;
END;
//
DELIMITER ;

/*
-- Aggiorna Tabella Collagata per Dipendente Dopo Insert
DROP TRIGGER IF EXISTS Aggiungi_In_Categoria_Dipendente;
delimiter //
CREATE TRIGGER Aggiungi_In_Categoria_Dipendente
AFTER INSERT ON Dipendente
FOR EACH ROW
BEGIN
	IF NEW.Matricola IS NOT NULL THEN
		IF NEW.Matricola REGEXP '^TR' THEN
			INSERT INTO Trasportatore(Matricola, TariffaGiorno) VALUES (NEW.Matricola, 68.5);
		ELSEIF NEW.Matricola REGEXP '^IM' THEN
			INSERT INTO Impiegato(Matricola, TariffaGiorno, CodiceSede) VALUES (NEW.Matricola, 60, 'SEDE001');
		ELSEIF NEW.Matricola NOT REGEXP '^DG' THEN	
			-- INSERT INTO Dirigente(Matricola, StipendioBase, DataPromozione, CodiceSede) VALUES (NEW.Matricola, 1500, now(), 'SEDE001');
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Trigger: Formato Matricola non valido';
		END IF;
	END IF;
END;
//
delimiter ;
*/