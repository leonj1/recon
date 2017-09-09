package com.josemleon;

import com.josemleon.exceptions.PropertiesFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Since we support nest properties,
 * and may need to recursively search for more properties,
 * this class performs the recursive calls.
 *
 * Created by Jose M Leon 2017
 **/
public class GetEffectiveProperty implements AppProperty {
    private Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AppProperty appProperty;
    private Parser parser;

    public GetEffectiveProperty(AppProperty appProperty, Parser parser) {
        this.appProperty = appProperty;
        this.parser = parser;
    }

    /**
     * Allow command line properties to override those in application.properties
     * @param property - which is being searched
     * @return - the value found
     */
    @Override
    public String value(String property) throws PropertiesFileNotFoundException, IOException {
        Property p = this.parser.property(property);
        if (p.exists()) {
            log.debug(String.format("GetEffectiveProperty: Returning cmdline value %s for property %s", p.value(), property));
            return p.value();
        }
        log.debug(
                String.format(
                        "GetEffectiveProperty: Returning default value %s for property %s",
                        this.appProperty.value(property),
                        property
                )
        );
        return this.appProperty.value(property);
    }
}
