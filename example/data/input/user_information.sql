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


INSERT INTO user_role
(user_role_id, user_role)
VALUES
(1, 'Admin'), (2, 'Viewer');

INSERT INTO user_information 
(user_id, user_name, first_name, last_name, email, password, user_role_id) VALUES
(1, 'john_doe', 'John', 'Doe', 'john.doe@northconcepts.com', '**********', 1),
(2, 'jane_doe', 'jane', 'Doe', 'jane.doe@northconcepts.com', '**********', 1),
(3, 'view_user', 'viewer', '', 'view_user@northconcepts.com', '**********', 2);
