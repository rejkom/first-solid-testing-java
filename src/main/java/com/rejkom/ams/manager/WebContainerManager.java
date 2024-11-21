package com.rejkom.ams.manager;

import com.rejkom.ams.executor.SshCommandExecutor;

/**
 * This class handles web container application related operations.
 */
public class WebContainerManager {

    private final SshCommandExecutor sshExecutor;

    public WebContainerManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public void startWebContainer() {
        sshExecutor.executeCommand("startapplication < /dev/null >& /dev/null \n sleep 10 ");
    }

    public void stopWebContainer() {
        sshExecutor.executeCommand("stopapplication < /dev/null >& /dev/null \n sleep 10 ");
    }

}
