USE fashion_store;

-- MEN'S PRODUCTS (ID 1-7)
UPDATE products SET image_url = 'assets/images/men1.jpg' WHERE product_id = 1;
UPDATE products SET image_url = 'assets/images/men2.jpg' WHERE product_id = 2;
UPDATE products SET image_url = 'assets/images/men3.jpg' WHERE product_id = 3;
UPDATE products SET image_url = 'assets/images/men4.jpg' WHERE product_id = 4;
UPDATE products SET image_url = 'assets/images/men5.jpg' WHERE product_id = 5;
UPDATE products SET image_url = 'assets/images/men6.jpg' WHERE product_id = 6;
UPDATE products SET image_url = 'assets/images/men7.jpg' WHERE product_id = 7;

-- WOMEN'S PRODUCTS (ID 8-14)
UPDATE products SET image_url = 'assets/images/women1.jpg' WHERE product_id = 8;
UPDATE products SET image_url = 'assets/images/women2.jpg' WHERE product_id = 9;
UPDATE products SET image_url = 'assets/images/women3.jpg' WHERE product_id = 10;
UPDATE products SET image_url = 'assets/images/women4.jpg' WHERE product_id = 11;
UPDATE products SET image_url = 'assets/images/women5.jpg' WHERE product_id = 12;
UPDATE products SET image_url = 'assets/images/women6.jpg' WHERE product_id = 13;
UPDATE products SET image_url = 'assets/images/women7.jpg' WHERE product_id = 14;

-- KIDS' PRODUCTS (ID 15-19)
UPDATE products SET image_url = 'assets/images/kids1.jpg' WHERE product_id = 15;
UPDATE products SET image_url = 'assets/images/kids2.jpg' WHERE product_id = 16;
UPDATE products SET image_url = 'assets/images/kid3.jpg'  WHERE product_id = 17;
UPDATE products SET image_url = 'assets/images/kid4.jpg'  WHERE product_id = 18;
UPDATE products SET image_url = 'assets/images/kid5.jpg'  WHERE product_id = 19;

-- Verify
SELECT product_id, name, image_url FROM products ORDER BY product_id;
