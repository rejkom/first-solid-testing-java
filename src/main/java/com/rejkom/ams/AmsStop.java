package com.rejkom.ams;

import com.rejkom.ams.executor.CommandExecutor;
import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.executor.SshCommandExecutorOnLinux;

/**
 * Executes commands to stop the AMS application service remotely.
 */
public class AmsStop implements CommandExecutor {

    private final SshCommandExecutor sshExecutor;

    public AmsStop(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public String execute(String configName) {
        String command = ". .bashrc \n ams stop $AMS_CONFIG_DIR/" + configName;
        return sshExecutor.executeCommand(command);
    }

}
