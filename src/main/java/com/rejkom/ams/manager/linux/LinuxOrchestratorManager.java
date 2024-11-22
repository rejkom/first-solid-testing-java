package com.rejkom.ams.manager.linux;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.OrchestratorManager;

public class LinuxOrchestratorManager implements OrchestratorManager {

    private final SshCommandExecutor sshExecutor;

    public LinuxOrchestratorManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void startOrchestrator() {
        sshExecutor.executeCommand(". .bashrc ; sudo $HOME/startOrchestrator " +
                "start < /dev/null >& /dev/null & sleep 10 \n");
    }

    @Override
    public void stopOrchestrator() {
        sshExecutor.executeCommand(". .bashrc ; $HOME/stopOrchestrator " +
                "stop < /dev/null >& /dev/null & sleep 10");
    }

}
