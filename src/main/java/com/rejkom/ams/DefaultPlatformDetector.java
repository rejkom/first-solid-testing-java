package com.rejkom.ams;

public class DefaultPlatformDetector implements PlatformDetector {

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    @Override
    public String getPlatformName() {
        if (OS_NAME.contains("win")) {
            return "windows";
        } else if (OS_NAME.contains("nix") || OS_NAME.contains("nux")) {
            return "linux";
        } else if (OS_NAME.contains("mac")) {
            return "mac";
        }
        throw new UnsupportedOperationException("Unknown platform " + OS_NAME);
    }

}
