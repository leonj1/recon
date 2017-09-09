# recon
Parse properties from properties file and support overrides from command line. Similar to Spring Boot
Example application.properties
```properties
app.db.name=testdb
app.db.user=sa
app.db.pass=
app.db.host=mem

spring.datasource.url=jdbc:h2://${app.db.host}/${app.db.name}
spring.datasource.username=${app.db.user}
spring.datasource.password=${app.db.pass}
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.max-active=2

feature.x.enable=true
```
# Example of your custom AppPropertiesReader
```java
public class TestGetProperty {

    private AppProperty getProperty;

    public TestGetProperty(AppProperty getProperty) {
        this.getProperty = getProperty;
    }

    public String getDatabaseName() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("app.db.name");
    }

    public Boolean getFeatureXEnabled() throws PropertiesFileNotFoundException, IOException {
        return Boolean.parseBoolean(this.getProperty.value("feature.x.enable"));
    }

    public Integer getDatabaseMaxActiveConnections() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("spring.datasource.max-active"));
    }
}
```
# Then you could use it like this
```java
public class App {
    private Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String APPLICATION_PROPERTIES = "application.properties";

    public static void main(String[] args) {
    
        // Load properties from application.properties and override with command line arguments
        TestGetProperty appProperties = null;
        Parser cmdlineParser = new CommandlineParser(args);
        try {
            appProperties = new TestGetProperty(
                    new GetEffectiveProperty(
                            new GetProperty(
                                    APPLICATION_PROPERTIES,
                                    cmdlineParser
                            ),
                            cmdlineParser
                    )
            );
        } catch (Exception e) {
            log.error(String.format("Problem trying to find resource %s", APPLICATION_PROPERTIES));
            System.exit(1);
        }

        // Now we can start getting the values
        System.out.println("Max active connections value: " + appProperties.getDatabaseMaxActiveConnections());
    }
}
```
