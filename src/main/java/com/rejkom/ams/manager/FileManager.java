package com.rejkom.ams.manager;

public interface FileManager {

    void clearData();

    void copyTestData(String configName);

    void copyMsgTestData2Export(String configName, String msgName, String importPath);

    void copySqlStatement(String sqlStatement);

    void jschReadAndSaveFile(String configNameFilePath, String saveFilePath);

    boolean compareFileResults(String expectedConfig, String receivedConfig, String expectedPath, String receivedPath);

    // Renamed initial grepFile method as specific only for Linux environment
    boolean searchFile(String filePatch, String fileName, String grepCommand);

}
