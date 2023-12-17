INSERT INTO doce9051.Klientas (AK, Vardas, Pavarde, Email)
VALUES
  ('50303065001', 'Jonas', 'Jonauskas', 'jonas.jonauskas@yahoo.com'),
  ('39601014231', 'Albertas', 'Einsteinas', 'albertas.albis@inbox.com'),
  ('50112063114', 'Dominykas', 'Baltakis', 'dominykas.baltakis@gmail.com');
  
INSERT INTO doce9051.Lokacija (Miestas, Gatve, Numeris)
VALUES
  ('Vilnius', 'Naugarduko', '24'),
  ('Kaunas', 'Laisvės', '22B'),
  ('Klaipėda', 'Kalvos', NULL);

INSERT INTO doce9051.Nuoma (AK, Paimta, Grazinta, LokacijosID)
VALUES
  ('50303065001', '2023-12-01', '2023-12-18', 101),
  ('39601014231', '2023-12-02', '2023-12-03', 102);
  
INSERT INTO doce9051.Automobilis (Vin, Marke, Modelis, Metai, Numeriai, Kaina)
VALUES
  ('JTMRFREV6D5026982', 'Toyota', 'RAV4', 2020, 'MBJ312', 70.00),
  ('WAUAF34D5XN004220', 'Audi', 'A8', 2019, 'MNO888', 65.00),
  ('SCBZR03D2PCX46067', 'Bentley', 'Turbo', 2021, 'ABC123', 75.00),
  ('2HGFG3B90CH518288', 'Honda', 'Civic', 2022, 'XYZ789', 80.00);
  
INSERT INTO doce9051.Isnomuotas (NuomosID, Vin)
VALUES
  (1001, 'JTMRFREV6D5026982'),
  (1001, 'WAUAF34D5XN004220'),
  (1001, 'SCBZR03D2PCX46067');
