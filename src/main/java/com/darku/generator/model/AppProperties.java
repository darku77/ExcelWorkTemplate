// =============================================================================
// Copyright/Security Notice
//
// Licensed Materials - Property of Qnamic AG
// (C) Copyright Qnamic AG 2003-2022
// All rights reserved
//
// End Copyright
// =============================================================================
package com.darku.generator.model;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Catalin on 01.09.2022
 */
@ConfigurationProperties(prefix = "excel-gen-prop")
@Getter
@Setter
@ToString
public class AppProperties {

    private String owner;
    private Path pathToGenerate;
}
