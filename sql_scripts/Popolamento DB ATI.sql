USE ATI;

/*Popolamento TipoMerce*/
INSERT INTO TipoMerce (Codice, Nome, Peso) VALUES
('T-AL-005', 'Pane', 5),
('M-IN-001', 'Benzina', 0.1),
('A-TO-125', 'Pesticidi', 0.25),
('T-FR-015', 'Vetro', 15),
('M-AL-000', 'Latte', 0.1),
('A-IN-050', 'Diesel', 50),
('T-TO-010', 'Acido Solforico', 10),
('M-FR-000', 'Marmo', 0.1),
('A-ST-100', 'Legno Ebano', 0.15),
('T-ST-020', 'Granito', 20),
('M-TO-075', 'Cloro', 0.1),
('A-AL-200', 'Carne Bovina', 20),
('M-FR-007', 'Cristallo', 0.1),
('T-ST-002', 'Ferro', 20),
('A-IN-150', 'Acetone', 0.15),
('T-TO-015', 'Ammoniaca', 15),
('M-AL-001', 'Patate Sila', 0.1),
('A-FR-050', 'Vetro', 0.20),
('T-ST-018', 'Granito', 18),
('M-IN-005', 'Metano', 0.1),
('T-TO-200', 'Piombo', 20),
('T-AL-010', 'Carne', 10),
('M-FR-001', 'Porcellana', 0.1),
('T-ST-080', 'Legno', 20),
('T-FR-012', 'Marmo', 10),
('M-TO-050', 'Ftalati', 0.1),
('A-AL-300', 'Pescato Mediterraneo', 0.25),
('M-TO-025', 'Acido Solforico', 0.25),
('M-ST-001', 'Ferro', 0.1),
('A-IN-025', 'Acetone', 0.25),
('T-FR-010', 'Vetro', 10),
('A-AL-000', 'Latte', 0.25),
('A-FR-030', 'Vetro Temperato', 0.20),
('A-ST-015', 'Granito', 0.25),
('T-IN-003', 'Metano', 15),
('A-TO-180', 'Cloruro di Vinile', 0.18),
('T-AL-012', 'Carne Suina', 10),
('M-FR-011', 'Gres', 0.1),
('T-ST-120', 'Legno Abete', 12),
('T-FR-020', 'Policarbonato', 20),
('M-TO-026', 'Nitrato Potassio', 0.1),
('A-AL-250', 'Salmone Canadese', 0.20),
('T-TO-030', 'Policrilammide', 20),
('A-ST-001', 'Ferro', 0.25),
('A-IN-120', 'Propano', 0.20),
('M-FR-008', 'Vetro', 0.08),
('M-AL-003', 'Arance Sicilia', 0.1),
('A-FR-070', 'Quarzo', 0.20),
('M-ST-022', 'Granito', 0.1),
('A-IN-004', 'Metano', 0.25),
('A-TO-150', 'Pesticidi', 0.25);


/*Popolamento CentroDistribuzione*/
INSERT INTO CentroDistribuzione (Citta, Zona, Distanza) VALUES
('Bari', 'Centrale', 240),
('Bari', 'Periferico', 240),
('Bologna', 'Centrale', 510),
('Cagliari', 'Centrale', 850),
('Cagliari', 'Poetto', 860),
('Catania', 'Centrale', 680),
('Catania', 'Borgo-Sanzio', 690),
('Firenze', 'Centrale', 520),
('Firenze', 'Periferico', 520),
('Genova', 'Centrale', 840),
('Genova', 'Albaro', 850),
('Milano', 'Centrale', 700),
('Milano', 'Brera', 710),
('Napoli', 'Centrale', 1),
('Napoli', 'Chiaia', 5),
('Palermo', 'Centrale', 610),
('Palermo', 'Periferico', 610),
('Roma', 'Centrale', 230),
('Roma', 'Trastevere', 240),
('Torino', 'Centrale', 810),
('Torino', 'Vanchiglia', 820),
('Venezia', 'Centrale', 570),
('Venezia', 'Dorsoduro', 580),
('Verona', 'Centrale', 500),
('Verona', 'Periferico', 500),
('Lecce', 'Periferico', 440),
('Bologna', 'Periferico', 510),
('Udine', 'Centrale', 840),
('Milano', 'Navigli', 700),
('Teramo', 'Centrale', 610),
('Jesi', 'Centrale', 810);


/* Popolamento Mezzi  Consumo Km/L (equivalente gasolio) */
INSERT INTO Mezzo (Targa, Tipologia, CaricoMax, Marca, Consumo) VALUES 
('AT000001', 'Treno', 20, 'Hitachi', 4.1),
('AT000002', 'Treno', 20, 'Hitachi', 4.1),
('AT000003', 'Treno', 20, 'Hitachi', 4.1),
('AT000004', 'Treno', 20, 'Hitachi', 4.1),
('AT000005', 'Treno', 20, 'Hitachi', 4.1),
('AT000006', 'Treno', 20, 'Hitachi', 4.1),
('AT000007', 'Treno', 20, 'Hitachi', 4.1),
('AA002AA', 'Motrice', 0.1, 'Krone', 3.1),
('AB003AB', 'Autotreno', 0.25, 'Iveco', 2.6),
('AC004AC', 'Motrice', 0.1, 'Mercedes', 3.0),
('AD005AD', 'Autotreno', 0.25, 'Iveco', 2.6),
('AE006AE', 'Motrice', 0.1, 'Mercedes', 3.0),
('AF007AF', 'Autotreno', 0.25, 'Iveco', 2.6),
('AG008AG', 'Motrice', 0.1, 'Mercedes', 3.0),
('AH009AH', 'Autotreno', 0.25, 'Iveco', 2.6),
('AI010AI', 'Motrice', 0.1, 'Mercedes', 3.0),
('AJ011AJ', 'Autotreno', 0.25, 'Iveco', 2.6),
('AK012AK', 'Motrice', 0.1, 'Mercedes', 3.0),
('AL013AL', 'Autotreno', 0.25, 'Iveco', 2.6),
('AM014AM', 'Motrice', 0.1, 'Mercedes', 3.0),
('AN015AN', 'Autotreno', 0.25, 'Fiat', 2.4),
('AO016AO', 'Motrice', 0.1, 'Fiat', 3.2),
('AP017AP', 'Autotreno', 0.25, 'Fiat', 2.4),
('AQ018AQ', 'Motrice', 0.1, 'Fiat', 3.2),
('AR019AR', 'Autotreno', 0.25, 'Fiat', 2.4),
('AS020AS', 'Motrice', 0.1, 'Fiat', 3.2),
('AT021AT', 'Autotreno', 0.25, 'Fiat', 2.4),
('AU022AU', 'Motrice', 0.1, 'Fiat', 3.2),
('AV023AV', 'Autotreno', 0.25, 'Fiat', 2.4),
('AX025AX', 'Motrice', 0.1, 'Fiat', 3.2),
('AZ027AZ', 'Autotreno', 0.25, 'Fiat', 2.4),
('BA028BA', 'Motrice', 0.1, 'Fiat', 3.2),
('BC030BC', 'Autotreno', 0.25, 'Fiat', 2.4),
('BD091BD', 'Motrice', 0.1, 'Fiat', 3.2),
('BF033BF', 'Autotreno', 0.25, 'Volvo', 2.3),
('BG034BG', 'Motrice', 0.1, 'Fiat', 3.2),
('BH035BH', 'Autotreno', 0.25, 'Volvo', 2.3),
('BK038BK', 'Motrice', 0.1, 'Volvo', 2.7),
('BL039BL', 'Autotreno', 0.25, 'Volvo', 2.3),
('BM040BM', 'Motrice', 0.1, 'Volvo', 2.7),
('BN041BN', 'Autotreno', 0.25, 'Volvo', 2.3),
('BP043BP', 'Motrice', 0.1, 'Volvo', 2.7),
('BQ044BQ', 'Autotreno', 0.25, 'Volvo', 2.3),
('BR055BR', 'Motrice', 0.1, 'Volvo', 2.7),
('BS046BS', 'Autotreno', 0.25, 'Volvo', 2.3),
('BT047BT', 'Motrice', 0.1, 'Volvo', 2.7),
('BV049BV', 'Autotreno', 0.25, 'Volvo', 2.3),
('BW050BW', 'Motrice', 0.1, 'Volvo', 2.7),
('BX057BX', 'Autotreno', 0.25, 'Volvo', 2.3),
('BY089BY', 'Motrice', 0.1, 'Volvo', 2.7),
('BZ053BZ', 'Autotreno', 0.25, 'Volvo', 2.3),
('CA054CA', 'Motrice', 0.1, 'Volvo', 2.7),
('CB055CB', 'Autotreno', 0.25, 'Renault', 2.9),
('CC056CC', 'Motrice', 0.1, 'Renault', 3.5),
('CD057CD', 'Autotreno', 0.25, 'Renault', 2.9),
('CE058CE', 'Motrice', 0.1, 'Renault', 3.5),
('CF059CF', 'Autotreno', 0.25, 'Renault', 2.9),
('CG060CG', 'Motrice', 0.1, 'Renault', 3.5),
('CH061CH', 'Autotreno', 0.25, 'Renault', 2.9),
('CI062CI', 'Motrice', 0.1, 'Renault', 3.5),
('CJ063CJ', 'Autotreno', 0.25, 'Renault', 2.9),
('CK064CK', 'Motrice', 0.1, 'Renault', 3.5),
('CL065CL', 'Autotreno', 0.25, 'Renault', 2.9),
('CM066CM', 'Motrice', 0.1, 'Renault', 3.5),
('CN067CN', 'Autotreno', 0.25, 'Renault', 2.9),
('CO068CO', 'Motrice', 0.1, 'Renault', 3.5),
('CP069CP', 'Autotreno', 0.25, 'Renault', 2.9),
('CQ070CQ', 'Motrice', 0.1, 'Schmitz', 2.8),
('CR071CR', 'Autotreno', 0.25, 'Renault', 2.9),
('CS072CS', 'Motrice', 0.1, 'Schmitz', 2.8),
('CT073CT', 'Autotreno', 0.25, 'Renault', 2.9),
('CU074CU', 'Motrice', 0.1, 'Schmitz', 2.8),
('CV075CV', 'Autotreno', 0.25, 'Renault', 2.9),
('CY078CY', 'Motrice', 0.1, 'Schmitz', 2.8),
('CZ079CZ', 'Autotreno', 0.25, 'Iveco', 2.6),
('DA080DA', 'Motrice', 0.1, 'Schmitz', 2.8),
('DB081DB', 'Autotreno', 0.25, 'Iveco', 2.6),
('DC082DC', 'Motrice', 0.1, 'Schmitz', 2.8),
('DF085DF', 'Autotreno', 0.25, 'Iveco', 2.6),
('DG086DG', 'Motrice', 0.1, 'Iveco', 3.0),
('DH087DH', 'Autotreno', 0.25, 'Iveco', 2.6),
('DI088DI', 'Motrice', 0.1, 'Iveco', 3.0),
('DJ089DJ', 'Autotreno', 0.25, 'Iveco', 2.6),
('DK090DK', 'Motrice', 0.1, 'Iveco', 3.0),
('DL091DL', 'Autotreno', 0.25, 'Iveco', 2.6),
('DM092DM', 'Motrice', 0.1, 'Iveco', 3.0),
('DN093DN', 'Autotreno', 0.25, 'Iveco', 2.6),
('DO094DO', 'Motrice', 0.1, 'Iveco', 3.0),
('DP095DP', 'Autotreno', 0.25, 'Iveco', 2.6),
('DQ096DQ', 'Motrice', 0.1, 'Mercedes', 3.4),
('DR097DR', 'Autotreno', 0.25, 'Iveco', 2.6),
('DS098DS', 'Motrice', 0.1, 'Mercedes', 3.4),
('DT099DT', 'Autotreno', 0.25, 'Iveco', 2.6),
('DU100DU', 'Motrice', 0.1, 'Mercedes', 3.4),
('DU101DU', 'Autotreno', 0.25, 'Iveco', 2.6),
('AT000008', 'Treno', 20, 'G.A.I.', 3.9),
('AT000009', 'Treno', 20, 'G.A.I.', 3.9),
('AT000010', 'Treno', 20, 'G.A.I.', 3.9),
('AT000011', 'Treno', 20, 'G.A.I.', 3.9),
('AT000012', 'Treno', 20, 'G.A.I.', 3.9),
('AT000013', 'Treno', 20, 'G.A.I.', 3.9),
('AT000014', 'Treno', 20, 'FerroSud', 4.2),
('AT000015', 'Treno', 20, 'FerroSud', 4.2),
('AT000016', 'Treno', 20, 'FerroSud', 4.2),
('AT000017', 'Treno', 20, 'FerroSud', 4.2),
('AT000018', 'Treno', 20, 'FerroSud', 4.2),
('AT000019', 'Treno', 20, 'FerroSud', 4.2),
('AT000020', 'Treno', 20, 'Krauss-Maffei', 4.3),
('AT000021', 'Treno', 20, 'Krauss-Maffei', 4.3),
('AT000022', 'Treno', 20, 'Krauss-Maffei', 4.3);


/*Popolamento Dipendenti */
INSERT INTO Dipendente (Matricola, Cognome, Nome, DataNascita, AttualeDipendente) VALUES
('TR0002','Udinesi','Concordia','2000-4-3',true),
('TR0003','Toscano','Alvisa','1963-3-14',true),
('TR0004','Milano','Addolorata','1957-11-20',true),
('TR0005','Cocci','Raul','1996-5-4',true),
('TR0006','Genovese','Gustava','1966-2-15',true),
('TR0007','Lori','Rosina','1982-1-11',true),
('TR0008','Calabrese','Ella','1960-4-27',true),
('TR0009','Ferri','Licia','1991-2-23',true),
('TR0010','Lettiere','Fantino','1964-11-30',true),
('TR0011','Fanucci','Aurelia','1996-4-25',true),
('TR0012','Mazzi','Fiore','1975-3-27',true),
('TR0013','Russo','Filomena','1959-4-28',true),
('TR0014','Endrizzi','Artemia','1999-2-10',true),
('TR0015','Longo','Giona','1963-6-3',true),
('TR0016','Manfrin','Astolfo','1976-11-12',true),
('TR0017','Lucciano','Abela','1994-12-28',true),
('TR0018','Bergamaschi','Nino','1999-2-26',true),
('TR0019','Zetticci','Antonino','1992-8-13',true),
('TR0020','Genovesi','Vito','1971-6-23',true),
('TR0021','Boni','Mario','1989-3-27',true),
('TR0022','Conti','Alfonsa','1984-6-6',true),
('TR0023','Marino','Penelope','1958-3-13',true),
('TR0024','Piazza','Alessia','2001-10-3',true),
('TR0025','Fiorentini','Placido','1994-10-18',true),
('TR0026','Panicucci','Taziano','1971-10-16',true),
('TR0027','Ferri','Ortensio','1989-4-26',true),
('TR0028','Padovano','Nazario','1957-5-21',true),
('TR0029','Sabbatini','Dina','1994-11-22',true),
('TR0030','Monaldo','Viviano','1975-4-22',true),
('TR0031','Calabrese','Wilma','1983-2-12',true),
('TR0032','Schiavone','Eric','2003-2-13',true),
('TR0033','Marino','Gabriele','1967-10-25',true),
('TR0034','Boni','Furio','1991-11-21',true),
('TR0035','Marchesi','Concetta','1960-10-2',true),
('TR0036','Siciliano','Cristian','1992-12-6',true),
('TR0037','Schiavone','Ferdinando','2003-10-18',true),
('TR0038','Castiglione','Giuseppa','1977-5-4',true),
('TR0039','Palerma','Fioretta','1978-9-23',true),
('TR0040','Castiglione','Aurora','1998-12-14',true),
('TR0041','Li Fonti','Fedele','1978-9-9',true),
('TR0042','Padovano','Benedetta','1971-11-11',true),
('TR0043','Pisani','Letizia','1977-3-20',true),
('TR0044','Gallo','Rufino','1982-3-19',true),
('TR0045','Russo','Alceo','1959-1-27',true),
('TR0046','DeRose','Rocco','1996-1-1',true),
('TR0047','Zito','Silvestro','1999-2-8',true),
('TR0048','Schiavone','Beato','1987-2-23',true),
('TR0049','Padovano','Tarquinia','1967-6-27',true),
('TR0050','Longo','Guido','1976-5-6',true),
('TR0051','Moretti','Dora','1972-4-12',true),
('TR0052','Costa','Giona','1996-5-20',true),
('TR0053','Mazzanti','Oliviero','1963-1-18',true),
('TR0054','Calabresi','Agnese','1968-12-9',true),
('TR0055','Lombardo','Gerardina','1988-6-2',true),
('TR0056','Siciliani','Calliope ','1989-7-5',true),
('TR0057','Marino','Prudenzio','1993-4-5',true),
('TR0058','Cattaneo','Ambrosino','1960-2-7',true),
('TR0059','Pisani','Furio','1989-1-4',true),
('TR0060','Piazza','Orazio','1993-1-3',true),
('TR0061','Milanesi','Crispina','1975-11-2',true),
('TR0062','Mazzanti','Manuela','1966-4-30',true),
('TR0063','Lucchesi','Delinda','2002-7-14',true),
('TR0064','Lucciano','Ida ','2000-7-7',true),
('TR0065','Cremonesi','Arduino','1967-3-13',true),
('TR0066','Udinese','Rolando','1957-3-26',true),
('TR0067','Moretti','Simone','1989-10-18',true),
('TR0068','Fanucci','Castore','1966-1-2',true),
('TR0069','Fiorentini','Lodovica','2002-7-8',true),
('TR0070','Mancini','Costanzo','1965-6-23',true),
('TR0071','Rizzo','Alida','1998-6-28',true),
('TR0072','Onio','Edilio','1995-4-5',true),
('TR0073','Monaldo','Donata','1962-12-1',true),
('TR0074','Milani','Libera','1974-7-20',true),
('TR0075','Costa','Principio','2004-1-5',true),
('TR0076','Gallo','Filippo','2002-5-2',true),
('TR0077','Buccho','Abelardo','1976-7-23',true),
('TR0078','Pisano','Aurelia','1993-11-25',true),
('TR0079','Beneventi','Santina','1981-12-27',true),
('TR0080','Monaldo','Alfio','1981-5-13',true),
('TR0081','Romano','Salvatore','1978-1-12',true),
('TR0082','Greece','Libera Maria','1968-3-3',true),
('TR0083','Fiorentini','Quirino','1970-3-23',true),
('TR0084','Panicucci','Beniamino','1985-12-25',true),
('TR0085','Lucchese','Gaudenzia','1968-4-21',true),
('TR0086','DeRose','Natascia','1990-2-25',true),
('TR0087','Costa','Settimo','1983-3-28',true),
('TR0088','Fiorentini','Quarantino','1969-4-15',true),
('TR0089','Fiorentino','Rosetta','1986-8-4',true),
('TR0090','Mancini','Geraldo','1981-10-10',true),
('TR0091','Napolitano','Frediano','1971-3-20',true),
('TR0092','Palermo','Callisto','1991-4-25',true),
('TR0093','Iadanza','Amilcare','1972-12-24',true),
('TR0094','Ferrari','Concordio','1993-11-16',true),
('TR0095','Mazzanti','Stanislao','1971-3-2',true),
('TR0096','Mazzanti','Petronilla','1983-8-4',true),
('TR0097','Bergamaschi','Procopio','1997-7-1',true),
('TR0098','Russo','Matteo','1967-2-26',true),
('TR0099','Calabrese','Dalia','1992-3-26',true),
('TR0100','Onio','Marta','1995-8-28',true),
('TR0101','Longo','Cesio','1987-5-29',true),
('IM0102','Pugliesi','Fabrizia','1962-12-16',true),
('IM0103','Russo','Annibale','1989-2-23',true),
('IM0104','Sagese','Maurizia','1993-8-7',true),
('IM0105','Marchesi','Pasqualina','1989-11-20',true),
('IM0106','Marchesi','Manuela','1957-1-19',true),
('IM0107','Zito','Nora','1962-8-23',true),
('IM0108','Fanucci','Marcello','1958-8-24',true),
('IM0109','Cattaneo','Ada','1957-11-7',true),
('IM0110','Conti','Petronio','1982-9-26',true),
('IM0111','Ricci','Innocenzo','1974-5-25',true),
('IM0112','Lucciano','Fedro','1975-9-17',true),
('IM0113','Bruno','Violante','1974-1-8',true),
('IM0114','Toscano','Fulgenzia','1961-2-26',true),
('IM0115','Manfrin','Azeglio','1969-9-18',true),
('IM0116','Napolitano','Cassandra','1980-2-10',true),
('IM0117','Esposito','Tranquillo','1959-7-8',true),
('IM0118','Mazzanti','Luigi','1982-8-18',true),
('IM0119','Angelo','Pupetta','1992-3-13',true),
('IM0120','Giordano','Rolando','1991-11-6',true),
('IM0121','Piccio','Brunilde','1961-4-19',true),
('IM0122','Toscano','Oscar','1982-9-6',true),
('IM0123','Russo','Rosalia','1982-6-27',true),
('IM0124','Endrizzi','Liliana','1973-8-7',true),
('IM0125','Manna','Barbara','1978-3-5',true),
('IM0126','Lombardo','Dorotea','1976-7-30',true),
('IM0127','Arcuri','Maria Pia','1980-2-26',true),
('IM0128','Dellucci','Amalio','1970-2-26',true),
('IM0129','Fiorentino','Liviana','1961-12-7',true),
('IM0130','Panicucci','Rinaldo','1961-12-26',true),
('IM0131','Udinese','Mareta','1959-3-14',true),
('IM0132','Sabbatini','Flora','1985-2-1',true),
('IM0133','Pagnotto','Cassandra','1976-1-10',true),
('IM0134','Trevisani','Calliope ','1994-5-17',true),
('IM0135','Costa','Eva','1998-11-6',true),
('IM0136','Mazzi','Claudio','1987-3-21',true),
('IM0137','Siciliano','Luigina','1985-2-28',true),
('IM0138','Barese','Eberardo','1988-11-10',true),
('IM0139','De Luca','Valente','1971-3-8',true),
('IM0140','Romano','Anna Maria','1958-11-21',true),
('IM0141','Panicucci','Manuela','1983-7-30',true),
('DG0142','Padovano','Ermenegildo','1971-4-5',true),
('DG0143','Schiavone','Elda','1974-6-6',true),
('DG0144','Fiorentino','Francesco','1977-1-13',true),
('DG0145','Udinesi','Isabella','1964-6-26',true),
('DG0146','Beneventi','Marisa','1997-4-13',true),
('DG0147','Sal','Mario','1954-11-28',true),
('DG0148','Pirozzi','Batilda','1982-1-7',true),
('DG0149','Calabresi','Alberto','1992-12-31',true),
('DG0150','Barese','Angelo','1965-7-4',true),
('DG0151','Lombardo','Violante','1996-7-11',true),
('TR0152','Marcelo','Vincenzo','1977-12-14',false),
('TR0153','Barese','Fabrizio','1981-10-27',false),
('TR0154','Toscani','Ofelia','1967-12-8',false),
('TR0155','Manfrin','Domenico','1982-9-6',false),
('TR0156','Costa','Amalia','1991-2-14',false),
('TR0157','Piccio','Aladino','1973-8-24',false),
('TR0158','Panicucci','Luciano','1959-8-7',false),
('TR0159','Iadanza','Letizia','1993-11-29',false),
('TR0160','Bruno','Marcella','1982-8-16',false),
('TR0161','DeRose','Neera','1994-7-5',false),
('TR0162','Lucchesi','Raimondo','1983-5-2',false),
('TR0163','Lucciano','Efisio','1992-7-20',false),
('TR0164','Udinese','Lia','1983-4-22',false),
('TR0165','Giordano','Nunzio','1966-6-2',false),
('TR0166','Russo','Gildo','1986-3-13',false),
('TR0167','Pirozzi','Tarquinio','1968-3-4',false),
('TR0168','Calabresi','Innocenzo','1962-4-3',false),
('TR0169','Lo Duca','Adalberto','1993-6-29',false),
('TR0170','Angelo','Gerardo','1986-2-14',false),
('TR0171','Lucchesi','Wanda ','1985-6-2',false),
('TR0172','Gallo','Felicita','1964-11-12',false),
('TR0173','Palerma','Vala ','1985-7-22',false),
('TR0174','Fallaci','Aristide','1965-6-16',false),
('TR0175','Lucchesi','Selene','1985-1-7',false),
('TR0176','Lori','Ivano','1961-3-23',false),
('TR0177','Gallo','Flora','1957-3-2',false),
('TR0178','Ricci','Romola','1975-9-21',false),
('TR0179','Onio','Lorena','1995-6-22',false),
('TR0180','Esposito','Cirilla','1973-7-13',false),
('TR0181','Siciliano','Amaranto','2003-3-10',false),
('TR0182','Conti','Iole','1963-4-24',false),
('TR0183','Bianchi','Leda','1984-6-20',false),
('TR0184','Pisani','Fedele','1965-10-8',false),
('IM0185','Fiorentino','Romano','1986-8-17',false),
('IM0186','Romano','Fabiano','1957-8-26',false),
('IM0187','Marchesi','Macario','1972-3-15',false),
('IM0188','Lettiere','Alvisio','1999-12-27',false),
('IM0189','Genovesi','Marianna','1992-3-1',false),
('IM0190','Bellucci','Fulvia','1987-1-16',false),
('IM0191','Ferri','Norberto','2002-12-26',false),
('IM0192','Palerma','Manuela','1978-1-3',false),
('IM0193','Sal','Giuseppina','2002-10-22',false),
('IM0194','Lorenzo','Leonida','1970-12-5',false),
('IM0195','Schiavone','Battista','1976-4-30',false),
('IM0196','Padovesi','Uriele','1992-11-10',false),
('IM0197','Palerma','Alida','1957-10-25',false),
('DG0198','Marino','Ambretta','1993-10-12',false),
('DG0199','Beneventi','Carla','1973-5-1',false),
('DG0200','Pirozzi','Benigna','1986-8-21',false),
('DG0201','Lucciano','Sandra','1968-12-15',false);


/*Popolamento Categoria*/
INSERT INTO Categoria (NomeCategoria) VALUES
('Alimentare'), 
('Stabile'), 
('Fragile'), 
('Tossico'),
('Infiammabile');


/*Popolamento Sede*/
INSERT INTO Sede (CodiceSede, Via, Citta, CAP, Email, Telefono) VALUES
('SEDE001', 'Via Giuseppe Garibaldi 3', 'Napoli', '80100', 'napoli-info@ati.com', '+39 081 3456789'),
('SEDE002', 'Via Leonardo da Vinci 2', 'Milano', '20100', 'milano-info@ati.com', '+39 02 2345678'),
('SEDE003', 'Via Alessandro Volta 1', 'Roma', '00100', 'roma-info@ati.com', '+39 06 1234567'),
('SEDE004', 'Via Emanuele Filiberto 4', 'Torino', '10100', 'torino-info@ati.com', '+39 011 4567890'),
('SEDE005', 'Via Ruggero Settimo 5', 'Palermo', '90100', 'palermo-info@ati.com', '+39 091 5678901'),
('SEDE006', 'Via Cristoforo Colombo 6', 'Genova', '16100', 'genova-info@ati.com', '+39 010 6789012'),
('SEDE007', 'Via Lorenzo il Magnifico 7', 'Firenze', '50100', 'firenze-info@ati.com', '+39 055 7890123'),
('SEDE008', 'Via San Petronio 8', 'Bologna', '40100', 'bologna-info@ati.com', '+39 051 8901234'),
('SEDE009', 'Via Etnea 9', 'Catania', '95100', 'catania-info@ati.com', '+39 095 0123456'),
('SEDE010', 'Via della Libertà 10', 'Venezia', '30100', 'venezia-info@ati.com', '+39 041 1234567');


/*Popolamento Trasportatori*/
INSERT INTO Trasportatore (Matricola, TariffaGiorno) VALUES
('TR0002', 68.5),
('TR0003', 70.0),
('TR0004', 72.5),
('TR0005', 68.5),
('TR0006', 70.0),
('TR0007', 72.5),
('TR0008', 68.5),
('TR0009', 70.0),
('TR0010', 72.5),
('TR0011', 68.5),
('TR0012', 70.0),
('TR0013', 72.5),
('TR0014', 68.5),
('TR0015', 70.0),
('TR0016', 72.5),
('TR0017', 68.5),
('TR0018', 70.0),
('TR0019', 72.5),
('TR0020', 68.5),
('TR0021', 70.0),
('TR0022', 72.5),
('TR0023', 68.5),
('TR0024', 70.0),
('TR0025', 72.5),
('TR0026', 68.5),
('TR0027', 70.0),
('TR0028', 72.5),
('TR0029', 68.5),
('TR0030', 70.0),
('TR0031', 72.5),
('TR0032', 68.5),
('TR0033', 70.0),
('TR0034', 72.5),
('TR0035', 68.5),
('TR0036', 70.0),
('TR0037', 72.5),
('TR0038', 68.5),
('TR0039', 70.0),
('TR0040', 72.5),
('TR0041', 68.5),
('TR0042', 70.0),
('TR0043', 72.5),
('TR0044', 68.5),
('TR0045', 70.0),
('TR0046', 72.5),
('TR0047', 68.5),
('TR0048', 70.0),
('TR0049', 72.5),
('TR0050', 68.5),
('TR0051', 70.0),
('TR0052', 72.5),
('TR0053', 68.5),
('TR0054', 70.0),
('TR0055', 72.5),
('TR0056', 68.5),
('TR0057', 70.0),
('TR0058', 72.5),
('TR0059', 68.5),
('TR0060', 70.0),
('TR0061', 72.5),
('TR0062', 68.5),
('TR0063', 70.0),
('TR0064', 72.5),
('TR0065', 68.5),
('TR0066', 70.0),
('TR0067', 72.5),
('TR0068', 68.5),
('TR0069', 70.0),
('TR0070', 72.5),
('TR0071', 68.5),
('TR0072', 70.0),
('TR0073', 72.5),
('TR0074', 68.5),
('TR0075', 70.0),
('TR0076', 72.5),
('TR0077', 68.5),
('TR0078', 70.0),
('TR0079', 72.5),
('TR0080', 68.5),
('TR0081', 70.0),
('TR0082', 72.5),
('TR0083', 68.5),
('TR0084', 70.0),
('TR0085', 72.5),
('TR0086', 68.5),
('TR0087', 70.0),
('TR0088', 72.5),
('TR0089', 68.5),
('TR0090', 70.0),
('TR0091', 72.5),
('TR0092', 68.5),
('TR0093', 70.0),
('TR0094', 72.5),
('TR0095', 68.5),
('TR0096', 70.0),
('TR0097', 72.5),
('TR0098', 68.5),
('TR0099', 70.0),
('TR0100', 72.5),
('TR0101', 68.5);


/*Popolamento Impiegati*/
INSERT INTO Impiegato (Matricola, TariffaGiorno, CodiceSede) VALUES
('IM0102', 62.5, 'SEDE001'),
('IM0103', 60.0, 'SEDE002'),
('IM0104', 65.0, 'SEDE003'),
('IM0105', 62.5, 'SEDE004'),
('IM0106', 60.0, 'SEDE005'),
('IM0107', 65.0, 'SEDE006'),
('IM0108', 62.5, 'SEDE007'),
('IM0109', 60.0, 'SEDE008'),
('IM0110', 65.0, 'SEDE009'),
('IM0111', 62.5, 'SEDE010'),
('IM0112', 60.0, 'SEDE001'),
('IM0113', 65.0, 'SEDE002'),
('IM0114', 62.5, 'SEDE003'),
('IM0115', 60.0, 'SEDE004'),
('IM0116', 65.0, 'SEDE005'),
('IM0117', 62.5, 'SEDE006'),
('IM0118', 60.0, 'SEDE007'),
('IM0119', 65.0, 'SEDE008'),
('IM0120', 62.5, 'SEDE009'),
('IM0121', 60.0, 'SEDE010'),
('IM0122', 65.0, 'SEDE001'),
('IM0123', 62.5, 'SEDE002'),
('IM0124', 60.0, 'SEDE003'),
('IM0125', 65.0, 'SEDE004'),
('IM0126', 62.5, 'SEDE005'),
('IM0127', 60.0, 'SEDE006'),
('IM0128', 65.0, 'SEDE007'),
('IM0129', 62.5, 'SEDE008'),
('IM0130', 60.0, 'SEDE009'),
('IM0131', 65.0, 'SEDE010'),
('IM0132', 62.5, 'SEDE001'),
('IM0133', 60.0, 'SEDE002'),
('IM0134', 65.0, 'SEDE003'),
('IM0135', 62.5, 'SEDE004'),
('IM0136', 60.0, 'SEDE005'),
('IM0137', 65.0, 'SEDE006'),
('IM0138', 62.5, 'SEDE007'),
('IM0139', 60.0, 'SEDE008'),
('IM0140', 65.0, 'SEDE009'),
('IM0141', 62.5, 'SEDE010');


/*Popolamento Dirigenti*/
INSERT INTO Dirigente (Matricola, StipendioBase, DataPromozione, CodiceSede) VALUES
('DG0142', 1500, '2022-05-10', 'SEDE001'),
('DG0143', 1500, '2021-09-15', 'SEDE002'),
('DG0144', 1500, '2020-07-20', 'SEDE003'),
('DG0145', 1500, '2019-11-25', 'SEDE004'),
('DG0146', 1500, '2018-12-30', 'SEDE005'),	
('DG0147', 1500, '2022-02-28', 'SEDE006'),
('DG0148', 1500, '2020-06-05', 'SEDE007'),
('DG0149', 1500, '2019-08-12', 'SEDE008'),
('DG0150', 1500, '2021-04-18', 'SEDE009'),
('DG0151', 1500, '2020-10-22', 'SEDE010');


/*Popolamento TrasportabilitaMezzo*/ /*DA MIGLIORARE!*/
INSERT INTO TrasportabilitaMezzo (Targa, NomeCategoria) VALUES
('AT000001','Stabile'),
('AT000001','Tossico'),
('AT000001','Infiammabile'),
('AT000001','Alimentare'),
('AT000002','Alimentare'),
('AT000002','Stabile'),
('AT000002','Infiammabile'),
('AT000002','Fragile'),
('AT000003','Infiammabile'),
('AT000003','Stabile'),
('AT000003','Fragile'),
('AT000003','Alimentare'),
('AT000004','Stabile'),
('AT000004','Infiammabile'),
('AT000004','Fragile'),
('AT000004','Alimentare'),
('AT000004','Tossico'),
('AT000005','Fragile'),
('AT000005','Stabile'),
('AT000005','Alimentare'),
('AT000005','Tossico'),
('AT000005','Infiammabile'),
('AT000006','Fragile'),
('AT000006','Tossico'),
('AT000006','Alimentare'),
('AT000006','Stabile'),
('AT000006','Infiammabile'),
('AT000007','Stabile'),
('AT000007','Alimentare'),
('AT000007','Fragile'),
('AT000007','Tossico'),
('AT000007','Infiammabile'),
('AA002AA','Stabile'),
('AA002AA','Tossico'),
('AB003AB','Stabile'),
('AB003AB','Infiammabile'),
('AB003AB','Fragile'),
('AB003AB','Tossico'),
('AB003AB','Alimentare'),
('AC004AC','Infiammabile'),
('AC004AC','Alimentare'),
('AD005AD','Alimentare'),
('AD005AD','Infiammabile'),
('AD005AD','Tossico'),
('AE006AE','Fragile'),
('AE006AE','Infiammabile'),
('AE006AE','Alimentare'),
('AE006AE','Stabile'),
('AF007AF','Stabile'),
('AF007AF','Tossico'),
('AG008AG','Fragile'),
('AG008AG','Tossico'),
('AG008AG','Stabile'),
('AH009AH','Fragile'),
('AH009AH','Stabile'),
('AH009AH','Tossico'),
('AH009AH','Infiammabile'),
('AI010AI','Alimentare'),
('AI010AI','Infiammabile'),
('AI010AI','Tossico'),
('AI010AI','Fragile'),
('AI010AI','Stabile'),
('AJ011AJ','Fragile'),
('AJ011AJ','Alimentare'),
('AJ011AJ','Stabile'),
('AK012AK','Stabile'),
('AK012AK','Alimentare'),
('AK012AK','Fragile'),
('AL013AL','Stabile'),
('AL013AL','Fragile'),
('AL013AL','Infiammabile'),
('AL013AL','Alimentare'),
('AL013AL','Tossico'),
('AM014AM','Tossico'),
('AM014AM','Stabile'),
('AM014AM','Infiammabile'),
('AM014AM','Alimentare'),
('AN015AN','Alimentare'),
('AN015AN','Fragile'),
('AN015AN','Infiammabile'),
('AN015AN','Stabile'),
('AN015AN','Tossico'),
('AO016AO','Stabile'),
('AO016AO','Alimentare'),
('AO016AO','Tossico'),
('AO016AO','Infiammabile'),
('AP017AP','Infiammabile'),
('AP017AP','Fragile'),
('AP017AP','Stabile'),
('AP017AP','Alimentare'),
('AP017AP','Tossico'),
('AQ018AQ','Fragile'),
('AQ018AQ','Alimentare'),
('AQ018AQ','Infiammabile'),
('AQ018AQ','Stabile'),
('AQ018AQ','Tossico'),
('AR019AR','Fragile'),
('AR019AR','Infiammabile'),
('AR019AR','Tossico'),
('AR019AR','Stabile'),
('AR019AR','Alimentare'),
('AS020AS','Stabile'),
('AS020AS','Alimentare'),
('AS020AS','Infiammabile'),
('AT021AT','Tossico'),
('AT021AT','Alimentare'),
('AT021AT','Infiammabile'),
('AU022AU','Fragile'),
('AU022AU','Infiammabile'),
('AU022AU','Stabile'),
('AV023AV','Infiammabile'),
('AV023AV','Stabile'),
('AV023AV','Tossico'),
('AX025AX','Fragile'),
('AX025AX','Tossico'),
('AX025AX','Stabile'),
('AX025AX','Infiammabile'),
('AZ027AZ','Stabile'),
('AZ027AZ','Alimentare'),
('AZ027AZ','Fragile'),
('AZ027AZ','Tossico'),
('BA028BA','Infiammabile'),
('BA028BA','Tossico'),
('BC030BC','Infiammabile'),
('BC030BC','Tossico'),
('BC030BC','Alimentare'),
('BC030BC','Fragile'),
('BC030BC','Stabile'),
('BD091BD','Alimentare'),
('BD091BD','Infiammabile'),
('BD091BD','Stabile'),
('BD091BD','Fragile'),
('BD091BD','Tossico'),
('BF033BF','Tossico'),
('BF033BF','Infiammabile'),
('BF033BF','Stabile'),
('BG034BG','Stabile'),
('BG034BG','Fragile'),
('BG034BG','Tossico'),
('BG034BG','Infiammabile'),
('BG034BG','Alimentare'),
('BH035BH','Alimentare'),
('BH035BH','Tossico'),
('BH035BH','Fragile'),
('BK038BK','Stabile'),
('BK038BK','Alimentare'),
('BK038BK','Infiammabile'),
('BK038BK','Tossico'),
('BK038BK','Fragile'),
('BL039BL','Infiammabile'),
('BL039BL','Fragile'),
('BL039BL','Stabile'),
('BL039BL','Alimentare'),
('BL039BL','Tossico'),
('BM040BM','Infiammabile'),
('BM040BM','Stabile'),
('BN041BN','Tossico'),
('BN041BN','Stabile'),
('BN041BN','Infiammabile'),
('BP043BP','Fragile'),
('BP043BP','Tossico'),
('BP043BP','Alimentare'),
('BQ044BQ','Tossico'),
('BQ044BQ','Fragile'),
('BQ044BQ','Stabile'),
('BQ044BQ','Infiammabile'),
('BQ044BQ','Alimentare'),
('BR055BR','Fragile'),
('BR055BR','Tossico'),
('BR055BR','Infiammabile'),
('BS046BS','Stabile'),
('BS046BS','Alimentare'),
('BT047BT','Fragile'),
('BT047BT','Alimentare'),
('BT047BT','Tossico'),
('BT047BT','Stabile'),
('BV049BV','Stabile'),
('BV049BV','Infiammabile'),
('BV049BV','Alimentare'),
('BW050BW','Fragile'),
('BW050BW','Stabile'),
('BX057BX','Alimentare'),
('BX057BX','Stabile'),
('BX057BX','Fragile'),
('BX057BX','Infiammabile'),
('BY089BY','Infiammabile'),
('BY089BY','Tossico'),
('BY089BY','Fragile'),
('BY089BY','Alimentare'),
('BY089BY','Stabile'),
('BZ053BZ','Infiammabile'),
('BZ053BZ','Stabile'),
('CA054CA','Tossico'),
('CA054CA','Fragile'),
('CA054CA','Alimentare'),
('CA054CA','Infiammabile'),
('CB055CB','Alimentare'),
('CB055CB','Infiammabile'),
('CB055CB','Tossico'),
('CC056CC','Tossico'),
('CC056CC','Fragile'),
('CC056CC','Infiammabile'),
('CD057CD','Stabile'),
('CD057CD','Alimentare'),
('CD057CD','Tossico'),
('CD057CD','Fragile'),
('CE058CE','Stabile'),
('CE058CE','Alimentare'),
('CF059CF','Alimentare'),
('CF059CF','Fragile'),
('CF059CF','Infiammabile'),
('CF059CF','Stabile'),
('CG060CG','Alimentare'),
('CG060CG','Tossico'),
('CG060CG','Fragile'),
('CH061CH','Infiammabile'),
('CH061CH','Stabile'),
('CH061CH','Tossico'),
('CH061CH','Fragile'),
('CH061CH','Alimentare'),
('CI062CI','Alimentare'),
('CI062CI','Tossico'),
('CJ063CJ','Alimentare'),
('CJ063CJ','Stabile'),
('CJ063CJ','Fragile'),
('CJ063CJ','Infiammabile'),
('CJ063CJ','Tossico'),
('CK064CK','Infiammabile'),
('CK064CK','Tossico'),
('CK064CK','Alimentare'),
('CK064CK','Fragile'),
('CL065CL','Fragile'),
('CL065CL','Infiammabile'),
('CM066CM','Fragile'),
('CM066CM','Tossico'),
('CM066CM','Infiammabile'),
('CN067CN','Infiammabile'),
('CN067CN','Stabile'),
('CO068CO','Alimentare'),
('CO068CO','Infiammabile'),
('CO068CO','Tossico'),
('CO068CO','Stabile'),
('CO068CO','Fragile'),
('CP069CP','Fragile'),
('CP069CP','Stabile'),
('CQ070CQ','Tossico'),
('CQ070CQ','Infiammabile'),
('CR071CR','Stabile'),
('CR071CR','Fragile'),
('CR071CR','Infiammabile'),
('CR071CR','Alimentare'),
('CR071CR','Tossico'),
('CS072CS','Alimentare'),
('CS072CS','Infiammabile'),
('CS072CS','Stabile'),
('CT073CT','Infiammabile'),
('CT073CT','Fragile'),
('CT073CT','Stabile'),
('CT073CT','Alimentare'),
('CU074CU','Tossico'),
('CU074CU','Infiammabile'),
('CU074CU','Stabile'),
('CU074CU','Alimentare'),
('CU074CU','Fragile'),
('CV075CV','Fragile'),
('CV075CV','Stabile'),
('CV075CV','Infiammabile'),
('CV075CV','Tossico'),
('CY078CY','Alimentare'),
('CY078CY','Fragile'),
('CY078CY','Tossico'),
('CY078CY','Stabile'),
('CZ079CZ','Stabile'),
('CZ079CZ','Infiammabile'),
('CZ079CZ','Fragile'),
('CZ079CZ','Alimentare'),
('CZ079CZ','Tossico'),
('DA080DA','Alimentare'),
('DA080DA','Stabile'),
('DB081DB','Infiammabile'),
('DB081DB','Stabile'),
('DB081DB','Fragile'),
('DB081DB','Alimentare'),
('DB081DB','Tossico'),
('DC082DC','Infiammabile'),
('DC082DC','Tossico'),
('DC082DC','Fragile'),
('DF085DF','Alimentare'),
('DF085DF','Infiammabile'),
('DF085DF','Tossico'),
('DF085DF','Stabile'),
('DG086DG','Stabile'),
('DG086DG','Tossico'),
('DG086DG','Fragile'),
('DH087DH','Tossico'),
('DH087DH','Infiammabile'),
('DI088DI','Fragile'),
('DI088DI','Infiammabile'),
('DJ089DJ','Alimentare'),
('DJ089DJ','Tossico'),
('DK090DK','Infiammabile'),
('DK090DK','Tossico'),
('DL091DL','Infiammabile'),
('DL091DL','Tossico'),
('DM092DM','Fragile'),
('DM092DM','Stabile'),
('DM092DM','Alimentare'),
('DM092DM','Tossico'),
('DM092DM','Infiammabile'),
('DN093DN','Tossico'),
('DN093DN','Alimentare'),
('DN093DN','Fragile'),
('DN093DN','Stabile'),
('DN093DN','Infiammabile'),
('DO094DO','Fragile'),
('DO094DO','Stabile'),
('DP095DP','Fragile'),
('DP095DP','Infiammabile'),
('DQ096DQ','Alimentare'),
('DQ096DQ','Fragile'),
('DQ096DQ','Infiammabile'),
('DQ096DQ','Tossico'),
('DQ096DQ','Stabile'),
('DR097DR','Infiammabile'),
('DR097DR','Fragile'),
('DR097DR','Alimentare'),
('DS098DS','Tossico'),
('DS098DS','Fragile'),
('DS098DS','Stabile'),
('DS098DS','Infiammabile'),
('DS098DS','Alimentare'),
('DT099DT','Alimentare'),
('DT099DT','Fragile'),
('DT099DT','Infiammabile'),
('DT099DT','Stabile'),
('DT099DT','Tossico'),
('DU100DU','Stabile'),
('DU100DU','Fragile'),
('DU100DU','Alimentare'),
('DU100DU','Infiammabile'),
('DU101DU','Alimentare'),
('DU101DU','Fragile'),
('AT000008','Infiammabile'),
('AT000008','Fragile'),
('AT000008','Tossico'),
('AT000008','Stabile'),
('AT000008','Alimentare'),
('AT000009','Stabile'),
('AT000009','Fragile'),
('AT000009','Alimentare'),
('AT000009','Infiammabile'),
('AT000009','Tossico'),
('AT000010','Fragile'),
('AT000010','Tossico'),
('AT000010','Alimentare'),
('AT000010','Stabile'),
('AT000010','Infiammabile'),
('AT000011','Tossico'),
('AT000011','Fragile'),
('AT000011','Stabile'),
('AT000011','Alimentare'),
('AT000011','Infiammabile'),
('AT000012','Fragile'),
('AT000012','Tossico'),
('AT000012','Infiammabile'),
('AT000012','Alimentare'),
('AT000012','Stabile'),
('AT000013','Infiammabile'),
('AT000013','Alimentare'),
('AT000013','Stabile'),
('AT000013','Tossico'),
('AT000013','Fragile'),
('AT000014','Infiammabile'),
('AT000014','Stabile'),
('AT000014','Fragile'),
('AT000015','Tossico'),
('AT000015','Alimentare'),
('AT000015','Stabile'),
('AT000015','Fragile'),
('AT000015','Infiammabile'),
('AT000016','Fragile'),
('AT000016','Stabile'),
('AT000016','Tossico'),
('AT000016','Infiammabile'),
('AT000017','Alimentare'),
('AT000017','Infiammabile'),
('AT000017','Fragile'),
('AT000017','Stabile'),
('AT000017','Tossico'),
('AT000018','Stabile'),
('AT000018','Tossico'),
('AT000018','Fragile'),
('AT000018','Infiammabile'),
('AT000019','Stabile'),
('AT000019','Tossico'),
('AT000019','Fragile'),
('AT000019','Infiammabile'),
('AT000019','Alimentare'),
('AT000020','Tossico'),
('AT000020','Alimentare'),
('AT000020','Fragile'),
('AT000021','Infiammabile'),
('AT000021','Fragile'),
('AT000021','Stabile'),
('AT000021','Tossico'),
('AT000021','Alimentare'),
('AT000022','Infiammabile'),
('AT000022','Fragile'),
('AT000022','Stabile'),
('AT000022','Alimentare'),
('AT000022','Tossico');



/*Popolamento Consegne*/ /* Altro File SQL */

/*Popolamento RegistroPresenze*/ /* Altro File SQL */


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
('2024-01-05', 'T-TO-200','Teramo','Centrale','AT000004','TR0008');
