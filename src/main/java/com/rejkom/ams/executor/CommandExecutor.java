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
        StringBuilder output = new StringBuilder();
        try {
            Channel channel = sshExecutor.sesConnection.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            channel.connect();

            try (InputStream inputStream = channel.getInputStream();
                 InputStreamReader isReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(isReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    output.append(line);
                }
            }

            channel.disconnect();
        } catch (IOException | JSchException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public int executeCommandAndGetCount(String commandTarget, String outputPath)
            throws IOException, JSchException {

        String command = "" + commandTarget + " $configDirectory/" + outputPath + "/* | wc -l";
        Channel channel = sshExecutor.sesConnection.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        channel.connect();
        InputStream inputStream = channel.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String targetCount = reader.readLine();

        // Convert the retrieved count from String to Integer
        int resultCount = Integer.parseInt(targetCount.trim());
        System.out.println("Count result = " + resultCount);

        inputStream.close();
        reader.close();
        channel.disconnect();

        return resultCount;
    }

    public boolean enterCommandReturnValue(String enterCommand) throws IOException, JSchException {
        String command = enterCommand;
        Channel channel = sshExecutor.sesConnection.openChannel("exec");
        InputStream inputStream = channel.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        ((ChannelExec) channel).setCommand(command);
        channel.connect();

        String resultOfGrep = bufferedReader.readLine();
        System.out.println("Found value= " + resultOfGrep);
        return resultOfGrep != null;
    }

    // Moved from SSHExecutorInterface class
    public int sendCommandReturnValue(String configName) throws IOException, JSchException {

        String command = ("sed 's/[^ ]//g' $AMS_CONFIG_DIR/" + configName +
                "/data/export/* | awk '{ print length }' |  awk '{ SUM += $1} END { print SUM+0 }'");
        Channel channel = sshExecutor.sesConnection.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        channel.connect();
        InputStream inputStream = channel.getInputStream();
        InputStreamReader isReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(isReader);
        String amountOfTarget = bufferedReader.readLine();

        //converting a String with spaces to an Integer
        int amountInt = Integer.parseInt(amountOfTarget.trim());
        System.out.println("Found value= " + amountInt);

        inputStream.close();
        bufferedReader.close();
        channel.disconnect();

        return amountInt;
    }

}
