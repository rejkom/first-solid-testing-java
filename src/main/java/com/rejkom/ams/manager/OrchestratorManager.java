package com.rejkom.ams.manager;

import com.rejkom.ams.executor.SshCommandExecutor;

/**
 * This class handles Orchestrator application related operations.
 */
public class OrchestratorManager {

    private final SshCommandExecutor sshExecutor;

    public OrchestratorManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public void startOrchestrator() {
        sshExecutor.executeCommand(". .bashrc ; sudo $HOME/startOrchestrator " +
                "start < /dev/null >& /dev/null & sleep 10 \n");
    }

    public void stopOrchestrator() {
        sshExecutor.executeCommand(". .bashrc ; $HOME/stopOrchestrator " +
                "stop < /dev/null >& /dev/null & sleep 10");
    }

}
