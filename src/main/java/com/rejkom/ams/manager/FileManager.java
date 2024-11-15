package com.rejkom.ams.manager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.rejkom.ams.executor.SSHExecutorInterface;

import java.io.*;

/**
 * This class handles data and file changes related operations.
 */
public class FileManager {

    private final SSHExecutorInterface sshExecutor;

    public FileManager(SSHExecutorInterface sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public void clearData() {
        sshExecutor.sendCommand(". .bashrc \n rm $CONFIG_DIR/data/input/done/* " +
                "$CONFIG_DIR/data/output/final/* \n");
    }

    public void copyTestData(String configName) {
        sshExecutor.sendCommand(". .bashrc \n cd $CONFIG_DIR/" + configName + "\n" +
                "cp test-data/* data/import/in/ \n sleep 7 ");
    }

    public void copyMsgTestData2Export(String configName, String msgName, String importPath) {
        sshExecutor.sendCommand(". .bashrc \n cd $ABC_CONFIG_DIR/" + configName + " \n " +
                "cp test-data/" + msgName + " data/import/in/" + importPath + " \n sleep 20");
    }

    public void copySqlStatement(String sqlStatement) {
        sshExecutor.sendCommand(". .bashrc \n cp $AMS_CONFIG_DIR/sampleMsg/import/sql/" +
                sqlStatement + " $ABC_CONFIG_DIR/JDBCDestination/data/import/in \n sleep 10");
    }

    public void jschReadAndSaveFile(String configNameFilePath, String saveFilePath) throws IOException, JSchException {
        String command = "cat $AMS_CONFIG_DIR" + configNameFilePath;
        String filePath = "$AMS_CONFIG_DIR sampleMsg/received/" + saveFilePath;

        try {
            Channel channel = sshExecutor.getSessionManager().getSession().openChannel("exec");
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

    public boolean compareFileResults(String expectedConfig, String receivedConfig,
                                      String expectedPath, String receivedPath) throws IOException, JSchException {

        String compareCommand = ". .bashrc \n sdiff -s $configDirectory/" + expectedConfig +
                "/test-data/" + expectedPath + " $configDirectory/" + receivedConfig +
                "/data/export/" + receivedPath + " 2>&1 | cat > $configDirectory/" + expectedConfig + "/result.xml";

        sshExecutor.sendCommand(compareCommand);
        String command = ". .bashrc \n cat $configDirectory/" + expectedConfig + "/result.xml \n";
        Channel channel = sshExecutor.getSessionManager().getSession().openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        channel.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        String diffResult = reader.readLine();

        System.out.println("Comparison result = " + diffResult);

        reader.close();
        channel.disconnect();

        return diffResult == null;
    }

    public boolean grepFile(String filePatch, String fileName, String grepCommand) {

        String command = "cat " + filePatch + "/" + fileName + " | grep \"" + grepCommand + "\"";
        return !sshExecutor.sendCommand(command).isEmpty();
    }

}
