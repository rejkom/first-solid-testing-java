package com.rejkom.ams.manager.linux;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.DatabaseManager;

public class LinuxDatabaseManager implements DatabaseManager {

    private final SshCommandExecutor sshExecutor;

    public LinuxDatabaseManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void startDatabase() {
        sshExecutor.executeCommand("startdatabase < /dev/null >& /dev/null \n sleep 10 ");
    }

    @Override
    public void stopDatabase() {
        sshExecutor.executeCommand("stopdatabase < /dev/null >& /dev/null \n sleep 10 ");
    }

    @Override
    public void operateOnDbValues(String configName) {
        String command = String.format(
                ". .bashrc \n cd %s%s \n sleep 2 \n sed -e 's/DB_HOST=.*/DB_HOST=env/g' " +
                        "database.properties > dp.temp && mv dp.temp database.properties \n sleep 5",
                "$AMS_CONFIG_DIR", configName
        );
        sshExecutor.executeCommand(command);
    }

}
