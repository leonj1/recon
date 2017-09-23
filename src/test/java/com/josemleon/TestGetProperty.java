package com.josemleon;

import com.josemleon.exceptions.PropertiesFileNotFoundException;

import java.io.IOException;

/**
 * Created by Jose M Leon 2017
 **/
public class TestGetProperty {

    private AppProperty getProperty;

    public TestGetProperty(AppProperty getProperty) {
        this.getProperty = getProperty;
    }

    public String getDatabaseName() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("app.db.name");
    }

    public String getDatabasePassword() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("app.db.pass");
    }

    public Boolean getFeatureXEnabled() throws PropertiesFileNotFoundException, IOException {
        return Boolean.parseBoolean(this.getProperty.value("feature.x.enable"));
    }

    public Integer getDatabaseMaxActiveConnections() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("spring.datasource.max-active"));
    }

    public String getDataSourceUrl() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("spring.datasource.url");
    }

    public String getEmailFrom() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("email.from");
    }
}
