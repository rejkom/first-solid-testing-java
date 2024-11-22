package com.rejkom.ams;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.*;

public class ApplicationRunner {

    private static final String CONFIG_NAME = "development";

    public static void main(String[] args) {
        Ams ams = new Ams("user", "127.0.0.1");
        SshCommandExecutor commandExecutor = ams.getSshExecutor();
        PlatformManagerFactory managerFactory = new PlatformManagerFactory(commandExecutor, new DefaultPlatformDetector());

        ConfigurationManager config = managerFactory.getConfigurationManager();
        config.configureApplication(CONFIG_NAME);

        DatabaseManager database = managerFactory.getDatabaseManager();
        database.operateOnDbValues(CONFIG_NAME);
        database.startDatabase();

        WebContainerManager container = managerFactory.getWebContainerManager();
        container.startWebContainer();

        LogManager configLogs = managerFactory.getLogManager();

        AmsStart start = new AmsStart(ams.getSshExecutor());
        AmsStop stop = new AmsStop(ams.getSshExecutor());

        String startResult = ams.executeCommand(start, CONFIG_NAME);
        configLogs.checkLogContains(CONFIG_NAME, "successfully started");

        String stopResult = ams.executeCommand(stop, CONFIG_NAME);

        database.stopDatabase();
        container.stopWebContainer();
    }

}
