package com.rejkom.ams.manager;

public interface DatabaseManager {

    void startDatabase();

    void stopDatabase();

    void operateOnDbValues(String configName);

}
