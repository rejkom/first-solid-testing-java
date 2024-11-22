package com.rejkom.ams.manager;

public interface ServiceManager {

    void startService(String configName);

    void restartService(String configName);

    void stopService(String configName);

}
