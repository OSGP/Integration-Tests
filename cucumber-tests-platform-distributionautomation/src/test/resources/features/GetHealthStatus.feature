@GetHealthStatus @DistributionAutomation @Platform
Feature: DistributionAutomation - GetHealthStatus
  As a grid operator
  I want to get Health Status from a device
  So I can ...

  Scenario: Request Health Status from device
    Given an rtu iec61850 device
      | DeviceIdentification | WAGO61850ServerRTU1                                        |
      | IcdFilename          | Simple_substation_v0.14.icd |
      | Port                 | 62100                                                      |
    When I send a getHealthStatus request
      | DeviceIdentification | WAGO61850ServerRTU1 |
    And I receive a notification
    Then I should recieve a response for a getHealthStatus request
      | DeviceIdentification     | WAGO61850ServerRTU1 |
      | Result                   | OK                  |
      | HealthStatus             | Ok                  |