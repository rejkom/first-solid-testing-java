package com.rejkom.ams.manager.windows;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.OrchestratorManager;

public class WindowsOrchestratorManager implements OrchestratorManager {

    private final SshCommandExecutor sshExecutor;

    public WindowsOrchestratorManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void startOrchestrator() {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void stopOrchestrator() {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

}
