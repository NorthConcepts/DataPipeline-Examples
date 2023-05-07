CREATE VIEW users_with_lastname_as_doe AS
SELECT user_id, user_name, email, password, user_role_id
FROM user
WHERE last_name = 'Doe';
