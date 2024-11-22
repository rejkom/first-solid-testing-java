package com.rejkom.ams.manager;

public interface ConfigurationManager {

    void setProperties(String[][] properties);

    void addNewProperties(String[][] properties);

    void configureApplication(String configName);

    void removeEOL(String[][] properties);

    void setJMSProperties(String[][] properties);

}
