\echo '******** Set The Database ********'

\c db_psi

\echo '********* Create The Triggers *********'

-- Stop script on error
\set ON_ERROR_STOP true

-- Execute queries quietly
\set QUIET on

-- Delete existing Triggers
DO $$
    BEGIN
        DROP TRIGGER IF EXISTS CLIENTS_LOG ON clients;
        DROP TRIGGER IF EXISTS CLIENTS_ENCRYPT_PASSWORD ON clients;
        DROP TRIGGER IF EXISTS MESSAGES_LOG ON messages;
        DROP TRIGGER IF EXISTS ADS_LOG ON ads;
        DROP TRIGGER IF EXISTS ADIMAGES_LOG ON adimages;
    END;
$$ LANGUAGE plpgsql;
\echo 'Deleted existing triggers'

-- Clients
CREATE TRIGGER CLIENTS_LOG
    AFTER INSERT OR UPDATE OR DELETE
    ON clients
    FOR EACH ROW
    EXECUTE PROCEDURE log_operations();
\echo 'Created CLIENTS_LOG trigger ON distributors'

-- Messages
CREATE TRIGGER MESSAGES_LOG
    AFTER INSERT OR UPDATE OR DELETE
    ON messages
    FOR EACH ROW
    EXECUTE PROCEDURE log_operations();
\echo 'Created MESSAGES_LOG trigger ON distributors'

-- Ads
CREATE TRIGGER ADS_LOG
    AFTER INSERT OR UPDATE OR DELETE
    ON ads
    FOR EACH ROW
    EXECUTE PROCEDURE log_operations();
\echo 'Created ADS_LOG trigger ON distributors'

-- Reservations
CREATE TRIGGER RESERVATION_LOG
    AFTER INSERT OR UPDATE OR DELETE
    ON reservations
    FOR EACH ROW
    EXECUTE PROCEDURE log_operations();
\echo 'Created RESERVATION_LOG trigger ON distributors'

-- Adimages
CREATE TRIGGER ADIMAGES_LOG
    AFTER INSERT OR UPDATE OR DELETE
    ON adimages
    FOR EACH ROW
    EXECUTE PROCEDURE log_operations();
\echo 'Created ADIMAGES_LOG trigger ON distributors'

-- Client Encryption
CREATE TRIGGER CLIENTS_ENCRYPT_PASSWORD
    BEFORE INSERT OR UPDATE
    ON clients
    FOR EACH ROW
    EXECUTE PROCEDURE encrypt_password();
\echo 'Created CLIENTS_ENCRYPT_PASSWORD trigger ON subscribers'
