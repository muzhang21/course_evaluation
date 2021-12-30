CREATE TABLE users(
	userid BIGINT PRIMARY KEY AUTO_INCREMENT,
	email VARCHAR(75) NOT NULL UNIQUE,
	encryptedpassword VARCHAR(128) NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE courses(
	code VARCHAR(9) PRIMARY KEY NOT NULL,
	title VARCHAR(255) NOT NULL,
	credits DECIMAL(3, 1) NOT NULL,
	complete boolean DEFAULT 0,
	term INT,
	finalgrade DECIMAL(5,2)
	
);

CREATE TABLE evaluations(
	id INT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	course VARCHAR(9) NOT NULL,
	grade  DECIMAL(5,2),
	max DECIMAL(5,2),
	weight DECIMAL(5,2),
	duedate DATE
);