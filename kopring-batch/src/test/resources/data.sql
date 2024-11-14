

INSERT INTO ORDERS (name, quantity, category)
SELECT CONCAT('Order ', id), FLOOR(RAND() * 100), 'BOOK'
FROM (SELECT @id := @id + 1 AS id FROM information_schema.tables, (SELECT @id := 0) temp) temp2 LIMIT 1000000;
