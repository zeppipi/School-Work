SET ECHO ON

SPOOL week7_schema_alter_output.txt

ALTER TABLE unit 
ADD(unit_credit     NUMBER(1)  DEFAULT 6); 

COMMENT ON COLUMN unit.unit_credit IS
    'Unit credits';
    
SPOOL OFF

SET ECHO OFF