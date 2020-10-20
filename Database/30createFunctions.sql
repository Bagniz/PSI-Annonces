\echo '******** Set The Database ********'

\c db_psi

\echo '********* Create The Functions *********'

-- Stop script on error
\set ON_ERROR_STOP true

-- Execute queries quietly
\set QUIET on

-- Delete existing functions
DO $$
    BEGIN
        DROP EXTENSION IF EXISTS pgcyrpto;
        DROP FUNCTION IF EXISTS encrypt_password;
        DROP FUNCTION IF EXISTS log_operations;
    END;
$$ LANGUAGE plpgsql;
\echo 'Deleted existing functions'

-- Log
CREATE OR REPLACE FUNCTION log_operations()
RETURNS TRIGGER AS $$
    DECLARE
        table_old_data TEXT;
        table_new_data TEXT;
    BEGIN
        IF (TG_OP = 'UPDATE') THEN -- An UPDATE Trigger
            table_old_data = OLD;
            table_new_data = NEW;
            -- Insert the data
            INSERT INTO log (table_name, user_name, action, original_data, new_data, query)
            VALUES (TG_TABLE_NAME::TEXT, SESSION_USER::TEXT, TG_OP, table_old_data, table_new_data, CURRENT_QUERY());
            RAISE NOTICE 'Logged action: %, on table: %', TG_OP, TG_TABLE_NAME;
            RETURN NEW;

        ELSEIF (TG_OP = 'DELETE') THEN -- A DELETE Trigger
            table_old_data = OLD;
            -- Insert the data
            INSERT INTO log (table_name, user_name, action, original_data, query)
            VALUES (TG_TABLE_NAME::TEXT, SESSION_USER::TEXT, TG_OP, table_old_data, CURRENT_QUERY());
            RAISE NOTICE 'Logged action: %, on table: %', TG_OP, TG_TABLE_NAME;
            RETURN OLD;

        ELSEIF (TG_OP = 'INSERT') THEN -- An INSERT Trigger
            table_new_data = NEW;
            -- Insert the data
            INSERT INTO log (table_name, user_name, action, new_data, query)
            VALUES (TG_TABLE_NAME::TEXT, SESSION_USER::TEXT, TG_OP, table_new_data, CURRENT_QUERY());
            RAISE NOTICE 'Logged action: %, on table: %', TG_OP, TG_TABLE_NAME;
            RETURN NEW;

        ELSE
            RAISE EXCEPTION '[LOG-OPERATIONS] - Other action occurred: % ,at %', TG_OP, NOW();
        END IF;
    END;
$$ LANGUAGE plpgsql;
\echo 'Created log_operations function'

-- Clients Password Encryption
CREATE OR REPLACE FUNCTION encrypt_password()
RETURNS TRIGGER AS $$
    BEGIN
        -- Add encryption extension
        PERFORM *
        FROM pg_extension
        WHERE extname = 'pgcrypto';

        IF (NOT FOUND) THEN
            CREATE EXTENSION IF NOT EXISTS pgcrypto;
        END IF;

        -- If it's an UPDATE the we test if the password is different
        IF (TG_OP = 'UPDATE') THEN
            IF (crypt(OLD.password, NEW.password)) THEN
                RAISE NOTICE 'The password is already encrypted';
                RETURN NEW;
            END IF;
        END IF;

        -- Encrypt the new password
        NEW.password := crypt(NEW.password, gen_salt('md5'));
        RAISE NOTICE 'New password is now encrypted';
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;
\echo 'Created encrypt_password function'
