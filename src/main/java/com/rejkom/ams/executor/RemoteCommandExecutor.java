package com.rejkom.ams.executor;

/**
 * This class contains methods related to executing commands and returning output.
 */
public class RemoteCommandExecutor {

    private final SshCommandExecutorOnLinux sshExecutor;

    public RemoteCommandExecutor(SshCommandExecutorOnLinux sshExecutor) {
        this.sshExecutor = sshExecutor;
    }

    public String executeCommandAndReturnOutput(String command) {
        return sshExecutor.executeCommand(command);
    }

    public int executeCommandAndGetCount(String commandTarget, String outputPath) {

        String command = "" + commandTarget + " $configDirectory/" + outputPath + "/* | wc -l";
        String targetCount = sshExecutor.executeCommand(command);
        // Convert the retrieved count from String to Integer
        int resultCount = Integer.parseInt(targetCount.trim());
        return resultCount;
    }

    public boolean enterCommandReturnValue(String enterCommand) {
        String command = enterCommand;
        String resultOfGrep = sshExecutor.executeCommand(command);
        System.out.println("Found value= " + resultOfGrep);
        return resultOfGrep != null;
    }

    // Moved from SSHExecutorInterface class
    public int sendCommandReturnValue(String configName) {

        String command = ("sed 's/[^ ]//g' $AMS_CONFIG_DIR/" + configName +
                "/data/export/* | awk '{ print length }' |  awk '{ SUM += $1} END { print SUM+0 }'");
        String amountOfTarget = sshExecutor.executeCommand(command);

        //converting a String with spaces to an Integer
        int amountInt = Integer.parseInt(amountOfTarget.trim());
        System.out.println("Found value= " + amountInt);

        return amountInt;
    }

}
