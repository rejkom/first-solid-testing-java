package com.rejkom.ams;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSHExecutorInterface {

    private static final Logger LOGGER = Logger.getLogger(SSHExecutorInterface.class.getName());
    private JSch jschSSHChannel;
    private String strUserName;
    private String strConnectionIP;
    private int intConnectionPort;
    public static Session sesConnection;
    private int intTimeOut;

    public SSHExecutorInterface(String strUserName, String strConnectionIP) {

        String PrivateKey = "src/main/resources/privateKey";

        intConnectionPort = 22;
        intTimeOut = 60000;

        jschSSHChannel = new JSch();
        try {
            jschSSHChannel.addIdentity(PrivateKey);
        } catch (JSchException jschX) {
            logError(jschX.getMessage());
        }

        String errorMessage = null;
        try {
            sesConnection = jschSSHChannel.getSession(strUserName, strConnectionIP, intConnectionPort);
            // UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
            sesConnection.setConfig("StrictHostKeyChecking", "no");
            sesConnection.setConfig("PreferredAuthentications", "publickey");
            sesConnection.connect(intTimeOut);


        } catch (JSchException jschX) {
            errorMessage = jschX.getMessage();
        }

        if (errorMessage != null)
            System.out.println(errorMessage);
        /* reading remote file without downloading
        public BufferedReader openFile(String fileName) throws JSchException, IOException
        {
            Channel channel = sesConnection.openChannel("exec");
            channel.connect();
            init Cat(fileName);
            InputStream in = channel.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(isr);

            return reader;
         } end of buffer reading */
    }

    private String logError(String errorMessage) {
        if (errorMessage != null) {
            LOGGER.log(Level.SEVERE, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, errorMessage});
        }

        return errorMessage;
    }

    private String logWarning(String warnMessage) {
        if (warnMessage != null) {
            LOGGER.log(Level.WARNING, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, warnMessage});
        }

        return warnMessage;
    }

    public String sendCommand(String command) {
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = sesConnection.openChannel("exec");
            ((ChannelExec) channel).setCommand(". .bashrc \n" + command);
            channel.connect();
            InputStream commandOutput = channel.getInputStream();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }
            channel.disconnect();
        } catch (IOException ioX) {
            logWarning(ioX.getMessage());
            return null;
        } catch (JSchException jschX) {
            logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    public String startAms(String configName) {

        String command = ". .bashrc \n ams start $AMS_CONFIG_DIR/" + configName;
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = sesConnection.openChannel("exec");
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
            logWarning(ioX.getMessage());
            return null;
        } catch (JSchException jschX) {
            logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    public String stopAms(String configName) {

        String command = ". .bashrc \n ams stop $AMS_CONFIG_DIR/" + configName;
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = sesConnection.openChannel("exec");
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
            logWarning(ioX.getMessage());
            return null;
        } catch (JSchException jschX) {
            logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    public int sendCommandReturnValue(String configName) throws IOException, JSchException {

        String command = ("sed 's/[^ ]//g' $AMS_CONFIG_DIR/" + configName +
                "/data/export/* | awk '{ print length }' |  awk '{ SUM += $1} END { print SUM+0 }'");
        Channel channel = sesConnection.openChannel("exec");
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

    public boolean grepFile(String filePatch, String fileName, String grepCommand)
            throws IOException, JSchException {

        String command = "cat " + filePatch + "/" + fileName + " | grep \"" + grepCommand + "\"";
        Channel channel = sesConnection.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        channel.connect();
        InputStream inputStream = channel.getInputStream();
        InputStreamReader isReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(isReader);
        String resultOfGrep = bufferedReader.readLine();

        System.out.println("Found value = " + resultOfGrep);

        inputStream.close();
        bufferedReader.close();
        channel.disconnect();

        boolean expected;
        if (resultOfGrep != null) expected = true;
        else expected = false;
        return expected;

    }

    public void close() {
        sesConnection.disconnect();
    }
}