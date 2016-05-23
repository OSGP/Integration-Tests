/**
 * Copyright 2016 Smart Society Services B.V. *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.smartmeteringmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.platform.cucumber.support.DeviceId;
import com.alliander.osgp.platform.cucumber.support.OrganisationId;
import com.alliander.osgp.platform.cucumber.support.RunXpathResult;
import com.alliander.osgp.platform.cucumber.support.TestCaseResult;
import com.alliander.osgp.platform.cucumber.support.TestCaseRunner;
import com.alliander.osgp.platform.cucumber.support.WsdlProjectFactory;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCaseRunner;
import com.eviware.soapui.model.iface.MessageExchange;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ReadAlarmRegister {
    private TestCase testCase;
    private String response;
    private String correlationUid;

    private static final String PATH_RESULT_ALARMTYPES = "/Envelope/Body/ReadAlarmRegisterResponse/AlarmRegister/AlarmTypes/text()";

    private static final String XPATH_MATCHER_RESULT_ALARMTYPES = "\\w[A-Z]";

    private static final String SOAP_PROJECT_XML = "../cucumber/soap-ui-project/FLEX-OVL-V3---SmartMetering-soapui-project.xml";
    private static final String TEST_SUITE_XML = "SmartmeterMonitoring";
    private static final String TEST_CASE_XML = "192 Read alarm register";
    private static final String TEST_CASE_NAME_REQUEST = "ReadAlarmRegister - Request 1";
    private static final String TEST_CASE_NAME_RESPONSE = "GetReadAlarmRegisterResponse - Request 1";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadAlarmRegister.class);
    private static final Map<String, String> PROPERTIES_MAP = new HashMap<>();

    private Pattern correlationUidPattern;
    private Matcher correlationUidMatcher;

    @Autowired
    private WsdlProjectFactory wsdlProjectFactory;

    @Autowired
    private TestCaseRunner testCaseRunner;

    @Autowired
    private RunXpathResult runXpathResult;

    @Autowired
    private DeviceId deviceId;

    @Autowired
    private OrganisationId organisationId;

    @When("^the get read alarm register request is received$")
    public void theGetActualMeterReadsRequestIsReceived() throws Throwable {
        this.correlationUidPattern = Pattern.compile(this.organisationId.getOrganisationId()
                + "\\|\\|\\|\\S{17}\\|\\|\\|\\S{17}");
        this.testCase = this.wsdlProjectFactory.createWsdlTestCase(SOAP_PROJECT_XML, TEST_SUITE_XML, TEST_CASE_XML);

        PROPERTIES_MAP.put("DeviceIdentificationE", this.deviceId.getDeviceId());
        PROPERTIES_MAP.put("OrganisationIdentification", this.organisationId.getOrganisationId());

        final TestCaseResult runTestStepByName = this.testCaseRunner.runWsdlTestCase(this.testCase, PROPERTIES_MAP,
                TEST_CASE_NAME_REQUEST);

        final TestStepResult runTestStepByNameResult = runTestStepByName.getRunTestStepByName();
        final WsdlTestCaseRunner wsdlTestCaseRunner = runTestStepByName.getResults();
        assertEquals(TestStepStatus.OK, runTestStepByNameResult.getStatus());

        for (final TestStepResult tcr : wsdlTestCaseRunner.getResults()) {
            this.response = ((MessageExchange) tcr).getResponseContent();
            this.correlationUidMatcher = this.correlationUidPattern.matcher(this.response);
        }
        assertTrue(this.correlationUidMatcher.find());
        this.correlationUid = this.correlationUidMatcher.group();
    }

    @Then("^the alarm register should be returned$")
    public void theActualMeterReadsResultShouldBeReturned() throws Throwable {
        PROPERTIES_MAP.put("CorrelationUid", this.correlationUid);

        final TestCaseResult runTestStepByName = this.testCaseRunner.runWsdlTestCase(this.testCase, PROPERTIES_MAP,
                TEST_CASE_NAME_RESPONSE);
        final TestStepResult runTestStepByNameResult = runTestStepByName.getRunTestStepByName();
        final WsdlTestCaseRunner wsdlTestCaseRunner = runTestStepByName.getResults();
        assertEquals(TestStepStatus.OK, runTestStepByNameResult.getStatus());

        for (final TestStepResult tcr : wsdlTestCaseRunner.getResults()) {
            LOGGER.info(TEST_CASE_NAME_RESPONSE + " response {}",
                    this.response = ((MessageExchange) tcr).getResponseContent());
        }

        assertTrue(this.runXpathResult.assertXpath(this.response, PATH_RESULT_ALARMTYPES,
                XPATH_MATCHER_RESULT_ALARMTYPES));
    }
}