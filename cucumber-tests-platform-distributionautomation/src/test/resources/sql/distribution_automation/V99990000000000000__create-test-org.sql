DO
$$
BEGIN

  IF NOT EXISTS(SELECT 1
                FROM organisation
                WHERE name = 'test-org')
  THEN
  INSERT INTO organisation(id, creation_time, modification_time, version, function_group, name, organisation_identification, enabled, domains, prefix)
    VALUES (nextval('organisation_id_seq'),'2013-01-01 00:00:00','2013-01-01 00:00:00',0,0,'test-org','test-org', TRUE, 'COMMON;PUBLIC_LIGHTING;TARIFF_SWITCHING;', 'TSO');
  END IF;

END;
$$