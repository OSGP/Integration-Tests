/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.support;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alliander.osgp.platform.cucumber.SmartMetering;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCaseRunner;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.support.SoapUIException;

@Component
public class TestCaseRunner {

    @Autowired
    protected ResponseNotifier responseNotifier;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCaseRunner.class);

    public TestCaseResult runWsdlTestCase(final TestCase testCase, final Map<String, String> propertiesMap,
            final String testCaseNameRequest) throws XmlException, IOException, SoapUIException {
        final WsdlTestCase wsdlTestCase = (WsdlTestCase) testCase;

        for (final Entry<String, String> entry : propertiesMap.entrySet()) {
            wsdlTestCase.setPropertyValue(entry.getKey(), entry.getValue());
        }

        final WsdlTestCaseRunner wsdlTestCaseRunner = new WsdlTestCaseRunner(wsdlTestCase, new PropertiesMap());

        final String correlId = this.getCorrelId(propertiesMap);
        if (correlId != null) {
            if (!this.responseNotifier.waitForResponse(correlId, this.getTimeout(propertiesMap),
                    this.getMaxTime(propertiesMap))) {
                LOGGER.warn("no response retrieved within maximum time");
            } else {
                assertTrue(this.resetCorrelId(propertiesMap));
                // reset the correlationUid to null for the next request
            }
        }

        return new TestCaseResult(wsdlTestCaseRunner.runTestStepByName(testCaseNameRequest), wsdlTestCaseRunner);
    }

    private String getCorrelId(final Map<String, String> propertiesMap) {
        if (propertiesMap.containsKey(SmartMetering.CORRELATION_UID_LABEL)) {
            return propertiesMap.get(SmartMetering.CORRELATION_UID_LABEL);
        } else {
            return null;
        }
    }

    private boolean resetCorrelId(final Map<String, String> propertiesMap) {
        if (propertiesMap.containsKey(SmartMetering.CORRELATION_UID_LABEL)) {
            propertiesMap.put(SmartMetering.CORRELATION_UID_LABEL, null);
            return true;
        } else {
            return false;
        }
    }

    private int getTimeout(final Map<String, String> propertiesMap) {
        if (propertiesMap.containsKey(SmartMetering.TIME_OUT)) {
            return new Integer(propertiesMap.get(SmartMetering.TIME_OUT));
        } else {
            return 3000;
        }
    }

    private int getMaxTime(final Map<String, String> propertiesMap) {
        if (propertiesMap.containsKey(SmartMetering.MAX_TIME)) {
            return new Integer(propertiesMap.get(SmartMetering.MAX_TIME));
        } else {
            return 180000;
        }
    }

}
