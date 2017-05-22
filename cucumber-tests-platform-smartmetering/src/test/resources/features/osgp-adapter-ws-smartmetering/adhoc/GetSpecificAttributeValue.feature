@SmartMetering @Platform
Feature: SmartMetering - Adhoc - GetSpecificAttributeValue
  As a grid operator 
  I want to retrieve a specific attribute value from a meter

  Background: 
    Given a dlms device
      | DeviceIdentification | TEST1024000000001 |
      | DeviceType           | SMART_METER_E     |

  Scenario: Retrieve COSEM Logical Device Name in a a bundle
    Given a get specific attribute value request
      | DeviceIdentification | TEST1024000000001 |
      | ClassId              |                 1 |
      | ObisCodeA            |                 0 |
      | ObisCodeB            |                 0 |
      | ObisCodeC            |                42 |
      | ObisCodeD            |                 0 |
      | ObisCodeE            |                 0 |
      | ObisCodeF            |               255 |
      | Attribute            |                 2 |
    When the get specific attribute value request is received
    Then a get specific attribute value response should be returned
      | Result       | OK                                                        |
      | ResponsePart | bytes[100, 101, 118, 105, 99, 101, 32, 110, 97, 109, 101] |

  Scenario: Retrieve Administrative in/out
    Given a get specific attribute value request
      | DeviceIdentification | TEST1024000000001 |
      | ClassId              |                 1 |
      | ObisCodeA            |                 0 |
      | ObisCodeB            |                 1 |
      | ObisCodeC            |                94 |
      | ObisCodeD            |                31 |
      | ObisCodeE            |                 0 |
      | ObisCodeF            |               255 |
      | Attribute            |                 2 |
    When the get specific attribute value request is received
    Then a get specific attribute value response should be returned
      | Result       | OK                                    |
      | ResponsePart | Choice=ENUMERATE, ResultData isNumber |

  Scenario: Retrieve Currently Active Tariff
    Given a get specific attribute value request
      | DeviceIdentification | TEST1024000000001 |
      | ClassId              |                 1 |
      | ObisCodeA            |                 0 |
      | ObisCodeB            |                 0 |
      | ObisCodeC            |                96 |
      | ObisCodeD            |                14 |
      | ObisCodeE            |                 0 |
      | ObisCodeF            |               255 |
      | Attribute            |                 2 |
    When the get specific attribute value request is received
    Then a get specific attribute value response should be returned
      | Result       | OK            |
      | ResponsePart | bytes[65, 66] |