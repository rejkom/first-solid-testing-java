package com.rejkom.ams.manager;

import com.rejkom.ams.executor.SshCommandExecutor;

/**
 * This class handles database related operations.
 */
public class DatabaseManager {

    private final SshCommandExecutor sshExecutor;

    public DatabaseManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public void startDatabase() {
        sshExecutor.sendCommand("startdatabase < /dev/null >& /dev/null \n sleep 10 ");
    }

    public void stopDatabase() {
        sshExecutor.sendCommand("stopdatabase < /dev/null >& /dev/null \n sleep 10 ");
    }

    public void operateOnDbValues(String configName) {
        String command = String.format(
                ". .bashrc \n cd %s%s \n sleep 2 \n sed -e 's/DB_HOST=.*/DB_HOST=env/g' " +
                        "database.properties > dp.temp && mv dp.temp database.properties \n sleep 5",
                "$AMS_CONFIG_DIR", configName
        );
        sshExecutor.sendCommand(command);
    }

}
