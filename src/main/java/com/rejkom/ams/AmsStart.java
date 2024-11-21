package com.rejkom.ams;

import com.rejkom.ams.executor.CommandExecutor;
import com.rejkom.ams.executor.SshCommandExecutor;

/**
 * Executes commands to start the AMS application service remotely.
 */
public class AmsStart implements CommandExecutor {

    private final SshCommandExecutor sshExecutor;

    public AmsStart(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public String execute(String configName) {
        String command = "ams start $AMS_CONFIG_DIR/" + configName;
        return sshExecutor.executeCommand(command);
    }

}
