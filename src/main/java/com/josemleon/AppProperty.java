package com.josemleon;

import com.josemleon.exceptions.PropertiesFileNotFoundException;

import java.io.IOException;

/**
 * Custom apps should depend this interface to read properties.
 * See TestGetProperty class as an example
 *
 * Created by Jose M Leon 2017
 **/
public interface AppProperty {
    /**
     * Fetches the value of the specified property
     * @param property - the property name
     * @return - the retrieved value. String since that is the type in command line and written to properties files.
     * @throws PropertiesFileNotFoundException - when properties file is not found; allows custom handling
     * @throws IOException - when there is a problem reading the properties file (e.g. Permissions)
     */
    String value(String property) throws PropertiesFileNotFoundException, IOException;
}
