package com.rejkom.ams.manager.linux;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.ServiceManager;

public class LinuxServiceManager implements ServiceManager {

    private final SshCommandExecutor sshExecutor;

    public LinuxServiceManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void startService(String configName) {
        sshExecutor.executeCommand(". .bashrc \n SERVICE start $CONFIG_DIR/" + configName + "\n" +
                "sleep 10");
    }

    @Override
    public void restartService(String configName) {
        sshExecutor.executeCommand(". .bashrc \n SERVICE restart $CONFIG_DIR/" + configName + "\n" +
                "sleep 15");
    }

    @Override
    public void stopService(String configName) {
        sshExecutor.executeCommand(". .bashrc \n SERVICE stop $CONFIG_DIR/" + configName + "\n");
    }

}
