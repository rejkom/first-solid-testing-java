package com.rejkom.ams.manager.windows;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.ServiceManager;

public class WindowsServiceManager implements ServiceManager {

    private final SshCommandExecutor sshExecutor;

    public WindowsServiceManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void startService(String configName) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void restartService(String configName) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void stopService(String configName) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }
}
