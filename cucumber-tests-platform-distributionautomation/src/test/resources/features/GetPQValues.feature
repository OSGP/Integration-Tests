@GetPQValues @DistributionAutomation @Platform
Feature: DistributionAutomation - GetPQValues
  As a grid operator
  I want to get PQ values from a device
  So I can ...

  Scenario: Request PQ value from device
    Given an rtu iec61850 device
      | DeviceIdentification | WAGO61850ServerRTU1                                        |
      | IcdFilename          | Simple_substation_v0.14.icd                                |
      | Port                 | 62100                                                      |
    When I send a getPQValues request
      | DeviceIdentification | WAGO61850ServerRTU1 |
    And I receive a notification
    Then I should recieve a response for a getPQValuesResponse request
      | DeviceIdentification     | WAGO61850ServerRTU1 |
      | Component                | PROTOCOL_IEC61850   |
      | Result                   | OK                  |
      | LogicalDevices           | 1                   |

      # LogicalDevice ( concat with _$deviceId ) : Device name
      # Logical Nodes ( concat with _$deviceId ) : Amount of nodes for device $deviceId
      # Logical Node Name ( concat with _$deviceId_$nodeId ) : Name of the node for device _$deviceId _$nodeId
      #-------------------------------------------------
      | LogicalDevice_1          | WAGO61850ServerRTU1 |
      | LogicalNodes_1           | 2                   |
      | LogicalNodeName_1_1      | MMXU1               |
      | LogicalNodeName_1_2      | MMXU2               |

      # DataSamples ( concat with _$deviceId_$nodeId )
      # Device 1 Node 1
      #-------------------------------------------------
      | A.phsA.cVal.mag.f_1_1    | 0.0                 |
      | A.phsB.cVal.mag.f_1_1    | 0.0                 |
      | A.phsC.cVal.mag.f_1_1    | 0.0                 |
      | VAr.phsA.cVal.mag.f_1_1  | 0.0                 |
      | VAr.phsB.cVal.mag.f_1_1  | 0.0                 |
      | VAr.phsC.cVal.mag.f_1_1  | 0.0                 |
      | TotVAr.mag.f_1_1         | 0.0                 |
      | PPV.phsAB.cVal.mag.f_1_1 | 0.0                 |
      | PPV.phsBC.cVal.mag.f_1_1 | 0.0                 |
      | PPV.phsCA.cVal.mag.f_1_1 | 0.0                 |
      | W.phsA.cVal.mag.f_1_1    | 0.0                 |
      | W.phsB.cVal.mag.f_1_1    | 0.0                 |
      | W.phsC.cVal.mag.f_1_1    | 0.0                 |
      | TotW.mag.f_1_1           | 0.0                 |

      # Device 1 Node 2
      #-------------------------------------------------
      | A.phsA.cVal.mag.f_1_2    | 0.0                 |
      | A.phsB.cVal.mag.f_1_2    | 0.0                 |
      | A.phsC.cVal.mag.f_1_2    | 0.0                 |
      | VAr.phsA.cVal.mag.f_1_2  | 0.0                 |
      | VAr.phsB.cVal.mag.f_1_2  | 0.0                 |
      | VAr.phsC.cVal.mag.f_1_2  | 0.0                 |
      | TotVAr.mag.f_1_2         | 0.0                 |
      | PPV.phsAB.cVal.mag.f_1_2 | 0.0                 |
      | PPV.phsBC.cVal.mag.f_1_2 | 0.0                 |
      | PPV.phsCA.cVal.mag.f_1_2 | 0.0                 |
      | W.phsA.cVal.mag.f_1_2    | 0.0                 |
      | W.phsB.cVal.mag.f_1_2    | 0.0                 |
      | W.phsC.cVal.mag.f_1_2    | 0.0                 |
      | TotW.mag.f_1_2           | 0.0                 |