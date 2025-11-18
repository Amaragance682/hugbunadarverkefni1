DROP TABLE IF EXISTS edit_requests CASCADE;
DROP TABLE IF EXISTS shift_flags CASCADE;
DROP TABLE IF EXISTS shift_notes CASCADE;
DROP TABLE IF EXISTS shift_breaks CASCADE;
DROP TABLE IF EXISTS shift_tasks CASCADE;
DROP TABLE IF EXISTS shifts CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS user_company_contracts CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS audit_log CASCADE;

CREATE TABLE users (
    id serial primary key,
    name varchar(128) not null,
    email varchar(128) not null unique,
    password varchar(256) not null,
    admin boolean not null,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp
);

CREATE TABLE companies (
    id serial primary key,
    name varchar(256) not null,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp
);

CREATE TABLE locations (
    id serial primary key,
    company_id int references companies(id) not null,
    name varchar(128) not null,
    address varchar(256) not null,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp,
    unique(company_id, address)
);

CREATE TABLE user_company_contracts (
    id serial primary key,
    user_id int references users(id) not null,
    company_id int references companies(id) not null,
    contract_settings JSONB,
    role text check (role in ('EMPLOYEE', 'MANAGER')) not null,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp
);

CREATE TABLE tasks (
    id serial primary key,
    company_id int references companies(id) not null,
    location_id int references locations(id),
    name text not null,
    description text,
    is_finished boolean default false,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp
);

CREATE TABLE shifts (
    id serial primary key,
    contract_id int references user_company_contracts(id) not null,
    start_ts timestamptz not null,
    end_ts timestamptz,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp
);

CREATE TABLE shift_tasks (
    id serial primary key,
    shift_id int not null,
    task_id int references tasks(id),
    start_ts timestamptz not null,
    end_ts timestamptz,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp,
    foreign key (shift_id) references shifts(id) on delete cascade
);

CREATE TABLE shift_breaks (
    id serial primary key,
    shift_id int not null,
    break_type text not null,
    start_ts timestamptz not null,
    end_ts timestamptz,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp,
    foreign key (shift_id) references shifts(id) on delete cascade
);

CREATE TABLE shift_notes (
    id serial primary key,
    shift_id int not null,
    note text not null,
    created_by int references users(id),
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp,
    foreign key (shift_id) references shifts(id) on delete cascade
);

CREATE TABLE edit_requests (
    id serial primary key,
    user_id int references users(id) not null,
    reason text,
    requested_changes JSONB not null,
    status text not null default 'pending',
    reviewed_by int references users(id),
    reviewed_at timestamptz,
    created timestamptz not null default current_timestamp,
    updated timestamptz not null default current_timestamp
);

CREATE TABLE audit_log (
    id serial primary key,
    actor_id int references users(id),
    entity_type text not null,
    entity_id int not null,
    action text not null,
    before_json jsonb,
    after_json jsonb,
    at_ts timestamptz not null default current_timestamp
);


-- FUNCTIONS

CREATE OR REPLACE FUNCTION audit_trigger()
RETURNS trigger AS $$
BEGIN
  IF TG_OP = 'DELETE' THEN
    INSERT INTO audit_log(actor_id, entity_type, entity_id, action, before_json, after_json)
    VALUES (
        COALESCE(NULLIF(current_setting('app.user_id', true), ''), NULL)::INT,
        TG_TABLE_NAME,
        OLD.id,
        TG_OP,
        row_to_json(OLD)::jsonb,
        NULL
    );
    RETURN OLD;

  ELSIF TG_OP = 'UPDATE' THEN
    INSERT INTO audit_log(actor_id, entity_type, entity_id, action, before_json, after_json)
    VALUES (
        COALESCE(NULLIF(current_setting('app.user_id', true), ''), NULL)::INT,
        TG_TABLE_NAME,
        NEW.id,
        TG_OP,
        row_to_json(OLD)::jsonb,
        row_to_json(NEW)::jsonb
    );
    RETURN NEW;

  ELSIF TG_OP = 'INSERT' THEN
    INSERT INTO audit_log(actor_id, entity_type, entity_id, action, before_json, after_json)
    VALUES (
        COALESCE(NULLIF(current_setting('app.user_id', true), ''), NULL)::INT,
        TG_TABLE_NAME,
        NEW.id,
        TG_OP,
        NULL,
        row_to_json(NEW)::jsonb
        );
    RETURN NEW;
  END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER companies_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON companies
    FOR EACH ROW
    EXECUTE FUNCTION audit_trigger();
CREATE TRIGGER locations_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON locations
    FOR EACH ROW
    EXECUTE FUNCTION audit_trigger();
CREATE TRIGGER contract_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON user_company_contracts
    FOR EACH ROW
    EXECUTE FUNCTION audit_trigger();
CREATE TRIGGER tasks_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON tasks
    FOR EACH ROW
    EXECUTE FUNCTION audit_trigger();
CREATE TRIGGER shifts_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON shifts
    FOR EACH ROW
    EXECUTE FUNCTION audit_trigger();
CREATE TRIGGER shift_tasks_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON shift_tasks
    FOR EACH ROW
    EXECUTE FUNCTION audit_trigger();
CREATE TRIGGER shift_breaks_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON shift_breaks
    FOR EACH ROW
    EXECUTE FUNCTION audit_trigger();
CREATE TRIGGER edit_requests_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON edit_requests
    FOR EACH ROW
    EXECUTE FUNCTION audit_trigger();
