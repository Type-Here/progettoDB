USE ATI;

DROP VIEW IF EXISTS dipendentiView;
 
-- SET @startOfCurrentMonth = DATE_ADD( CURDATE(), INTERVAL - day(curdate()) + 1 DAY);
CREATE VIEW dipendentiView AS (
WITH recursive dirigenteStipendio AS (
	select matricola, stipendiobase, 
			CASE WHEN datediff(curdate(), datapromozione) < 3650 THEN ROUND( (year(curdate()) - year(datapromozione))/100, 2 )
			ELSE 0.1
			END AS bonus
    from Dirigente
)
	SELECT  *,
		CASE 
        WHEN dip.matricola IN (SELECT matricola FROM dirigente) 
			THEN (SELECT ROUND(dir.stipendiobase * ( 1 + dir.bonus), 2) FROM dirigenteStipendio dir WHERE dir.matricola = dip.matricola)
            
		WHEN dip.matricola IN (SELECT matricola FROM trasportatore)
			THEN (SELECT ROUND(tariffagiorno * presenze, 2) 
				  FROM trasportatore t LEFT JOIN (select matricola, count(*) as presenze
												  from registropresenze p
												  where DATE(p.data) >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01')
												  AND DATE(p.data) < LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))
												  group by matricola
									) dipendentiPresenze USING (matricola)
				 WHERE dip.matricola = t.Matricola
				)
                
		WHEN dip.matricola IN (SELECT matricola FROM impiegato)
			THEN (SELECT ROUND(tariffagiorno * presenze, 2) 
				  FROM impiegato i LEFT JOIN (select matricola, count(*) as presenze
												  from registropresenze p
												  where DATE(p.data) >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01')
												  AND DATE(p.data) < LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))
												  group by matricola
									) dipendentiPresenze USING (matricola)
				 WHERE dip.matricola = i.Matricola
				)
		ELSE 0
		END AS stipendioMensile
	FROM dipendente dip
    WHERE attualedipendente = true
);


/*  LEFT JOIN dirigenteStipendio dir USING(matricola)
    LEFT JOIN (select matricola, count(*) as presenze
				from registropresenze p
				where DATE(p.data) >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01')
						AND DATE(p.data) < LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))
				group by matricola
			) dipendentiPresenze USING (matricola)
*/