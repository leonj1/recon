package com.josemleon;

import com.josemleon.exceptions.PropertiesFileNotFoundException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Recon unit tests
 */
public class AppTest {
    // Properties file intended to be in classpath
    private static final String APPLICATION_PROPERTIES = "application.properties";

    @Test
    public void verifyGetStringProperty() throws PropertiesFileNotFoundException, IOException {
        Parser cmdlineParser = new CommandlineParser(new String[0]);
        assertEquals(
                "Should have been able to fetch an explicitly set property",
                "testdb",
                new TestGetProperty(
                        new GetEffectiveProperty(
                                new GetProperty(
                                        APPLICATION_PROPERTIES,
                                        cmdlineParser
                                ),
                                cmdlineParser
                        )
                ).getDatabaseName()
        );
    }

    @Test
    public void verifyGetBlankStringProperty() throws PropertiesFileNotFoundException, IOException {
        Parser cmdlineParser = new CommandlineParser(new String[0]);
        assertEquals(
                "Should have been able to fetch a property set to empty string",
                "",
                new TestGetProperty(
                        new GetEffectiveProperty(
                                new GetProperty(
                                        APPLICATION_PROPERTIES,
                                        cmdlineParser
                                ),
                                cmdlineParser
                        )
                ).getDatabasePassword()
        );
    }

    @Test
    public void verifyGetNestedProperties() throws PropertiesFileNotFoundException, IOException {
        Parser cmdlineParser = new CommandlineParser(new String[0]);
        assertEquals(
                "Should have been able to fetch a property whose value is another nested property",
                "jdbc:h2://mem/testdb",
                new TestGetProperty(
                        new GetEffectiveProperty(
                                new GetProperty(
                                        APPLICATION_PROPERTIES,
                                        cmdlineParser
                                ),
                                cmdlineParser
                        )
                ).getDataSourceUrl()
        );
    }

    @Test
    public void verifyGetBooleanProperty() throws PropertiesFileNotFoundException, IOException {
        Parser cmdlineParser = new CommandlineParser(new String[0]);
        assertEquals(
                "Should have been able to fetch a property set to a boolean",
                true,
                new TestGetProperty(
                        new GetEffectiveProperty(
                                new GetProperty(
                                        APPLICATION_PROPERTIES,
                                        cmdlineParser
                                ),
                                cmdlineParser
                        )
                ).getFeatureXEnabled()
        );
    }
}
