package com.rejkom.ams.executor;

import com.rejkom.ams.manager.SshSessionManager;

public class SshCommandExecutorFactory {

    public static SshCommandExecutor getExecutor(SshSessionManager sessionManager) {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return new SshCommandExecutorOnWindows(sessionManager);
        } else {
            return new SshCommandExecutorOnLinux(sessionManager);
        }
    }

}
