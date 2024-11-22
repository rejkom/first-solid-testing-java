package com.rejkom.ams.manager.windows;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.WebContainerManager;

public class WindowsWebContainerManager implements WebContainerManager {

    private final SshCommandExecutor sshExecutor;

    public WindowsWebContainerManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void startWebContainer() {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void stopWebContainer() {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

}
