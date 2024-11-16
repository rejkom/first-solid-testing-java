package com.rejkom.ams.executor;

import com.jcraft.jsch.*;
import com.rejkom.ams.manager.SshSessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class SshCommandExecutor {

    private static final Logger LOGGER = Logger.getLogger(SshCommandExecutor.class.getName());
    private final SshSessionManager session;

    public SshCommandExecutor(SshSessionManager sessionManager) {
        this.session = sessionManager;
    }

    public String sendCommand(String command) {
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = session.getSession().openChannel("exec");
            ((ChannelExec) channel).setCommand(". .bashrc \n" + command);
            channel.connect();
            InputStream commandOutput = channel.getInputStream();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }
            channel.disconnect();
        } catch (IOException | JSchException ex) {
            LOGGER.severe("Error: " + ex.getMessage());
            return null;
        }
        return outputBuffer.toString();
    }

    public void close() {
        session.getSession().disconnect();
    }

    public SshSessionManager getSessionManager() {
        return session;
    }
}