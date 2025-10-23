-- SAMPLE DATA

INSERT INTO users (name, email, password, admin) VALUES ('admin', 'admin@admin.com', '$2a$10$a79bdUMRlOxpceVdWvybiO48AJMJ73A/ywpiLT8bq8eE25TlvcR4.', true);
INSERT INTO companies (name) VALUES ('sample_company');
INSERT INTO locations (company_id, name, address) VALUES (1, 'sample_location', 'sample_address');
INSERT INTO tasks (company_id, location_id, name, description) VALUES (1, 1, 'sample_task', 'this is a sample task');
