package com.zhaisoft.lib.updater.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    public static Properties getProperty() throws Exception {
//        InputStream inputStream = PropertyUtil.class.getClassLoader()
//                .getResourceAsStream("config.properties");

        //android studio
        InputStream inputStream = PropertyUtil.class.getResourceAsStream("config.properties");

        Properties p = new Properties();
        p.load(inputStream);
        return p;
    }

    public static Properties getPropertyFromSrcString(String propertyFile)
            throws Exception {
//		InputStream inputStream = PropertyUtil.class.getClassLoader()
//				.getResourceAsStream(propertyFile);


        //android studio
        InputStream inputStream = PropertyUtil.class.getResourceAsStream(propertyFile);


        Properties p = new Properties();
        p.load(inputStream);
        return p;
    }

    public static Properties getPropertyFromInputStream(InputStream inputStream)
            throws Exception {
        /*
		 * InputStream inputStream = PropertyUtil.class.getClassLoader()
		 * .getResourceAsStream("config.properties");
		 */
        Properties p = new Properties();
        p.load(inputStream);
        return p;
    }
}
