DO
$$
BEGIN

  IF NOT EXISTS(SELECT 1
                FROM device
                WHERE device_identification = 'WAGO61850ServerRTU1')
  THEN
    INSERT INTO "device" ( "creation_time", "device_identification", "device_type", "id", "in_maintenance", "is_activated", "is_active", "modification_time", "network_address", "protocol_info_id", "version" )
        VALUES ( '2016-01-01 00:00:00', 'WAGO61850ServerRTU1', 'RTU', 1, false, true, true, '2016-01-01 00:00:00', '127.0.0.1', 5, 0 );

    INSERT INTO "rtu_device" ( "id" ) VALUES ( 1 );

    INSERT INTO "device_authorization" ( "creation_time", "device", "function_group", "id", "modification_time", "organisation", "version")
        VALUES ( '2016-01-01 00:00:00', 1, 0, 1, '2016-01-01 00:00:00', 4, 0 );
  END IF;

END;
$$