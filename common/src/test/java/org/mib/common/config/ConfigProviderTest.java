package org.mib.common.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigProviderTest {

    @Test
    public void testConfig() {
        assertEquals(Integer.valueOf(1), ConfigProvider.getInt("int"));
        assertEquals(true, ConfigProvider.getBoolean("bool"));
        assertEquals(Double.valueOf(0.7), ConfigProvider.getDouble("double"));
        assertEquals("mib", ConfigProvider.get("string"));
        assertEquals(Long.valueOf(1), ConfigProvider.getLong("long"));
        assertEquals(Character.valueOf('a'), ConfigProvider.getChar("char"));
        assertEquals(Short.valueOf((short) 1), ConfigProvider.getShort("short"));
        assertEquals("value", ConfigProvider.get("env1"));
        assertEquals("value:haha", ConfigProvider.get("env2"));
        assertEquals("jdbc:mysql://localhost:3306/db", ConfigProvider.get("jdbc"));
        assertEquals("value3{}", ConfigProvider.get("exp"));
    }
}
