package com.rejkom.ams.executor;

public interface SshCommandExecutor {

    String executeCommand(String command);

    void close();

}
