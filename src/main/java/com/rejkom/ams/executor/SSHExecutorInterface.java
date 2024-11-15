package com.rejkom.ams.executor;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
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

    public String logError(String errorMessage) {
        if (errorMessage != null) {
            LOGGER.log(Level.SEVERE, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, errorMessage});
        }
        return errorMessage;
    }

    public String logWarning(String warnMessage) {
        if (warnMessage != null) {
            LOGGER.log(Level.WARNING, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, warnMessage});
        }
        return warnMessage;
    }

    public void close() {
        sesConnection.disconnect();
    }
}