package com.josemleon;

/**
 * Data Type Object (DTO) that holds the value of property if found in command line arguments.
 * Includes "exists()", to follow the return pattern seen in Go-lang's functions.
 *
 * Created by Jose M Leon 2017
 **/
public class CommandlineProperty implements Property {

    private String value;
    private boolean exists;

    public CommandlineProperty(String value, boolean exists) {
        this.value = value;
        this.exists = exists;
    }

    /**
     * @return - whether the property was found
     */
    @Override
    public boolean exists() {
        return this.exists;
    }

    /**
     * @return - the value of the property when found
     */
    @Override
    public String value() {
        return this.value;
    }
}
