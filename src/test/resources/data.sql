INSERT INTO client (id, name, account_number, account_balance, created_date) VALUES (random_uuid(), 'John', '123456', 1000, current_timestamp);
INSERT INTO client (id, name, account_number, account_balance, created_date) VALUES (random_uuid(), 'Peter', '654321', 5000, current_timestamp);
INSERT INTO client (id, name, account_number, account_balance, created_date) VALUES (random_uuid(), 'Parker', '999888', 3000, current_timestamp);

INSERT INTO transfer (id, transfer_status, transfer_amount, sender_account, receiver_account, created_date)
VALUES (random_uuid(), 'COMPLETE', 800, '123456', '654321', current_timestamp);
INSERT INTO transfer (id, transfer_status, transfer_amount, sender_account, receiver_account, created_date)
VALUES (random_uuid(), 'COMPLETE', 800, '654321', '123456', current_timestamp);
INSERT INTO transfer (id, transfer_status, transfer_amount, sender_account, receiver_account, created_date)
VALUES (random_uuid(), 'INCOMPLETE', 800, '654321', '123456', current_timestamp);