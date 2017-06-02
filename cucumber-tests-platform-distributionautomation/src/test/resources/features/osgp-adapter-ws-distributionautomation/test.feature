@DistributionAutomation @Platform
Feature: DistributionAutomation - GetPQValues
  As a grid operator
  I want to get PQ values from a device

  Scenario: Request PQ value from device
    Given an rtu iec61850 device
      | DeviceIdentification | RTU-PAMPUS             |
      | IcdFilename          | MarkerWadden_0_1_1.icd |
      | Port                 |                  62102 |
    When a get pq value request is received
      | DeviceIdentification      | RTU-PAMPUS |
      | NumberOfSystems           |          1 |
      | SystemId_1                |          1 |
      | SystemType_1              | BOILER     |
      | NumberOfMeasurements_1    |          1 |
      | MeasurementFilterNode_1_1 | Health     |
    Then the get pq value response should be returned
      | DeviceIdentification | RTU-PAMPUS        |
      | Component            | PROTOCOL_IEC61850 |
      | Message              | ???               |