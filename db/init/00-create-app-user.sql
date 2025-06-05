-- Создание роли, если не существует
DO
$$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'app_user') THEN
CREATE ROLE app_user LOGIN PASSWORD 'secret_app_pass';
ELSE
    ALTER ROLE app_user WITH PASSWORD 'secret_app_pass';
END IF;
END
$$;

\echo '>>> ensure database msdb'

-- Создание базы данных, если её нет
DO
$$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'msdb') THEN
    CREATE DATABASE msdb OWNER app_user;
END IF;
END
$$;

ALTER DATABASE msdb OWNER TO app_user;

-- Выдаём все права на базу данных
GRANT ALL PRIVILEGES ON DATABASE msdb TO app_user;

\c msdb

GRANT USAGE, CREATE ON SCHEMA public TO app_user;


GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO app_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO app_user;


ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO app_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT USAGE, SELECT ON SEQUENCES TO app_user;
