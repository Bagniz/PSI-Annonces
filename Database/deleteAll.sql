\echo '********* Delete All The Data *********'

-- Stop script on error
\set ON_ERROR_STOP true

-- Execute queries quietly
\set QUIET on

-- Delete existing tables
DO $$
    BEGIN
        DROP TABLE IF EXISTS log;
        DROP TABLE IF EXISTS adimages;
        DROP TABLE IF EXISTS ads;
        DROP TABLE IF EXISTS messages;
        DROP TABLE IF EXISTS clients;
    END;
$$ LANGUAGE plpgsql;
\echo 'Deleted all the tables and triggers'

-- Delete existing functions
DO $$
    BEGIN
        DROP EXTENSION IF EXISTS pgcyrpto;
        DROP FUNCTION IF EXISTS encrypt_password;
        DROP FUNCTION IF EXISTS log_operations;
    END;
$$ LANGUAGE plpgsql;
\echo 'Deleted existing functions'