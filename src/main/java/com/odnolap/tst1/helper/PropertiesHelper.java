package com.odnolap.tst1.helper;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PropertiesHelper {
    private static Map<Object, Object> appProperties;
    private static final String propertiesFileName = "application.properties";

    public static Object getProperty(Object property) {
        if (appProperties == null) {
            appProperties = new HashMap<>();
            try(InputStream propertiesIs = PropertiesHelper.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
                Properties properties = new Properties();
                properties.load(propertiesIs);
                appProperties.putAll(properties);
                log.info("file {} is read.", propertiesFileName);
            } catch (IOException ex) {
                log.info("Error during reading properties from {}: {} ", propertiesFileName, ex.getMessage());
            }
        }
        return appProperties.get(property);
    }
}
