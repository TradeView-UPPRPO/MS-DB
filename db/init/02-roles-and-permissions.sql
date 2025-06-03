-- app_user может только читать и изменять данные в существующих таблицах
-- CREATE USER app_user WITH PASSWORD 'secret_app_pass';

GRANT CONNECT ON DATABASE msdb TO app_user;
GRANT USAGE ON SCHEMA public TO app_user;

GRANT SELECT, INSERT, UPDATE, DELETE
    ON ALL TABLES IN SCHEMA public
    TO app_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO app_user;

GRANT USAGE, SELECT
    ON ALL SEQUENCES IN SCHEMA public
    TO app_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO app_user;
