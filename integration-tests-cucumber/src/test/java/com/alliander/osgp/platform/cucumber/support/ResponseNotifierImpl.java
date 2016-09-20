/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource("file:/etc/osp/osgp-cucumber-response-data-smart-metering.properties")
public class ResponseNotifierImpl implements ResponseNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseNotifierImpl.class);

    private static final int FIRST_WAIT_TIME = 1000;
    
    private Connection connection;

    @Value("${cucumber.osgpadapterwssmartmeteringdbs.url}")
    private String jdbcUrl;

    @Value("${cucumber.dbs.username}")
    private String username;

    @Value("${cucumber.dbs.password}")
    private String password;

    @Override
    public boolean waitForResponse(final String correlid, final int timeout, final int maxtime) {
        Statement statement = null;
        try {
            statement = this.conn().createStatement();
            
            //check if we have (almost) immediate response
            Thread.sleep(FIRST_WAIT_TIME);
            PollResult pollres = this.pollDatabase(statement, correlid);
            if (pollres.equals(PollResult.OK)) {
                return true;
            }
            
            int delayedtime = 0;
            while (true) {
                Thread.sleep(timeout);
                if ((delayedtime += timeout) < maxtime) {
                    pollres = this.pollDatabase(statement, correlid);
                    if (pollres.equals(PollResult.OK)) {
                        return true;
                    } else if (pollres.equals(PollResult.ERROR)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (final SQLException se) {
            LOGGER.error(se.getMessage());
            return false;
        } catch (final InterruptedException intex) {
            LOGGER.error(intex.getMessage());
            return false;
        } finally {
            this.closeStatement(statement);
        }
    }

    private PollResult pollDatabase(final Statement statement, final String correlid) {
        ResultSet rs = null;
        PollResult result = PollResult.NOT_OK;
        try {
            rs = statement.executeQuery(
                    "SELECT count(*) FROM meter_response_data WHERE correlation_uid = '" + correlid + "'");
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    result = PollResult.OK;
                }
            }
            return result;
        } catch (final SQLException se) {
            LOGGER.error(se.getMessage());
            return PollResult.ERROR;
        } finally {
            this.closeResultSet(rs);
        }
    }

    private void closeStatement(final Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (final SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void closeResultSet(final ResultSet rs) {
        if (rs == null) {
            return;
        }
        try {
            rs.close();
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private Connection conn() {
        if (this.connection == null) {
            this.connection = this.connectToDatabaseOrDie();
        }
        return this.connection;
    }

    private Connection connectToDatabaseOrDie() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(this.jdbcUrl, this.username, this.password);
        } catch (final ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
            System.exit(1);
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage());
            System.exit(2);
        }
        return this.connection;
    }

    // -------------

    // To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    private enum PollResult {
        OK,
        NOT_OK,
        ERROR;
    }
}
