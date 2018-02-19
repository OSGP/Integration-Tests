# Needs a DlmsDevice simulator with e650 profile on port 1026
@SmartMetering @Platform @SN
Feature: SmartMetering short names - Ad hoc

  Scenario: Get specific attribute value from L+G E650
    Given a dlms device
      | DeviceIdentification | TEST1024000000005 |
      | DeviceType           | SMART_METER_E     |
      | UseSn                | true              |
      | UseHdlc              | true              |
      | Port                 |              1026 |
      | Hls5active           | false             |
      | Lls1active           | true              |
    When the get specific attribute value request is received
      | DeviceIdentification | TEST1024000000005 |
      | ClassId              |                 3 |
      | ObisCodeA            |                 0 |
      | ObisCodeB            |                 0 |
      | ObisCodeC            |                96 |
      | ObisCodeD            |               240 |
      | ObisCodeE            |                12 |
      | ObisCodeF            |               255 |
      | Attribute            |                18 |
    Then a get specific attribute value response should be returned
      | DeviceIdentification | TEST1024000000005 |
      | Result               | OK                |
      | ResponsePart         |                 4 |

  Scenario: Get event log capture objects from L+G E650
    Given a dlms device
      | DeviceIdentification | TEST1024000000005 |
      | DeviceType           | SMART_METER_E     |
      | UseSn                | true              |
      | UseHdlc              | true              |
      | Port                 |              1026 |
      | Hls5active           | false             |
      | Lls1active           | true              |
    When the get specific attribute value request is received
      | DeviceIdentification | TEST1024000000005 |
      | ClassId              |                 7 |
      | ObisCodeA            |                 1 |
      | ObisCodeB            |                 0 |
      | ObisCodeC            |                99 |
      | ObisCodeD            |                98 |
      | ObisCodeE            |                 0 |
      | ObisCodeF            |               255 |
      | Attribute            |                 3 |
    Then a get specific attribute value response should be returned
      | DeviceIdentification | TEST1024000000005 |
      | Result               | OK                |
      | ResponsePart         | 0-0:1.0.0.255     |
