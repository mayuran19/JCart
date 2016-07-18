package com.mayuran19.jcart.core.environment;

import com.mayuran19.jcart.core.constant.Constants;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.*;
import org.springframework.core.io.support.ResourcePropertySource;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayuran on 16/7/16.
 */
public class EncryptedPasswordPropertiesPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Map<String, Object> encryptedProperties = new HashMap<>();
        ConfigurableEnvironment env = (ConfigurableEnvironment) configurableListableBeanFactory.getBean(Environment.class);
        MutablePropertySources propertySources = env.getPropertySources();
        propertySources.forEach((propertySource -> {
            if (propertySource instanceof ResourcePropertySource) {
                String[] keys = ((EnumerablePropertySource) propertySource).getPropertyNames();
                for (String key : keys) {
                    if (propertySource.getProperty(key).toString().startsWith(Constants.Encryption.ENCRYPTION_PREFIX) &&
                            propertySource.getProperty(key).toString().endsWith(Constants.Encryption.ENCRYPTION_POSTFIX)) {
                        String encryptedValue = propertySource.getProperty(key).toString();
                        String encryptedStripped = encryptedValue.substring(4, encryptedValue.length() - 1);
                        encryptedProperties.put(key, getStandardPBEStringEncryptor(env).decrypt(encryptedStripped));
                    }
                }
            }
        }));

        propertySources.addFirst(new MapPropertySource("decryptedValues", encryptedProperties));
    }

    private StandardPBEStringEncryptor getStandardPBEStringEncryptor(Environment environment) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(environment.getProperty(Constants.Encryption.KEY_ALGORITHM));
        encryptor.setPassword(new String(Base64.getDecoder().decode(environment.getProperty(Constants.Encryption.KEY_PASSKEY))));

        return encryptor;
    }
}
