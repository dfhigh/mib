package org.mib.common.inet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Get host name of current host.
 */
public class HostNameProvider {

    public static String getHostName() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("hostname").getInputStream()))) {
            return br.readLine();
        } catch (Exception e) {
            return "unknown host";
        }
    }

}
