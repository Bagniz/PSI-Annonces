\echo '******** Set The Database ********'

\c db_psi

\echo '******** Creating The Tables ********'

-- Stop script on error
\set ON_ERROR_STOP true

-- Execute queries quietly
\set QUIET on

-- Log
CREATE TABLE IF NOT EXISTS log(
    table_name TEXT NOT NULL,
    user_name TEXT,
    action_tstamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action TEXT NOT NULL CHECK (action IN ('INSERT', 'UPDATE', 'DELETE')),
    original_data TEXT,
    new_data TEXT,
    query TEXT NOT NULL
);
\echo 'Created log table'

-- Clients
CREATE TABLE IF NOT EXISTS clients(
    id SERIAL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL CHECK ( birthdate <= CURRENT_DATE - INTERVAL '18 year' ),
    email VARCHAR(255) NOT NULL UNIQUE CHECK ( email SIMILAR TO '[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}' ),
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    postal_code VARCHAR(5) NOT NULL CHECK ( postal_code SIMILAR TO '[0-9]{5}' ),
    city VARCHAR(128) NOT NULL,
    phone_number VARCHAR(128) NOT NULL CHECK ( phone_number SIMILAR TO '[+][0-9]{1,3}\s?[0-9]{3}\s?[0-9]{3}\s?[0-9]{3,4}' ),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);
\echo 'Created clients table'

-- Messages
CREATE TABLE IF NOT EXISTS messages(
    id SERIAL,
    sender INTEGER NOT NULL,
    receiver INTEGER NOT NULL,
    message TEXT NOT NULL,
    sending_date TIMESTAMP NOT NULL CHECK ( sending_date <= CURRENT_TIMESTAMP ),
    is_seen BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (sender) REFERENCES clients(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (receiver) REFERENCES clients(id) ON UPDATE CASCADE ON DELETE CASCADE
);
\echo 'Created messages table'

-- Categories
CREATE TABLE IF NOT EXISTS categories(
    id SERIAL,
    name TEXT NOT NULL,
    PRIMARY KEY (id)
);
\echo 'Created Categories table'

-- Ads
CREATE TABLE IF NOT EXISTS ads(
    id SERIAL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    price real NOT NULL CHECK ( price >=0 ),
    id_cat INTEGER NOT NULL,
    posted_by INTEGER NOT NULL,
    posting_date TIMESTAMP NOT NULL CHECK ( posting_date <= CURRENT_TIMESTAMP ),
    is_reserved BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (posted_by) REFERENCES clients(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_cat) REFERENCES categories(id) ON UPDATE CASCADE ON DELETE CASCADE
);
\echo 'Created ads table'

-- Adimages
CREATE TABLE IF NOT EXISTS adimages(
    id SERIAL,
    ad INTEGER NOT NULL,
    image TEXT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (ad) REFERENCES ads(id) ON UPDATE CASCADE ON DELETE CASCADE
);
\echo 'Created adimages table'
