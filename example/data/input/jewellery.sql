
DROP TABLE jewellery IF EXISTS;
CREATE TABLE jewellery (
	title VARCHAR(100),
	published BOOLEAN,
	variant_inventory_qty INTEGER,
	gift_card TINYINT,
	variant_price FLOAT,
	variant_compare_at_price DOUBLE,
	min_price DECIMAL(10,0),
	max_price DECIMAL(30,0),
	auction_start_price DECIMAL(25,3),
	sold_price DECIMAL(20,0),
	taxable CHAR,
	created_on TIMESTAMP,
	launch_date DATE,
	launch_time TIME
);

INSERT INTO jewellery
(title, published, variant_inventory_qty, gift_card,
variant_price, variant_compare_at_price, min_price, max_price, auction_start_price, sold_price, taxable,
created_on, launch_date, launch_time)
VALUES
('7 Shakra Bracelet', false, 5, 0,
123.123, 1234.1234, 100, 9765490, 1000.123, 67890567, 'N', 
'2022-09-25 21:35:13', '2022-09-25', '09:30:17'),

('Anchor Bracelet Mens', true, 10, 1,
987.987, 5678.5678, 300, 1256793, 5600.987, 44889986, 'Y', 
'2022-12-21 21:35:13', '2023-01-12', '09:30:17'),

('Bangle Bracelet', true, 10, 0,
123.123, 1234.1234, 100, 9765490, 1000.123, 67890567, 'Y', 
'2022-09-25 21:35:13', '2022-09-25', '09:30:17')
;

