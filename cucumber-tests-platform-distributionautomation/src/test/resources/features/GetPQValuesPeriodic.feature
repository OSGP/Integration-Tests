@GetPQValuesPeriodic @DistributionAutomation @Platform
Feature: DistributionAutomation - GetPQValuesPeriodic
  As a grid operator
  I want to get Periodic PQ values from a device
  So I can ...
  #### Todo: This feature is not yet implemented

  Scenario: Request Periodic PQ value from device
    Given an rtu iec61850 device
      | DeviceIdentification | WAGO61850ServerRTU1                                        |
      | IcdFilename          | Simple_substation_v0.14.icd |
      | Port                 | 62100                                                      |
    When I send a getPQValuesPeriodic request
      | DeviceIdentification | WAGO61850ServerRTU1      |
      | From                 | 1970-01-01T00:00:00.000Z |
      | To                   | 1970-01-02T00:00:00.000Z |
    And I receive a notification
    Then I should recieve a SOAP Fault for a getPQValuesPeriodicResponse request
      | DeviceIdentification | WAGO61850ServerRTU1       |
      | Component            | UNKNOWN                   |
      | Message              | An unknown error occurred |
