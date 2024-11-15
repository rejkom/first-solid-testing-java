package com.rejkom.ams;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.rejkom.ams.executor.SSHExecutorInterface;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class handles the main AMS project application related operations.
 */
public class Ams {

    private final SSHExecutorInterface sshExecutor;

    public Ams(String user, String ipAddress) {
        sshExecutor = new SSHExecutorInterface(user, ipAddress);
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
            Channel channel = sshExecutor.sesConnection.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.connect();
            InputStream commandOutput = channel.getInputStream();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }
            channel.disconnect();
        } catch (IOException ioX) {
            sshExecutor.logWarning(ioX.getMessage());
            return null;
        } catch (JSchException jschX) {
            sshExecutor.logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    // Moved from SSHExecutorInterface class
    public String stopAms(String configName) {

        String command = ". .bashrc \n ams stop $AMS_CONFIG_DIR/" + configName;
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = sshExecutor.sesConnection.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.connect();
            InputStream commandOutput = channel.getInputStream();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }
            channel.disconnect();
        } catch (IOException ioX) {
            sshExecutor.logWarning(ioX.getMessage());
            return null;
        } catch (JSchException jschX) {
            sshExecutor.logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    public SSHExecutorInterface getSshExecutor() {
        return sshExecutor;
    }

}