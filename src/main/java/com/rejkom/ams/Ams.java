package com.rejkom.ams;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.rejkom.ams.executor.SSHExecutorInterface;
import com.rejkom.ams.manager.SshSessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * This class handles the main AMS project application related operations.
 */
public class Ams {

    private static final Logger LOGGER = Logger.getLogger(Ams.class.getName());

    private final SSHExecutorInterface sshExecutor;

    public Ams(String user, String ipAddress) {
        SshSessionManager sessionManager = new SshSessionManager(user, ipAddress);
        sessionManager.connectSession();
        sshExecutor = new SSHExecutorInterface(sessionManager);
    }

    public boolean sendCommand(String command) {
        sshExecutor.sendCommand(command);
        return false;
    }

    public void close() {
        sshExecutor.close();
    }

    // Moved from SSHExecutorInterface class
    public String startAms(String configName) {

        String command = ". .bashrc \n ams start $AMS_CONFIG_DIR/" + configName;
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = sshExecutor.getSessionManager().getSession().openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
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

    // Moved from SSHExecutorInterface class
    public String stopAms(String configName) {

        String command = ". .bashrc \n ams stop $AMS_CONFIG_DIR/" + configName;
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = sshExecutor.getSessionManager().connectSession().openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
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

    public SSHExecutorInterface getSshExecutor() {
        return sshExecutor;
    }

}