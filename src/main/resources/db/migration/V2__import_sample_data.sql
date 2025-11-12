-- SAMPLE DATA

-- USERS
INSERT INTO users (name, email, password, admin, created, updated) VALUES ('admin', 'admin@admin.com', '$2a$10$a79bdUMRlOxpceVdWvybiO48AJMJ73A/ywpiLT8bq8eE25TlvcR4.', true, current_timestamp, current_timestamp);
INSERT INTO users (name, email, password, admin, created, updated) VALUES ('employee', 'employee@employee.com', '$2a$10$a79bdUMRlOxpceVdWvybiO48AJMJ73A/ywpiLT8bq8eE25TlvcR4.', false, current_timestamp, current_timestamp);
INSERT INTO users (name, email, password, admin, created, updated) VALUES ('manager', 'manager@manager.com', '$2a$10$a79bdUMRlOxpceVdWvybiO48AJMJ73A/ywpiLT8bq8eE25TlvcR4.', false, current_timestamp, current_timestamp);

-- COMPANIES
INSERT INTO companies (name, created, updated) VALUES ('sample_company', current_timestamp, current_timestamp);

-- CONTRACTS
INSERT INTO user_company_contracts (user_id, company_id, role)
VALUES (2, 1, 'EMPLOYEE');
INSERT INTO user_company_contracts (user_id, company_id, role)
VALUES (3, 1, 'MANAGER');

-- LOCATIONS
INSERT INTO locations (company_id, name, address, created, updated) VALUES (1, 'sample_location', 'sample_address', current_timestamp, current_timestamp);

-- TASKS
INSERT INTO tasks (company_id, location_id, name, description, is_finished, created, updated) VALUES (1, 1, 'sample_task', 'this is a sample task', false, current_timestamp, current_timestamp);
INSERT INTO tasks (company_id, location_id, name, description, is_finished, created, updated) VALUES (1, 1, 'sample_task2', 'second task to switch to', false, current_timestamp, current_timestamp);

-- SHIFTS
INSERT INTO shifts (contract_id, start_ts, end_ts)
VALUES (1, '2025-11-12T00:10:00.000Z', '2025-11-12T00:21:00.000Z');
INSERT INTO shift_tasks (shift_id, task_id, start_ts, end_ts)
VALUES (1, 1, '2025-11-12T00:10:00.000Z', '2025-11-12T00:15:00.000Z');
INSERT INTO shift_tasks (shift_id, task_id, start_ts, end_ts)
VALUES (1, 2, '2025-11-12T00:15:00.000Z', '2025-11-12T00:21:00.000Z');
INSERT INTO shift_breaks (shift_id, break_type, start_ts, end_ts)
VALUES (1, 'Lunch', '2025-11-12T00:12:00.000Z', '2025-11-12T00:13:00.000Z');
