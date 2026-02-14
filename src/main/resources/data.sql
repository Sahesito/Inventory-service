INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 1, 50, 5, 10, 100, 'Warehouse-A'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 1);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 2, 30, 3, 5, 50, 'Warehouse-A'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 2);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 3, 25, 2, 5, 50, 'Warehouse-A'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 3);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 4, 15, 1, 5, 30, 'Warehouse-B'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 4);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 5, 100, 10, 20, 200, 'Warehouse-C'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 5);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 6, 75, 8, 15, 150, 'Warehouse-C'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 6);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 7, 60, 6, 15, 120, 'Warehouse-C'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 7);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 8, 40, 4, 10, 80, 'Warehouse-D'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 8);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 9, 35, 3, 10, 70, 'Warehouse-D'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 9);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 10, 20, 2, 5, 40, 'Warehouse-D'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 10);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 11, 150, 15, 30, 300, 'Warehouse-E'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 11);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 12, 200, 20, 40, 400, 'Warehouse-E'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 12);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 13, 80, 8, 20, 160, 'Warehouse-F'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 13);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 14, 45, 5, 10, 90, 'Warehouse-F'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 14);

INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, max_stock_level, location)
SELECT 15, 12, 1, 3, 25, 'Warehouse-F'
    WHERE NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = 15);