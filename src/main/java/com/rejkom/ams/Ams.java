package com.rejkom.ams;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

import java.io.*;

public class Ams {

    static SSHExecutorInterface sshExecutor;

    public Ams(String user, String ipAddress) {
        this.sshExecutor = new SSHExecutorInterface(user, ipAddress);
    }

    public boolean sendCommand(String command) {
        sshExecutor.sendCommand(command);
        return false;
    }

    public void setProperties(String[][] properties) {
        for (String[] line : properties) {
            sshExecutor.sendCommand(". .bashrc \n" +
                    "cd $CONFIG_DIR/" + line[0] + "\n" +
                    line[1] + "\n" +
                    "sed -e 's/^" + line[2] + ".*/" + line[2] + "=" + line[3] + "/' " +
                    line[1] + " > temp.config && mv temp.config " + line[1] + "\n");
        }
    }

    public void startService(String configName) {
        sshExecutor.sendCommand(". .bashrc \n SERVICE start $CONFIG_DIR/" + configName + "\n" +
                "sleep 10");
    }

    public void restartService(String configName) {
        sshExecutor.sendCommand(". .bashrc \n SERVICE restart $CONFIG_DIR/" + configName + "\n" +
                "sleep 15");
    }

    public void stopService(String configName) {
        sshExecutor.sendCommand(". .bashrc \n SERVICE stop $CONFIG_DIR/" + configName + "\n");
    }

    public void startOrchestrator() {
        sshExecutor.sendCommand(". .bashrc ; sudo $HOME/startOrchestrator " +
                "start < /dev/null >& /dev/null & sleep 10 \n");
    }

    public void stopOrchestrator() {
        sshExecutor.sendCommand(". .bashrc ; $HOME/stopOrchestrator " +
                "stop < /dev/null >& /dev/null & sleep 10");
    }

    public void startWebContainer() {
        sshExecutor.sendCommand("startapplication < /dev/null >& /dev/null \n sleep 10 ");
    }

    public void stopWebContainer() {
        sshExecutor.sendCommand("stopapplication < /dev/null >& /dev/null \n sleep 10 ");
    }

    public void startDatabase() {
        sshExecutor.sendCommand("startdatabase < /dev/null >& /dev/null \n sleep 10 ");
    }

    public void stopDatabase() {
        sshExecutor.sendCommand("stopdatabase < /dev/null >& /dev/null \n sleep 10 ");
    }

    public void configureApplication(String configName) {
        sshExecutor.sendCommand(". .bashrc \n configureService " + configName + " \n");
    }

    public void clearData() {
        sshExecutor.sendCommand(". .bashrc \n rm $CONFIG_DIR/data/input/done/* " +
                "$CONFIG_DIR/data/output/final/* \n");
    }

    public void copyTestData(String configName) {
        sshExecutor.sendCommand(". .bashrc \n cd $CONFIG_DIR/" + configName + "\n" +
                "cp test-data/* data/import/in/ \n sleep 7 ");
    }

    public void close() {
        sshExecutor.close();
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

    public boolean checkLogContains(String configName, String searchLine) throws IOException, JSchException {
        String command = "cat $CONFIG_DIR/" + configName + "/logs/application.log | grep \"" + searchLine + "\"";
        Channel channel = sshExecutor.sesConnection.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        channel.connect();

        try (InputStream inputStream = channel.getInputStream();
             InputStreamReader isReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(isReader)) {
            String result = bufferedReader.readLine();
            return result != null;
        } finally {
            channel.disconnect();
        }
    }

    public void addNewProperties(String[][] properties) {
        for (String[] line : properties) {
            sshExecutor.sendCommand(". .bashrc \n " +
                    "echo \"" + line[1] + "=" + line[2] + "\">> $CONFIG_DIR/" + line[0] + "/" + line[3] + "\n");
        }
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

    public boolean compareFileResults(String expectedConfig, String receivedConfig,
                                String expectedPath, String receivedPath) throws IOException, JSchException {

        String compareCommand = ". .bashrc \n sdiff -s $configDirectory/" + expectedConfig +
                "/test-data/" + expectedPath + " $configDirectory/" + receivedConfig +
                "/data/export/" + receivedPath + " 2>&1 | cat > $configDirectory/" + expectedConfig + "/result.xml";

        sendCommand(compareCommand);
        String command = ". .bashrc \n cat $configDirectory/" + expectedConfig + "/result.xml \n";
        Channel channel = sshExecutor.sesConnection.openChannel("exec");
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

    public void removeEOL(String[][] properties) {
        for (String[] line : properties) {
            String command = String.format(
                    "echo '\\nNEW LINE' >> $AMS_CONFIG_DIR/%s%s%s\n sed '$ d' $AMS_CONFIG_DIR/%s%s > $AMS_CONFIG_DIR/%s%s%s",
                    line[0], line[1], line[2], line[0], line[1], line[0], line[1], line[3]
            );
            sshExecutor.sendCommand(command);
        }
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

    public void operateOnDbValues(String configName) {
        String command = String.format(
                ". .bashrc \n cd %s%s \n sleep 2 \n sed -e 's/DB_HOST=.*/DB_HOST=env/g' " +
                        "database.properties > dp.temp && mv dp.temp database.properties \n sleep 5",
                "$AMS_CONFIG_DIR", configName
        );
        sshExecutor.sendCommand(command);
    }

    public void copySqlStatement(String sqlStatement) {
        sshExecutor.sendCommand(". .bashrc \n cp $AMS_CONFIG_DIR/sampleMsg/import/sql/" +
                sqlStatement + " $ABC_CONFIG_DIR/JDBCDestination/data/import/in \n sleep 10");
    }

    public void jschReadAndSaveFile(String configNameFilePath, String saveFilePath) throws IOException, JSchException {
        String command = "cat $AMS_CONFIG_DIR" + configNameFilePath;
        String filePath = "$AMS_CONFIG_DIR sampleMsg/received/" + saveFilePath;

        try {
            Channel channel = sshExecutor.sesConnection.openChannel("exec");
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

    public void copyMsgTestData2Export(String configName, String msgName, String importPath) {
        sshExecutor.sendCommand(". .bashrc \n cd $ABC_CONFIG_DIR/" + configName + " \n " +
                "cp test-data/" + msgName + " data/import/in/" + importPath + " \n sleep 20");
    }

    public void setJMSProperties(String[][] properties) {

        for (String[] line : properties) {
            sshExecutor.sendCommand("sed -e 's/^service.jms.provider.url=.*"
                    + "/service.jms.provider.url=file:jndi-directory"
                    + line[0] + "/service.jms.properties > $AMS_CONFIG_DIR/"
                    + line[0] + "/temp && mv $AMS_CONFIG_DIR/"
                    + line[0] + "/temp $AMS_CONFIG_DIR/"
                    + line[0] + "/service.jms.properties \n "
                    + line[0] + "/service.jms.properties > $AMS_CONFIG_DIR/"
                    + line[0] + "/temp && mv $AMS_CONFIG_DIR/"
                    + line[0] + "/temp $AMS_CONFIG_DIR/"
                    + line[0] + "/service.jms.properties \n ");
        }

    }

}