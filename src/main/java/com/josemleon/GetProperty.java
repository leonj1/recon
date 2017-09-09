package com.josemleon;

import com.josemleon.exceptions.PropertiesFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jose M Leon 2017
 **/
public class GetProperty implements AppProperty {
    private Properties properties;
    private Parser parser;

    public GetProperty(Properties properties) {
        this.properties = properties;
    }

    public GetProperty(String sConfigFile, Parser parser) throws PropertiesFileNotFoundException, IOException {
        this(new Properties());
        this.parser = parser;

        InputStream in = getClass().getClassLoader().getResourceAsStream(sConfigFile);
        if (in == null) {
            throw new PropertiesFileNotFoundException("Properties file not found");
        }
        this.properties.load(in);
    }

    public String value(String property) {
        Property p = this.parser.property(property);
        if (p.exists()) {
            return p.value();
        }

        String result = this.properties.getProperty(property);
        String patternOfProperty = "(\\$\\{(.*?)\\})";
        Pattern pattern = Pattern.compile(patternOfProperty);
        Matcher matcher = pattern.matcher(result);

        while (matcher.find()) {
            for (int i = 2; i <= matcher.groupCount(); i++) {
                result = replaceGroup(
                        patternOfProperty,
                        result,
                        1,
                        value(matcher.group(i))
                );
            }
        }
        return result;
    }

    private String replaceGroup(String regex, String source, int groupToReplace, String replacement) {
        return replaceGroup(
                regex,
                source,
                groupToReplace,
                1,
                replacement
        );
    }

    private String replaceGroup(String regex, String source, int groupToReplace, int groupOccurrence, String replacement) {
        Matcher m = Pattern.compile(regex).matcher(source);
        for (int i = 0; i < groupOccurrence; i++) {
            if (!m.find()) {
                return source; // pattern not met, may also throw an exception here
            }
        }

        return new StringBuilder(source)
                .replace(
                        m.start(groupToReplace),
                        m.end(groupToReplace),
                        replacement
                )
                .toString();
    }
}
