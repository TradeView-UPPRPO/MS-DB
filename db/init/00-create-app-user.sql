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

-- создать базу, если её вдруг нет (на «чистом» кластере создастся entrypoint,
-- но если переменная POSTGRES_DB изменится, подстраховка не помешает)
DO
$$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'msdb') THEN
    CREATE DATABASE msdb OWNER app_user;
END IF;
END
$$;

ALTER DATABASE msdb OWNER TO app_user;
GRANT ALL PRIVILEGES ON DATABASE msdb TO app_user;
