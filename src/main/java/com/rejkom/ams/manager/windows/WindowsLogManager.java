package com.rejkom.ams.manager.windows;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.LogManager;

public class WindowsLogManager implements LogManager {

    private final SshCommandExecutor sshExecutor;

    public WindowsLogManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public boolean checkLogContains(String configName, String searchLine) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
        return false;
    }

}
