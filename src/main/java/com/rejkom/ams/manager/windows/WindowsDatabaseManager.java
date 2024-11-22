package com.rejkom.ams.manager.windows;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.DatabaseManager;

public class WindowsDatabaseManager implements DatabaseManager {

    private final SshCommandExecutor sshExecutor;

    public WindowsDatabaseManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void startDatabase() {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void stopDatabase() {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void operateOnDbValues(String configName) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

}
