package com.rejkom.ams.manager;

import com.jcraft.jsch.JSchException;
import com.rejkom.ams.executor.SshCommandExecutor;

import java.io.IOException;

/**
 * This class contains methods related to checking logs.
 */
public class LogManager {

    private final SshCommandExecutor sshExecutor;

    public LogManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public boolean checkLogContains(String configName, String searchLine) throws IOException, JSchException {
        String command = "cat $CONFIG_DIR/" + configName + "/logs/application.log | grep \"" + searchLine + "\"";
        return !sshExecutor.executeCommand(command).isEmpty();
    }

}
