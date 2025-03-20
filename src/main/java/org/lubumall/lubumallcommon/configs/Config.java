package org.lubumall.lubumallcommon.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("org.lubumall.lubumallcommon")
public class Config {
    private static Environment environment;

    @Autowired
    public void setEnvironment(Environment env) {
        environment = env;
    }

    public static String getEnvironmentProperty(String key) {
        return environment.getProperty(key);
    }
}