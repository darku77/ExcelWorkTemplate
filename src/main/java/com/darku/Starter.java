// =============================================================================
// Copyright/Security Notice
//
// Licensed Materials - Property of Qnamic AG
// (C) Copyright Qnamic AG 2003-2022
// All rights reserved
//
// End Copyright
// =============================================================================
package com.darku;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.darku.generator.WorkReportGenerator;

import lombok.extern.log4j.Log4j2;

/**
 * @author Catalin on 01.09.2022
 */
@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@Log4j2
public class Starter implements CommandLineRunner {


    private final WorkReportGenerator reportGenerator;

    public Starter(WorkReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Starter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("***** STARTING UP ********");
        reportGenerator.generate();
    }

}
