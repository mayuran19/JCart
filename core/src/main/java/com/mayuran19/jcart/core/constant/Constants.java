package com.mayuran19.jcart.core.constant;

/**
 * Created by mayuran on 16/7/16.
 */
public class Constants {
    public class DataSource{
        public final static String KEY_JDBC_URL = "jdbc.url";
        public final static String KEY_JDBC_DRIVER_CLASS_NAME = "jdbc.DriverClassName";
        public final static String KEY_JDBC_USERNAME = "JDBC.USERNAME";
        public final static String KEY_JDBC_PASSWORD = "JDBC.PASSWORD";
        public final static String BATCH_TABLE_PREFIX = "BATCH_";
    }

    public class Encryption{
        public final static String KEY_ALGORITHM = "encryption.algorithm";
        public final static String KEY_PASSKEY = "encryption.key";
        public final static String ENCRYPTION_PREFIX = "ENC(";
        public final static String ENCRYPTION_POSTFIX = ")";
    }
}
