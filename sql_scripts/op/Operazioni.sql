-- Una selezione ordinata su un attributo di una tabella con condizioni AND e OR; Op.13
-- Selezione Consegne in base a Tipo Merce e Data, ordinato per Centro di Distribuzione; I
SELECT * FROM Consegna WHERE TipoMerce=? AND Data=? ORDER BY Citta, Zona;
/* Menu Interattive */


-- Una selezione su due o più tabelle con condizioni; Op14
-- Selezione Dati Merce, Consegna, Mezzo Usato di una specifica consegna; I
select *
from ( consegna c join tipomerce m on c.CodiceMerce = m.Codice ) join mezzo using(targa) 
where CodiceMerce = 'T-TO-200' AND targa = 'AT000022' AND DataConsegna = '2024-01-01';
/* Implementato con Tasto Dettagli */


-- Una selezione aggregata su tutti i valori; Op15
-- Somma di tutti gli stipendi dei dipendenti. B
select sum(stipendioMensile) as SommaStipendi
from dipendentiView;
/* Menu Operazioni */

-- Una selezione aggregata su raggruppamenti; Op16
-- Somma Peso di Tipo Merce trasportata per CD e per Nome Merce. B
select citta, zona, m.nome, round(sum(m.peso),2) as SommaCarico
from consegna c join tipomerce m on c.codicemerce=m.codice
group by citta, zona, m.nome;
/* Menu Operazioni */

-- Una selezione aggregata su raggruppamenti con condizioni; Op17
-- Conta Consegne per CD aventi un numero di consegne maggiore di 2000 B
select citta, zona, count(*) as NumConsegne
from consegna
group by citta, zona
having NumConsegne > 2000;
/* Menu Operazioni */

-- Una selezione aggregata su raggruppamenti con condizioni che includano un’altra funzione di raggruppamento; Op18
-- Mezzo il cui numero di consegne è il più alto B
with ContaConsegne AS(
select targa, count(*) as NumConsegne
from consegna
group by targa
)
select *
from ContaConsegne join mezzo using(targa)
where numconsegne = (select max(numconsegne) as max from ContaConsegne);
/* Menu Operazioni */

-- Op19: Selezione Trasportatori che hanno guidato Camion (Autotreni e Motrici) ma non Treni;
-- Selezione Trasportatori che hanno guidato Camion ma non Treni. B
select *
from trasportatore
where matricola in (select distinct matricola from consegna join mezzo using(targa) where tipologia='Autotreno' OR tipologia='Motrice')
and matricola not in (select distinct matricola from consegna join mezzo using(targa) where tipologia='Treno');
/* Menu Operazioni */


-- Selezione del Tipo di Merce (Nome, Codice) che è stata consegnata in tutti i Centri di Distribuzione; Op20.a B
-- Divisione
SELECT Nome, Codice FROM TipoMerce M 
WHERE NOT EXISTS (SELECT * FROM CentroDistribuzione D 
				WHERE NOT EXISTS (SELECT * FROM Consegna C 
									WHERE M.Codice = C.CodiceMerce 
									AND D.Zona = C.Zona 
                                    AND D.Citta = C.Citta) );
/* Menu Operazioni */



-- HO INVERTITO: I centri distribuzione che hanno ricevuto tutti i tipi di merci; Op20.b B
SELECT * FROM centrodistribuzione D
WHERE NOT EXISTS (SELECT * FROM tipomerce M
				WHERE NOT EXISTS (SELECT * FROM Consegna C
									WHERE M.Codice = C.CodiceMerce 
									AND D.Zona = C.Zona 
                                    AND D.Citta = C.Citta) );
/*Menu Operazioni -- Non in Documento*/

/*
-- INSERT aggiunte in popolamento ATI

-- Popolamento Reso Necessario per Controllo Query 20,b
insert into consegna values 
('2024-01-04', 'M-FR-001','Roma','Centrale','DQ096DQ','TR0004'),
('2024-01-04', 'M-FR-007','Roma','Centrale','DG086DG','TR0004'),
('2024-01-04', 'M-TO-026','Roma','Centrale','BK038BK','TR0008');


-- Popolamento Reso Necessario per Controllo Query 20,a

INSERT INTO consegna values
('2024-01-05', 'T-TO-200','Bologna','Periferico','AT000022','TR0004'),
('2024-01-05', 'T-TO-200','Jesi','Centrale','AT000021','TR0005'),
('2024-01-05', 'T-TO-200','Lecce','Periferico','AT000020','TR0007'),
('2024-01-05', 'T-TO-200','Teramo','Centrale','AT000003','TR0008');
*/

-- Selezione Impiegati in Determinata Sede con Annesso Dirigente; Op21 I
select * 
from dipendente
where matricola IN (select matricola from impiegato where codicesede=(select codicesede from sede where citta='?'))
or matricola IN (select matricola from dirigente where codicesede=(select codicesede from sede where citta='?'));

-- Implementata come segue per migliorare inserimento input da utente
WITH recursive sedeScelta AS (
    SELECT codicesede FROM sede WHERE citta = ?
)
SELECT *
FROM dipendente
WHERE matricola IN (SELECT matricola FROM impiegato WHERE codicesede IN (SELECT codicesede FROM sedeScelta))
   OR matricola IN (SELECT matricola FROM dirigente WHERE codicesede IN (SELECT codicesede FROM sedeScelta));
/* Menu Interattive */

-- Somma Consumi di ogni Mezzo per quell’anno; Op22 I
select targa, round(sum(consumo * distanza),2) AS ConsumoAnnuo
from consegna join mezzo using(targa) join centrodistribuzione using(citta,zona)
where YEAR(dataconsegna) = ?
group by targa;
/*Menu Interattive */


-- Selezione Consegne per Quel Giorno; Op23 I  //Implementato con Filtro
select * from consegna where dataconsegna=curdate();

-- Calcolo Stipendio per Dipendente con Nome e Cognome; Op24 I //Implementato con Filtro
select * from dipendentiView where nome=? AND cognome=?;

-- Oppure //Implementato con Filtro
select * from dipendentiView where cognome LIKE '?%' AND nome LIKE '?%'; 


-- Numero consegne totali per Trasportatore; Op25 B
select matricola, numeroconsegne from trasportatore;
/* Tabella Base */