package com.rejkom.ams.manager;

import com.rejkom.ams.executor.SSHExecutorInterface;

/**
 * This class handles Orchestrator application related operations.
 */
public class OrchestratorManager {

    private final SSHExecutorInterface sshExecutor;

    public OrchestratorManager(SSHExecutorInterface sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public void startOrchestrator() {
        sshExecutor.sendCommand(". .bashrc ; sudo $HOME/startOrchestrator " +
                "start < /dev/null >& /dev/null & sleep 10 \n");
    }

    public void stopOrchestrator() {
        sshExecutor.sendCommand(". .bashrc ; $HOME/stopOrchestrator " +
                "stop < /dev/null >& /dev/null & sleep 10");
    }

}
