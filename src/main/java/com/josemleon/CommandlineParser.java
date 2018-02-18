package com.josemleon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses command line arguments when the application is launched.
 *
 * Created by Jose M Leon 2017
 **/
public class CommandlineParser implements Parser {
    private Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Pattern propertiesWithEqualsPattern = Pattern.compile("-{1,2}(.*?)=(.*)");
    private Pattern propertiesWithoutEqualsPattern = Pattern.compile("-{1,2}(.*?)");
    private Matcher propertiesWithEqualsMatcher;
    private Matcher propertiesWithoutEqualsMatcher;

    private String args[];
    private Map<String, String> map;
    private boolean parsed;
    private Stack<String> stack;

    /**
     * ctor
     * @param args - should be the command line arguments passed to your application
     */
    public CommandlineParser(String[] args) {
        this.args = args;
        this.map = new ConcurrentHashMap<>();
        this.stack = new Stack<String>();
    }

    /**
     * Returns a property from the command line arguments
     * @param property - the property name
     * @return - the value of the property
     */
    @Override
    public Property property(String property) {
        if (!this.parsed) {
            parse();
        }

        return new CommandlineProperty(
                this.map.get(property),
                this.map.containsKey(property)
        );
    }

    /**
     * Recursively find nested args that were passed via command line
     * @param matcher   - the regex pattern we're searching for
     * @param map       - map of key/value pairs or passed args
     * @param matched   - tracking if we found anything
     * @return          - whether we found any args as a key/value pair
     */
    private boolean findNestedArgs(Matcher matcher, Map<String, String> map, boolean matched) {
        if (matcher.find()) {
            matched = true;
            map.put(
                    matcher.group(1),
                    matcher.group(2)
            );
        } else {
            return matched;
        }
        return findNestedArgs(matcher, map, matched);
    }

    /**
     * Does the heavy lifting of parsing the command line arguments
     */
    private void parse() {
        if (this.parsed || this.args == null) {
            return;
        }

        Arrays.stream(args).forEach(arg -> {
            boolean matched = false;
            propertiesWithEqualsMatcher = propertiesWithEqualsPattern.matcher(arg);
            if (findNestedArgs(propertiesWithEqualsMatcher, this.map, matched)) {
                return;
            }

            propertiesWithoutEqualsMatcher = propertiesWithoutEqualsPattern.matcher(arg);
            while (propertiesWithoutEqualsMatcher.find()) {
                this.stack.push(propertiesWithEqualsMatcher.group(1));
            }

            // This far, could be a value to a property, only if stack is not empty
            // If stack is empty, then its application argument, that's not a property
            if (!this.stack.empty()) {
                this.map.put(
                        this.stack.pop(),
                        arg
                );
            } else {
                log.debug(String.format("Found argument %s not associated with property.", arg));
            }
        });

        if (!this.stack.empty()) {
            log.debug("There are command line properties without values defined.");
            while (!this.stack.empty()) {
                this.map.put(this.stack.pop(), null);
            }
        }

        this.parsed = true;
    }
}
