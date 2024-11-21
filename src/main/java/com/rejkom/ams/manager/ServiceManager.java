package com.rejkom.ams.manager;

import com.rejkom.ams.executor.SshCommandExecutor;

/**
 * This class handles service related operations.
 */
public class ServiceManager {

    private final SshCommandExecutor sshExecutor;

    public ServiceManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public void startService(String configName) {
        sshExecutor.executeCommand(". .bashrc \n SERVICE start $CONFIG_DIR/" + configName + "\n" +
                "sleep 10");
    }

    public void restartService(String configName) {
        sshExecutor.executeCommand(". .bashrc \n SERVICE restart $CONFIG_DIR/" + configName + "\n" +
                "sleep 15");
    }

    public void stopService(String configName) {
        sshExecutor.executeCommand(". .bashrc \n SERVICE stop $CONFIG_DIR/" + configName + "\n");
    }

}
