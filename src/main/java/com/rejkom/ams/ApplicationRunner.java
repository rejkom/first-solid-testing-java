package com.rejkom.ams;

import com.jcraft.jsch.JSchException;
import com.rejkom.ams.manager.ConfigurationManager;
import com.rejkom.ams.manager.DatabaseManager;
import com.rejkom.ams.manager.LogManager;
import com.rejkom.ams.manager.WebContainerManager;

import java.io.IOException;

public class ApplicationRunner {

    private static final String CONFIG_NAME = "development";

    public static void main(String[] args) throws JSchException, IOException {
        Ams ams = new Ams("user", "127.0.0.1");

        ConfigurationManager config = new ConfigurationManager(ams.getSshExecutor());
        config.configureApplication(CONFIG_NAME);

        DatabaseManager database = new DatabaseManager(ams.getSshExecutor());
        database.operateOnDbValues(CONFIG_NAME);
        database.startDatabase();

        WebContainerManager container = new WebContainerManager(ams.getSshExecutor());
        container.startWebContainer();

        LogManager configLogs = new LogManager(ams.getSshExecutor());

        ams.startAms(CONFIG_NAME);
        configLogs.checkLogContains(CONFIG_NAME, "successfully started");


        ams.stopAms(CONFIG_NAME);
        database.stopDatabase();
        container.stopWebContainer();
    }

}
