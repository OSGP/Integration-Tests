/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.support.ws.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.alliander.osgp.adapter.ws.endpointinterceptors.OrganisationIdentification;
import com.alliander.osgp.adapter.ws.schema.microgrids.notification.SendNotificationRequest;
import com.alliander.osgp.adapter.ws.schema.microgrids.notification.SendNotificationResponse;
import com.alliander.osgp.platform.cucumber.support.ws.microgrids.NotificationService;
import com.alliander.osgp.shared.exceptionhandling.WebServiceException;

@Endpoint
public class MicrogridsNotificationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MicrogridsNotificationEndpoint.class);
    private static final String MICROGRIDS_NOTIFICATION_NAMESPACE = "http://www.alliander.com/schemas/osgp/microgrids/notification/2016/06";

    @Autowired
    private NotificationService notificationService;

    public MicrogridsNotificationEndpoint() {
        // Default constructor
    }

    @PayloadRoot(localPart = "SendNotificationRequest", namespace = MICROGRIDS_NOTIFICATION_NAMESPACE)
    @ResponsePayload
    public SendNotificationResponse sendNotification(
            @OrganisationIdentification final String organisationIdentification,
            @RequestPayload final SendNotificationRequest request) throws WebServiceException {

        LOGGER.info("Incoming SendNotificationRequest for organisation: {} device: {}.", organisationIdentification,
                request.getNotification().getDeviceIdentification());

        this.notificationService.handleNotification(request.getNotification(), organisationIdentification);

        return new SendNotificationResponse();
    }
}