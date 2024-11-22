package com.rejkom.ams.manager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.rejkom.ams.executor.SshCommandExecutor;

import java.io.*;
import java.util.logging.Logger;

public class LinuxFileManager implements FileManager {

    private static final Logger LOGGER = Logger.getLogger(LinuxFileManager.class.getName());
    private final SshCommandExecutor sshExecutor;
    private final SshSessionManager sessionManager;

    public LinuxFileManager(SshCommandExecutor sshExecutor, SshSessionManager sessionManager) {
        this.sshExecutor = sshExecutor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void clearData() {
        sshExecutor.executeCommand(". .bashrc \n rm $CONFIG_DIR/data/input/done/* " +
                "$CONFIG_DIR/data/output/final/* \n");
    }

    @Override
    public void copyTestData(String configName) {
        sshExecutor.executeCommand(". .bashrc \n cd $CONFIG_DIR/" + configName + "\n" +
                "cp test-data/* data/import/in/ \n sleep 7 ");
    }

    @Override
    public void copyMsgTestData2Export(String configName, String msgName, String importPath) {
        sshExecutor.executeCommand(". .bashrc \n cd $ABC_CONFIG_DIR/" + configName + " \n " +
                "cp test-data/" + msgName + " data/import/in/" + importPath + " \n sleep 20");
    }

    @Override
    public void copySqlStatement(String sqlStatement) {
        sshExecutor.executeCommand(". .bashrc \n cp $AMS_CONFIG_DIR/sampleMsg/import/sql/" +
                sqlStatement + " $ABC_CONFIG_DIR/JDBCDestination/data/import/in \n sleep 10");
    }

    @Override
    public void jschReadAndSaveFile(String configNameFilePath, String saveFilePath) {
        String command = "cat $AMS_CONFIG_DIR" + configNameFilePath;
        String filePath = "$AMS_CONFIG_DIR sampleMsg/received/" + saveFilePath;

        try {
            Channel channel = sessionManager.getSession().openChannel("exec");
            OutputStream outputStream = new FileOutputStream(filePath);
            InputStream inputStream = channel.getInputStream();

            ((ChannelExec) channel).setCommand(command);
            channel.connect();

            int BUFFER_SIZE = 4096;
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean compareFileResults(String expectedConfig, String receivedConfig, String expectedPath, String receivedPath) {
        String compareCommand = ". .bashrc \n sdiff -s $configDirectory/" + expectedConfig +
                "/test-data/" + expectedPath + " $configDirectory/" + receivedConfig +
                "/data/export/" + receivedPath + " 2>&1 | cat > $configDirectory/" + expectedConfig + "/result.xml";

        sshExecutor.executeCommand(compareCommand);
        String command = ". .bashrc \n cat $configDirectory/" + expectedConfig + "/result.xml \n";
        String diffResult = "";
        try {
            Channel channel = sessionManager.getSession().openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            channel.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            diffResult = reader.readLine();

            System.out.println("Comparison result = " + diffResult);

            reader.close();
            channel.disconnect();
        } catch (JSchException | IOException ex) {
            LOGGER.severe(ex.getMessage());
        }

        return diffResult == null;
    }

    @Override
    public boolean searchFile(String filePatch, String fileName, String grepCommand) {
        String command = "cat " + filePatch + "/" + fileName + " | grep \"" + grepCommand + "\"";
        return !sshExecutor.executeCommand(command).isEmpty();
    }

}
