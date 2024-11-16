package com.rejkom.ams;

import com.rejkom.ams.executor.CommandExecutor;
import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.SshSessionManager;

/**
 * Provides methods to control AMS application services remotely, utilizing SSH for command execution.
 */
public class Ams {

    private final SshCommandExecutor sshExecutor;

    public Ams(String user, String ipAddress) {
        SshSessionManager sessionManager = new SshSessionManager(user, ipAddress);
        sessionManager.connectSession();
        sshExecutor = new SshCommandExecutor(sessionManager);
    }

    public String executeCommand(CommandExecutor commandExecutor, String configName) {
        return commandExecutor.execute(configName);
    }

    public void close() {
        sshExecutor.close();
    }

    public SshCommandExecutor getSshExecutor() {
        return sshExecutor;
    }

}