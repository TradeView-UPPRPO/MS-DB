-- Роли пользователей
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

-- Тип актива
CREATE TYPE asset_type AS ENUM ('CRYPTO', 'STOCK');
