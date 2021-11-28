CREATE TABLE user_role (
  user_role_id INT NOT NULL,
  user_role VARCHAR(32) NOT NULL,
  PRIMARY KEY (user_role_id)
 );
  
CREATE TABLE user_information (
  user_id INT NOT NULL,
  user_name VARCHAR(16) NOT NULL,
  first_name VARCHAR(255) NULL,
  last_name VARCHAR(255) NULL,
  email VARCHAR(255) NULL,
  password VARCHAR(255) NOT NULL,
  user_role_id INT NULL,
  PRIMARY KEY (user_id),
  FOREIGN KEY (user_role_id) REFERENCES user_role (user_role_id)
  );

CREATE TABLE account (
  account_id INT NOT NULL,
  account_name VARCHAR(45) NULL,
  account_description VARCHAR(255) NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (account_id),
  FOREIGN KEY (user_id) REFERENCES user_information (user_id)
  );

CREATE TABLE operators (
  operator_id INT NOT NULL,
  operator_symbol VARCHAR(45) NOT NULL,
  operator_description VARCHAR(255) NULL,
  type VARCHAR(45) NOT NULL,
  PRIMARY KEY (operator_id)
  );

INSERT INTO user_role
(user_role_id, user_role)
VALUES
(1, 'Admin'), (2, 'ViewRole');
