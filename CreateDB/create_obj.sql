CREATE SEQUENCE NuomosIDSeq
    START WITH 1001;

CREATE SEQUENCE LokacijosIDSeq
	START WITH 101;

CREATE TABLE doce9051.Klientas (
	AK 	CHAR(11) 		NOT NULL 	CONSTRAINT check_ak_format
						        CHECK (
								AK ~ '^[3456]\d+$' AND
							        LENGTH(AK) = 11),
	Vardas 	VARCHAR(32)		NOT NULL,
	Pavarde	VARCHAR(32)		NOT NULL,
	Email	VARCHAR(32)		NOT NULL 	CONSTRAINT check_valid_email 
							CHECK (Email ~* '^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$'),
	PRIMARY KEY (AK)
);

CREATE UNIQUE INDEX idx_Email ON doce9051.Klientas (Email);


CREATE TABLE doce9051.Lokacija (
	LokacijosID 	INT DEFAULT nextval('LokacijosIDSeq'),
	Miestas		VARCHAR(32)		NOT NULL,
	Gatve		VARCHAR(32)		NOT NULL,
	NUMERIS		VARCHAR(32),
	PRIMARY KEY (LokacijosID)
);

CREATE TABLE doce9051.Automobilis (
	Vin 		CHAR(17) 	NOT NULL 	CONSTRAINT check_valid_vin
							CHECK (Vin ~* '^[A-HJ-NPR-Z0-9]{17}$'),
	Marke 		VARCHAR(32)	NOT NULL,
	Modelis		VARCHAR(32)	NOT NULL,
	Metai		SMALLINT 	NOT NULL 	CONSTRAINT check_year
							CHECK(Metai > 2000 AND Metai <= EXTRACT(YEAR FROM CURRENT_DATE)),
	Numeriai	CHAR(6)				CONSTRAINT check_license_plate_format
							CHECK (
						            (Numeriai IS NULL) OR
						            (LENGTH(Numeriai) = 6 AND
						             UPPER(SUBSTRING(Numeriai FROM 1 FOR 3)) BETWEEN 'AAA' AND 'ZZZ' AND
						             SUBSTRING(Numeriai FROM 4 FOR 3) ~ '^\d{3}$')),
						        CONSTRAINT check_unique_license_plate UNIQUE(Numeriai),
	Kaina		NUMERIC(10, 2) 	NOT NULL 	CONSTRAINT check_kaina
							CHECK (Kaina >= 0 AND Kaina <= 1000),
	PRIMARY KEY (Vin)
);

CREATE TABLE doce9051.Nuoma (
	NuomosID	INT DEFAULT nextval('NuomosIDSeq'),
	AK		CHAR(11)	NOT NULL,
	Paimta		DATE 		NOT NULL 	DEFAULT CURRENT_DATE,
	Grazinta	DATE 		NOT NULL 	DEFAULT CURRENT_DATE + INTERVAL '1 day',
	LokacijosID	INT		NOT NULL,
	PRIMARY KEY	(NuomosID),
	CONSTRAINT IAK 	     FOREIGN KEY (AK) 		REFERENCES doce9051.Klientas(AK) 		ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT ILokacija FOREIGN KEY (LokacijosID)	REFERENCES doce9051.Lokacija(LokacijosID)	ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT check_grazinta_after_paimta 	CHECK (Grazinta >= Paimta)
);

CREATE INDEX idx_Paimta ON doce9051.Nuoma (Paimta);


CREATE TABLE doce9051.Isnomuotas (
	NuomosID 	INT 		NOT NULL,
	Vin 		CHAR(17)	NOT NULL,
	PRIMARY KEY (NuomosID, Vin),
	CONSTRAINT INuoma FOREIGN KEY (NuomosID) 	REFERENCES doce9051.Nuoma(NuomosID) 		ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT IVin FOREIGN KEY (Vin)		REFERENCES doce9051.Automobilis(Vin) 		ON UPDATE CASCADE ON DELETE RESTRICT
);

-- VIEW TABLES --------------------------------------------------------------------------------------------------------------
CREATE VIEW doce9051.KlientuNuomos AS
SELECT
    N.NuomosID,
    K.AK,
    K.Vardas,
    K.Pavarde,
    N.Paimta,
    N.Grazinta,
    L.Miestas,
    L.Gatve,
    L.NUMERIS
FROM doce9051.Nuoma N
JOIN doce9051.Klientas K ON N.AK = K.AK
JOIN doce9051.Lokacija L ON N.LokacijosID = L.LokacijosID;

CREATE MATERIALIZED VIEW doce9051.MaterializuotaIsnomuotiAutomobiliai AS
SELECT
    I.NuomosID,
    A.Vin,
    A.Marke,
    A.Modelis,
    A.Metai,
    A.Numeriai,
    A.Kaina,
    N.Paimta,
    N.Grazinta
FROM doce9051.Isnomuotas I
JOIN doce9051.Automobilis A ON I.Vin = A.Vin
JOIN doce9051.Nuoma N ON I.NuomosID = N.NuomosID;

CREATE VIEW doce9051.NuomuLokacijos AS
SELECT
    N.NuomosID,
    L.Miestas,
    L.Gatve,
    L.NUMERIS,
    N.Paimta,
    N.Grazinta
FROM doce9051.Nuoma N
JOIN doce9051.Lokacija L ON N.LokacijosID = L.LokacijosID;

-- TRIGGERS ----------------------------------------------------------------------
CREATE OR REPLACE FUNCTION check_car_limit()
RETURNS TRIGGER AS $$
DECLARE
    car_count INT;
BEGIN
    -- Count the number of cars for the given NuomosID
    SELECT COUNT(*) INTO car_count
    FROM doce9051.Isnomuotas
    WHERE NuomosID = NEW.NuomosID;

    -- Raise an exception if the car count exceeds 3
    IF car_count >= 3 THEN
        RAISE EXCEPTION 'Cannot rent more than 3 cars for one rent (NuomosID: %)', NEW.NuomosID;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_car_limit_trigger
BEFORE INSERT OR UPDATE ON doce9051.Isnomuotas
FOR EACH ROW
EXECUTE FUNCTION check_car_limit();


CREATE OR REPLACE FUNCTION check_rent_availability()
RETURNS TRIGGER AS $$
DECLARE
    paimta_date DATE;
    grazinta_date DATE;
BEGIN
    -- Retrieve Paimta and Grazinta dates from Nuoma table
    SELECT Paimta, Grazinta
    INTO paimta_date, grazinta_date
    FROM doce9051.Nuoma
    WHERE NuomosID = NEW.NuomosID;

    -- Check if the car is already rented on the specified dates
    IF EXISTS (
        SELECT 1
        FROM doce9051.Isnomuotas i
        JOIN doce9051.Nuoma n ON i.NuomosID = n.NuomosID
        WHERE i.Vin = NEW.Vin
          AND (
              (paimta_date BETWEEN n.Paimta AND n.Grazinta)
              OR (grazinta_date BETWEEN n.Paimta AND n.Grazinta)
              OR (n.Paimta BETWEEN paimta_date AND grazinta_date)
          )
    ) THEN
        RAISE EXCEPTION 'Car with VIN % is already rented on the specified dates', NEW.Vin;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER prevent_duplicate_rent
BEFORE INSERT ON doce9051.Isnomuotas
FOR EACH ROW
EXECUTE FUNCTION check_rent_availability();
