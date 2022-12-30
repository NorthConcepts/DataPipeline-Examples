
DROP TABLE jewellery IF EXISTS;
CREATE TABLE jewellery (
	title VARCHAR(100),
	published BOOLEAN,
	variant_inventory_qty INTEGER,
	gift_card TINYINT,
	variant_price FLOAT,
	variant_compare_at_price DOUBLE,
	min_price DECIMAL(5,0),
	max_price DECIMAL(30,10),
	height DECIMAL(15,0),
	width DECIMAL(8,0),
	weight DECIMAL(25,0),
	taxable CHAR,
	created_on TIMESTAMP,
	launch_date DATE,
	launch_time TIME
);

INSERT INTO jewellery
(title, published, variant_inventory_qty, gift_card,
variant_price, variant_compare_at_price, min_price, max_price,
height, width, weight,
created_on, launch_date, launch_time)
VALUES
('7 Shakra Bracelet', false, 5, 0,
123.123, 1234.1234, 100, 9765490,
12345, 98765, 12345678901234567890,
'2022-09-25 21:35:13', '2022-09-25', '09:30:17'),

('Anchor Bracelet Mens', true, 10, 1,
987.987, 5678.5678, 300, 1256793,
67890, 456235, 98765432109876543210,
'2022-12-21 21:35:13', '2023-01-12', '09:30:17'),

('Bangle Bracelet', true, 10, 0,
123.123, 1234.1234, 100, 9765490,
556677, 99441166, 11223344556677889900,
'2022-09-25 21:35:13', '2022-09-25', '09:30:17')
;

