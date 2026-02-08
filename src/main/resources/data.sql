-- Merchio Sample Data

-- Insert sample products
INSERT INTO products (name, description, category, price, stock_quantity, available, sku, created_at, updated_at)
VALUES
    ('iPhone 15 Pro', 'Latest Apple flagship smartphone with A17 Pro chip', 'Electronics', 999.99, 50, true, 'IP15-PRO-001', NOW(), NOW()),
    ('MacBook Pro 16"', 'Powerful laptop with M3 Pro chip for professionals', 'Electronics', 2499.99, 25, true, 'MBP-16-M3', NOW(), NOW()),
    ('AirPods Pro', 'Premium wireless earbuds with active noise cancellation', 'Accessories', 249.99, 100, true, 'APP-GEN2', NOW(), NOW()),
    ('Samsung Galaxy S24', 'Android flagship smartphone with advanced AI features', 'Electronics', 899.99, 0, false, 'SGS24-001', NOW(), NOW()),
    ('Dell XPS 15', 'High-performance Windows laptop for creators', 'Electronics', 1899.99, 15, true, 'DXPS-15', NOW(), NOW()),
    ('Sony WH-1000XM5', 'Industry-leading noise cancelling headphones', 'Accessories', 399.99, 30, true, 'SONY-WH5', NOW(), NOW()),
    ('iPad Air', 'Powerful and versatile tablet with M1 chip', 'Electronics', 599.99, 40, true, 'IPAD-AIR-M1', NOW(), NOW());

-- Insert sample orders
INSERT INTO orders (order_number, customer_email, customer_name, status, total_amount, payment_status, payment_method, shipping_address, created_at, updated_at)
VALUES
    ('ORD001ABC', 'john.doe@example.com', 'John Doe', 'SHIPPED', 999.99, 'COMPLETED', 'Credit Card', '123 Main St, New York, NY 10001', NOW() - INTERVAL '2 days', NOW()),
    ('ORD002XYZ', 'jane.smith@example.com', 'Jane Smith', 'DELIVERED', 2749.98, 'COMPLETED', 'PayPal', '456 Oak Ave, Los Angeles, CA 90001', NOW() - INTERVAL '5 days', NOW()),
    ('ORD003DEF', 'bob.johnson@example.com', 'Bob Johnson', 'PROCESSING', 1899.99, 'COMPLETED', 'Credit Card', '789 Pine Rd, Chicago, IL 60601', NOW() - INTERVAL '1 day', NOW()),
    ('ORD004GHI', 'alice.williams@example.com', 'Alice Williams', 'CONFIRMED', 649.98, 'COMPLETED', 'Apple Pay', '321 Elm St, Boston, MA 02101', NOW() - INTERVAL '3 hours', NOW());

-- Insert order items
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit, subtotal)
VALUES
    (1, 1, 1, 999.99, 999.99),
    (2, 2, 1, 2499.99, 2499.99),
    (2, 3, 1, 249.99, 249.99),
    (3, 5, 1, 1899.99, 1899.99),
    (4, 3, 1, 249.99, 249.99),
    (4, 6, 1, 399.99, 399.99);