package com.rejkom.ams.manager;

import com.rejkom.ams.PlatformDetector;
import com.rejkom.ams.executor.SshCommandExecutor;
import com.rejkom.ams.manager.linux.*;
import com.rejkom.ams.manager.windows.*;

import java.util.Map;

public class PlatformManagerFactory {

    private final SshCommandExecutor commandExecutor;
    private final PlatformDetector platformDetector;
    private Map<String, Object> providers;

    public PlatformManagerFactory(SshCommandExecutor commandExecutor, PlatformDetector platformDetector) {
        this.commandExecutor = commandExecutor;
        this.platformDetector = platformDetector;
    }

    public ConfigurationManager getConfigurationManager() {
        providers = Map.of(
                "windows", new WindowsConfigurationManager(commandExecutor),
                "linux", new LinuxConfigurationManager(commandExecutor));
        return (ConfigurationManager) providers.get(platformDetector.getPlatformName());
    }

    public DatabaseManager getDatabaseManager() {
        providers = Map.of(
                "windows", new WindowsDatabaseManager(commandExecutor),
                "linux", new LinuxDatabaseManager(commandExecutor));
        return (DatabaseManager) providers.get(platformDetector.getPlatformName());
    }

    public FileManager getFileManager() {
        providers = Map.of(
                "windows", new WindowsFileManager(commandExecutor),
                "linux", new LinuxFileManager(commandExecutor));
        return (FileManager) providers.get(platformDetector.getPlatformName());
    }

    public LogManager getLogManager() {
        providers = Map.of(
                "windows", new WindowsLogManager(commandExecutor),
                "linux", new LinuxLogManager(commandExecutor));
        return (LogManager) providers.get(platformDetector.getPlatformName());
    }

    public OrchestratorManager getOrchestratorManager() {
        providers = Map.of(
                "windows", new WindowsOrchestratorManager(commandExecutor),
                "linux", new LinuxOrchestratorManager(commandExecutor));
        return (OrchestratorManager) providers.get(platformDetector.getPlatformName());
    }

    public ServiceManager getServiceManager() {
        providers = Map.of(
                "windows", new WindowsServiceManager(commandExecutor),
                "linux", new LinuxServiceManager(commandExecutor));
        return (ServiceManager) providers.get(platformDetector.getPlatformName());
    }

    public WebContainerManager getWebContainerManager() {
        providers = Map.of(
                "windows", new WindowsWebContainerManager(commandExecutor),
                "linux", new LinuxWebContainerManager(commandExecutor));
        return (WebContainerManager) providers.get(platformDetector.getPlatformName());
    }

}
