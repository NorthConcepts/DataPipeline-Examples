CREATE VIEW users_with_lastname_as_doe AS
SELECT user_name, email, user_role_id
FROM user
WHERE last_name = 'Doe';
