package com.darku;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.darku.generator.WorkReportExcelGenerator;
import com.darku.generator.WorkReportModelGenerator;
import com.darku.generator.model.AppProperties;

import lombok.extern.log4j.Log4j2;

/**
 * @author Catalin on 01.09.2022
 */
@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@Log4j2
public class Starter implements CommandLineRunner {

    private final WorkReportExcelGenerator reportGenerator;
    private final WorkReportModelGenerator modelGenerator;
    private final AppProperties appProperties;

    public Starter(WorkReportExcelGenerator reportGenerator, WorkReportModelGenerator modelGenerator,
            AppProperties appProperties) {
        this.reportGenerator = reportGenerator;
        this.modelGenerator = modelGenerator;
        this.appProperties = appProperties;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Starter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("***** STARTING UP ********");
        log.info("Owner {}, path to generate {}, when {}", appProperties.getOwner(), appProperties.getPathToGenerate(),
                new Date());
        reportGenerator.generate(modelGenerator.buildModel(), appProperties.getOwner(),
                appProperties.getPathToGenerate());
    }

}
