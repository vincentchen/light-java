/*
 * Copyright (c) 2016 Network New Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.networknt.config;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 23/09/16.
 */
public class ConfigClassPathTest extends TestCase {

    Config config = null;
    String homeDir = System.getProperty("user.home");

    public void setUp() throws Exception {
        super.setUp();

        config = Config.getInstance();

        // write a config file into the user home directory.
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value", "classpath");
        config.getMapper().writeValue(new File(homeDir + "/test.json"), map);

        // Add home directory to the classpath of the system class loader.
        addURL(new File(homeDir).toURL());
    }

    public void tearDown() throws Exception {
        // Remove the test.json from home directory
        File test = new File(homeDir + "/test.json");
        test.delete();
    }

    @Test
    public void testGetConfigFromClassPath() throws Exception {
        config.clear();
        Map<String, Object> configMap = config.getJsonMapConfig("test");
        Assert.assertEquals("classpath", configMap.get("value"));
    }

    public void addURL(URL url) throws Exception {
        URLClassLoader classLoader
                = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class clazz= URLClassLoader.class;

        // Use reflection
        Method method= clazz.getDeclaredMethod("addURL", new Class[] { URL.class });
        method.setAccessible(true);
        method.invoke(classLoader, new Object[]{url});
    }
}
