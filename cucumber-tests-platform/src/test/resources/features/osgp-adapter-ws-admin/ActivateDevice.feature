Feature: Device management 
  As a grid operator
  I want to be able to perform DeviceManagement operations on a device
  In order to ...
    
	Scenario Outline: Activate a device
  	Given a device 
        | DeviceIdentification | <DeviceIdentification> |
        | Active               | <Active>               | 
     When receiving an activate device request
        | DeviceIdentification | <DeviceIdentification> |
	   Then the activate device response contains
	      | Result | <Result> |
	    And the device with device identification "<DeviceIdentification>" should be active

			Examples:
				| DeviceIdentification | Active | Result |
				| TEST1024000000001    | false  | OK     |
				