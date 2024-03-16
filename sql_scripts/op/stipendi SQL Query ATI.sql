WITH dirigenteStipendio AS (
	select nome, cognome, matricola, stipendiobase, 
			CASE WHEN datediff(curdate(), datapromozione) < 3650 THEN ROUND( (year(curdate()) - year(datapromozione))/100, 2 )
			ELSE 0.1
			END AS bonus
    from Dirigente natural join dipendente
)
select *, ROUND(s.stipendiobase * ( 1 + s.bonus), 2) AS stipendioMensile from dirigenteStipendio s;


#DECLARE @startOfCurrentMonth DATETIME;
SET @startOfCurrentMonth = DATE_ADD( CURDATE(), INTERVAL - day(curdate()) + 1 DAY);
WITH dipendentiPresenze AS (
	select matricola, count(*) as presenze
    from registropresenze p
    where DATE(p.data) >= DATE_SUB( @startOfCurrentMonth, INTERVAL 1 MONTH )
				AND DATE(p.data) < @startOfCurrentMonth
	group by matricola
)
SELECT *, ROUND(tariffagiorno * presenze) AS stipendio FROM dipendentiPresenze NATURAL JOIN trasportatore;


WITH dipendentiPresenze AS (
	select matricola, count(*) as presenze
    from registropresenze p
    where DATE(p.data) >= DATE_SUB( @startOfCurrentMonth, INTERVAL 1 MONTH )
				AND DATE(p.data) < @startOfCurrentMonth
	group by matricola
)
SELECT *, ROUND(tariffagiorno * presenze, 2) AS stipendio FROM dipendentiPresenze NATURAL JOIN impiegato;

	