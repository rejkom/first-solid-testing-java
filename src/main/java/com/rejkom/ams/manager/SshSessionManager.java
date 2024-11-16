package com.rejkom.ams.manager;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshSessionManager {

    private final String strUserName;
    private final String strConnectionIP;
    private final int intConnectionPort;
    private final int intTimeOut;
    private Session session;

    public SshSessionManager(String strUserName, String strConnectionIP) {
        this.strUserName = strUserName;
        this.strConnectionIP = strConnectionIP;
        this.intConnectionPort = 22;
        this.intTimeOut = 60000;
    }

    public Session connectSession() {
        JSch jschSSHChannel = new JSch();
        String privateKey = "src/main/resources/privateKey";
        try {
            jschSSHChannel.addIdentity(privateKey);
            session = jschSSHChannel.getSession(strUserName, strConnectionIP, intConnectionPort);
            session.setConfig("StrictHostKeyChecking", "no"); // For testing only
            session.setConfig("PreferredAuthentications", "publickey");
            session.connect(intTimeOut);
        } catch (JSchException e) {
            System.err.println("Error connecting to session: " + e.getMessage());
        }
        return session;
    }

    public void disconnectSession() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    public Session getSession() {
        return session;
    }

}
