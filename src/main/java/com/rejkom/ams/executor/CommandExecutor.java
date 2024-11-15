package com.rejkom.ams.executor;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class contains methods related to executing commands and returning output.
 */
public class CommandExecutor {

    private final SSHExecutorInterface sshExecutor;

    public CommandExecutor(SSHExecutorInterface sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public String executeCommandAndReturnOutput(String command) {
        return sshExecutor.sendCommand(command);
    }

    public int executeCommandAndGetCount(String commandTarget, String outputPath) {

        String command = "" + commandTarget + " $configDirectory/" + outputPath + "/* | wc -l";
        String targetCount = sshExecutor.sendCommand(command);
        // Convert the retrieved count from String to Integer
        int resultCount = Integer.parseInt(targetCount.trim());
        return resultCount;
    }

    public boolean enterCommandReturnValue(String enterCommand) {
        String command = enterCommand;
        String resultOfGrep = sshExecutor.sendCommand(command);
        System.out.println("Found value= " + resultOfGrep);
        return resultOfGrep != null;
    }

    // Moved from SSHExecutorInterface class
    public int sendCommandReturnValue(String configName) {

        String command = ("sed 's/[^ ]//g' $AMS_CONFIG_DIR/" + configName +
                "/data/export/* | awk '{ print length }' |  awk '{ SUM += $1} END { print SUM+0 }'");
        String amountOfTarget = sshExecutor.sendCommand(command);

        //converting a String with spaces to an Integer
        int amountInt = Integer.parseInt(amountOfTarget.trim());
        System.out.println("Found value= " + amountInt);

        return amountInt;
    }

}
