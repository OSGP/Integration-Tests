@GetDeviceModel @DistributionAutomation @Platform
Feature: DistributionAutomation - GetDeviceModel
  As a grid operator
  I want to get device model from a device
  So I can ...

  Scenario: Request DeviceModel from device
    Given an rtu iec61850 device
      | DeviceIdentification | WAGO61850ServerRTU1                                        |
      | IcdFilename          | Simple_substation_v0.14.icd |
      | Port                 | 62100                                                      |
    When I send a getDeviceModel request
      | DeviceIdentification | WAGO61850ServerRTU1 |
    And I receive a notification
    Then I should recieve a response for a getDeviceModel request
      | DeviceIdentification     | WAGO61850ServerRTU1 |
      | Result                   | OK                  |
      #---------------------------------------------------
      # increase field validation on fields!
      #       <ns2:GetDeviceModelResponse xmlns:ns2="http://www.osgpfoundation.org/schemas/osgp/distributionautomation/defs/2017/04" xmlns:ns3="http://www.alliander.com/schemas/osgp/common/2014/10">
      #         <ns2:Result>OK</ns2:Result>
      #         <ns2:DeviceIdentification>WAGO61850ServerRTU1</ns2:DeviceIdentification>
      #         <ns2:PhysicalServer id="WAGO61850ServerRTU1">
      #           <ns2:LogicalDevice id="WAGO61850ServerRTU1">
      #             <ns2:LogicalNode name="LLN0"/>
      #             <ns2:LogicalNode name="LPHD1"/>
      #             <ns2:LogicalNode name="MMXU1"/>
      #             <ns2:LogicalNode name="MMXU2"/>
      #           </ns2:LogicalDevice>
      #         </ns2:PhysicalServer>
      #       </ns2:GetDeviceModelResponse>
