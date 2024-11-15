package com.rejkom.ams.manager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.rejkom.ams.executor.SSHExecutorInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class contains methods related to checking logs.
 */
public class LogManager {

    private final SSHExecutorInterface sshExecutor;

    public LogManager(SSHExecutorInterface sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public boolean checkLogContains(String configName, String searchLine) throws IOException, JSchException {
        String command = "cat $CONFIG_DIR/" + configName + "/logs/application.log | grep \"" + searchLine + "\"";
        return !sshExecutor.sendCommand(command).isEmpty();
    }

}
