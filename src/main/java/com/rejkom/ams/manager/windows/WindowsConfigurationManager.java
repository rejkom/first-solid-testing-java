package com.rejkom.ams.manager.windows;

import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.ConfigurationManager;

public class WindowsConfigurationManager implements ConfigurationManager {

    private final SshCommandExecutor sshExecutor;

    public WindowsConfigurationManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    @Override
    public void setProperties(String[][] properties) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void addNewProperties(String[][] properties) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void configureApplication(String configName) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void removeEOL(String[][] properties) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

    @Override
    public void setJMSProperties(String[][] properties) {
        //TODO: Implement Windows-specific command execution logic based on the new project requirements
    }

}
