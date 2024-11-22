package com.rejkom.ams.manager.linux;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.LogManager;

public class LinuxLogManager implements LogManager {

    private final SshCommandExecutor sshExecutor;

    public LinuxLogManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public boolean checkLogContains(String configName, String searchLine) {
        String command = "cat $CONFIG_DIR/" + configName + "/logs/application.log | grep \"" + searchLine + "\"";
        return !sshExecutor.executeCommand(command).isEmpty();
    }

}
