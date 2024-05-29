CREATE TABLE provider
(
    Id         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    FirstName  VARCHAR(20)  NOT NULL,
    LastName   VARCHAR(20)  NOT NULL,
    DoB        DATE         NOT NULL,
    Email      VARCHAR(200) NOT NULL,
    Phone      VARCHAR(20)  NOT NULL,
    Speciality VARCHAR(50)  NOT NULL
);


CREATE TABLE patient
(
    Id         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    FirstName  VARCHAR(20)  NOT NULL,
    LastName   VARCHAR(20)  NOT NULL,
    DoB        DATE         NOT NULL,
    Email      VARCHAR(200) NOT NULL,
    Phone      VARCHAR(20)  NOT NULL,
    ProviderId INT,
    CONSTRAINT fk_provider FOREIGN KEY (ProviderId) REFERENCES provider (Id)
);
