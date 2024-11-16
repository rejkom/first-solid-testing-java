package com.rejkom.ams.manager;

import com.rejkom.ams.executor.SshCommandExecutor;

/**
 * This class handles configuration related operations.
 */
public class ConfigurationManager {

    private final SshCommandExecutor sshExecutor;

    public ConfigurationManager(SshCommandExecutor sshExecutor) {
        this.sshExecutor = sshExecutor;
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

    public void addNewProperties(String[][] properties) {
        for (String[] line : properties) {
            sshExecutor.sendCommand(". .bashrc \n " +
                    "echo \"" + line[1] + "=" + line[2] + "\">> $CONFIG_DIR/" + line[0] + "/" + line[3] + "\n");
        }
    }

    public void configureApplication(String configName) {
        sshExecutor.sendCommand(". .bashrc \n configureService " + configName + " \n");
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
