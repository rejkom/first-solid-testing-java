package com.rejkom.ams.executor;

import com.rejkom.ams.manager.SshSessionManager;

public class SshCommandExecutorOnWindows implements SshCommandExecutor {

    private final SshSessionManager session;

    public SshCommandExecutorOnWindows(SshSessionManager sessionManager) {
        this.session = sessionManager;

    }
    @Override
    public String executeCommand(String command) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
        return "";
    }

    @Override
    public void close() {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

}
