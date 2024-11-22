package com.rejkom.ams.manager.linux;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.WebContainerManager;

public class LinuxWebContainerManager implements WebContainerManager {

    private final SshCommandExecutor sshExecutor;

    public LinuxWebContainerManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void startWebContainer() {
        sshExecutor.executeCommand("startapplication < /dev/null >& /dev/null \n sleep 10 ");
    }

    @Override
    public void stopWebContainer() {
        sshExecutor.executeCommand("stopapplication < /dev/null >& /dev/null \n sleep 10 ");
    }

}
