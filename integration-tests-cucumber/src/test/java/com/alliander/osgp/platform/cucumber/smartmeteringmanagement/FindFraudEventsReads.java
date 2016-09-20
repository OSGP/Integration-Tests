/**
 * Copyright 2016 Smart Society Services B.V. *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.smartmeteringmanagement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.alliander.osgp.adapter.ws.schema.smartmetering.management.EventLogCategory;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.EventType;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FindFraudEventsReads extends AbstractFindEventsReads {

    private static final List<EventType> allowed = Collections.unmodifiableList(Arrays.asList(new EventType[] {
            EventType.TERMINAL_COVER_CLOSED, EventType.TERMINAL_COVER_REMOVED, EventType.STRONG_DC_FIELD_DETECTED,
            EventType.NO_STRONG_DC_FIELD_ANYMORE, EventType.METER_COVER_CLOSED, EventType.METER_COVER_REMOVED,
            EventType.FAILED_LOGIN_ATTEMPT, EventType.CONFIGURATION_CHANGE, EventType.EVENTLOG_CLEARED

    }));

    @Override
    protected String getEventLogCategory() {
        final String category = EventLogCategory.FRAUD_DETECTION_LOG.name();
        return category.substring(0, category.lastIndexOf('_'));
    }

    @When("^the find fraud events request is received$")
    @Override
    public void theFindEventsRequestIsReceived() throws Throwable {
        super.theFindEventsRequestIsReceived();
    }

    @Then("^fraud events should be returned$")
    @Override
    public void eventsShouldBeReturned() throws Throwable {
        super.eventsShouldBeReturned();
    }

    @Override
    protected List<EventType> getAllowedEventTypes() {
        return allowed;
    }
}
