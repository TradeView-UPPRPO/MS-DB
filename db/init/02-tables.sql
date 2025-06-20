-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users
(
    id           BIGSERIAL PRIMARY KEY,
    username     TEXT        NOT NULL UNIQUE,
    telegram_id  BIGINT      NOT NULL UNIQUE,
    role         user_role   NOT NULL DEFAULT 'USER',
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
    );

-- Активы пользователя
CREATE TABLE IF NOT EXISTS assets
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    symbol     TEXT        NOT NULL,
    type       asset_type  NOT NULL,
    amount     DECIMAL(28, 8)        NOT NULL,
    parameters JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (user_id, symbol)
    );

-- Котировки с внешних API
CREATE TABLE IF NOT EXISTS market_data
(
    id         BIGSERIAL PRIMARY KEY,
    symbol     TEXT        NOT NULL,
    source     TEXT        NOT NULL,
    data       JSONB       NOT NULL,
    price      NUMERIC,
    volume     NUMERIC,
    change_pct NUMERIC,
    fetched_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
    );

-- Результаты анализа
CREATE TABLE IF NOT EXISTS analysis_data
(
    id         BIGSERIAL PRIMARY KEY,
    symbol     TEXT        NOT NULL,
    analysis   JSONB       NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
    );

-- Новости по активам
CREATE TABLE IF NOT EXISTS news_data
(
    id           BIGSERIAL PRIMARY KEY,
    symbol       TEXT,
    title        TEXT        NOT NULL,
    url          TEXT        NOT NULL UNIQUE,
    image_url    TEXT,
    published_at TIMESTAMPTZ NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
    );

-- Настройки уведомлений
CREATE TABLE IF NOT EXISTS notification_settings
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    symbol     TEXT        NOT NULL,
    conditions JSONB       NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
    );
