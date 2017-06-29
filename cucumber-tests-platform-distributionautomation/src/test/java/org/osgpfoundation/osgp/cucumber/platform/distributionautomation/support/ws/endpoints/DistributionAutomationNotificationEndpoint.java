/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.endpoints;

import com.alliander.osgp.adapter.ws.endpointinterceptors.OrganisationIdentification;
import com.alliander.osgp.shared.exceptionhandling.WebServiceException;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.notification.SendNotificationRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.notification.SendNotificationResponse;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class DistributionAutomationNotificationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributionAutomationNotificationEndpoint.class);
    private static final String DISTRIBUTIONAUTOMATION_NOTIFICATION_NAMESPACE = "http://www.osgpfoundation.org/schemas/osgp/distributionautomation/notification/2017/04";


    @Autowired
    private NotificationService notificationService;

    public DistributionAutomationNotificationEndpoint() {
        // Default constructor
    }

    @PayloadRoot(localPart = "SendNotificationRequest", namespace = DISTRIBUTIONAUTOMATION_NOTIFICATION_NAMESPACE)
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
