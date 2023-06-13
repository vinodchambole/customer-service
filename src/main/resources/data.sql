INSERT INTO customers (id, name, email, transaction_password)
VALUES (1, 'Admin Admin', 'admin@axis.com', '00000');

INSERT INTO accounts (id, account_number, balance, account_type, email, created_at, updated_at, customer_id)
VALUES (1, '0000000000', 10000000000000.00, 'SAVINGS', 'admin@axis.com', '2023-06-13 12:34:56', '2023-06-13 12:34:56', 1);

INSERT INTO user (id, firstname, lastname, date_Of_Birth, email, phone_Number, address, password, aadhar_Number, pan_Number, gender, role)
VALUES (1, 'Admin', 'Admin', '1990-01-01', 'admin@axis.com', '1234567890', '123 Main St', '$2a$10$KQF/AwoKkPpNs.IRsJTrtOwBTFCytda1CVRuHCirrkVhxwqXLNbPG', '1234567890', 'ABCDE1234F', 'Male', 'ADMIN');
