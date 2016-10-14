/**
 * Copyright 2016 Smart Society Services B.V.
 */
package com.alliander.osgp.platform.dlms.cucumber.steps.ws.smartmetering.smartmeteringconfiguration;

import static org.junit.Assert.assertTrue;

import com.alliander.osgp.platform.cucumber.steps.Defaults;
import com.alliander.osgp.platform.dlms.cucumber.steps.ws.smartmetering.SmartMeteringStepsBase;
import com.alliander.osgp.platform.cucumber.core.ScenarioContext;
import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SetActivityCalendar extends SmartMeteringStepsBase {
    private static final String PATH_RESULT = "/Envelope/Body/SetActivityCalendarResponse/Result/text()";

    private static final String TEST_SUITE_XML = "SmartmeterConfiguration";
    private static final String TEST_CASE_XML = "414 Use wildcards for date fields for activity calendar";
    private static final String TEST_CASE_NAME_REQUEST = "SetActivityCalendar - Request 1";
    private static final String TEST_CASE_NAME_GETRESPONSE_REQUEST = "GetSetActivityCalendarResponse - Request 1";

    @When("^the set activity calendar request is received$")
    public void theSetActivityCalendarRequestIsReceived() throws Throwable {
        PROPERTIES_MAP.put(DEVICE_IDENTIFICATION_LABEL, ScenarioContext.Current().get("DeviceIdentification", Defaults.DEFAULT_DEVICE_IDENTIFICATION).toString());
        PROPERTIES_MAP.put(ORGANISATION_IDENTIFICATION_LABEL, ScenarioContext.Current().get("OrganizationIdentification", Defaults.DEFAULT_ORGANIZATION_IDENTIFICATION).toString());

        this.requestRunner(TestStepStatus.OK, PROPERTIES_MAP, TEST_CASE_NAME_REQUEST, TEST_CASE_XML, TEST_SUITE_XML);
    }

    @Then("^the activity calendar profiles are set on the device$")
    public void theActivityCalendarProfilesAreSetOnTheDevice() throws Throwable {
        PROPERTIES_MAP.put(CORRELATION_UID_LABEL, ScenarioContext.Current().get("CorrelationUid").toString());

        this.requestRunner(TestStepStatus.OK, PROPERTIES_MAP, TEST_CASE_NAME_GETRESPONSE_REQUEST, TEST_CASE_XML, TEST_SUITE_XML);

        assertTrue(this.runXpathResult.assertXpath(this.response, PATH_RESULT, Defaults.EXPECTED_RESULT));
    }
}