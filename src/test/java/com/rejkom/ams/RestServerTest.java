package com.rejkom.ams;

import com.jcraft.jsch.JSchException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class RestServerTest {

    static final String SAMPLE_NAME = "RestServer";

    static final String GET_JM = ". .bashrc \n wget http:\\/\\/www-us.apache.org\\/dist\\/\\/" +
            "jm\\/binaries\\/apache-jm-2.13.tgz \n sleep 120 \n " +
            "gtar -xf apache-jm-2.13.tgz \n sleep 5 \n rm apache-jm-2.13.tgz \n " +
            "mkdir jM \n mv apache-jm-2.13 jM \n cd jM \n mkdir projects \n " +
            "mkdir projects/RestServer \n " +
            "cp $HOME/scripts/jm/RestServer/* $HOME/jM/projects/RestServer/. \n ";

    static final String SEND_REQUEST = "cd jM/apache-jm-2.13/bin \n " +
            "nohup sh jm.sh -n -t ../../projects/RestServer/HTTP_Request_RestServer.jmx \n " +
            "sleep 10";

    static final String SEND_VALID_REQUEST = ". .bashrc \n cd jM/apache-jm-2.13/bin \n" +
            " nohup sh jm.sh -n -t ../../projects/RestServer/" +
            "HTTP_Request_RestServer_validconnection.jmx \n sleep 10";

    //request with path: /AMS-endpoint
    static final String SEND_REQUEST_CHANGE_ENDPOINT = ". .bashrc \n " +
            "cd jM/apache-jm-2.13/bin \n " +
            "nohup sh jm.sh -n -t $HOME/jM/projects/RestServer/" +
            "HTTP_Request_RestServer_changeendpoint.jmx \n sleep 10";

    //request with message: i_123Abc_ *-aaa
    static final String SEND_REQUEST_CHANGE_BODY = ". .bashrc \n " +
            "cd jM/apache-jm-2.13/bin \n" +
            " nohup sh jm.sh -n -t ../../projects/RestServer/" +
            "HTTP_Request_RestServer_changebody.jmx \n sleep 10";

    // for addNewProperty
    static final String[][] BACKWARDS_COMPATIBILITY = {
            {SAMPLE_NAME, "resource.list", "HttpResource", "director.properties"},
            {SAMPLE_NAME, "resource.HttpResource.class", "HttpServerResource", "director.properties"},
            {SAMPLE_NAME, "resource.HttpResource.port", "8485", "director.properties"},
            {SAMPLE_NAME, "resource.HttpResource.interface", "localhost", "director.properties"},
            {SAMPLE_NAME, "source.RestServer.httpResource", "HttpResource", "director.properties"},
            {SAMPLE_NAME, "source.RestServer.endpoint", "/AMS-rest", "director.properties"}

//            {config, "property name","value of property", "file"},
    };


    static final String PROPERTIES = ". .bashrc \n cd $AMS_CONFIG_DIR/" +
            SAMPLE_NAME + "\n sed -e 's/source.RestServer.baseUri=.*/" +
            "#source.RestServer.baseUri=/' director.properties > dp.temp && " +
            "mv dp.temp director.properties \n sleep 2 ";

    static final String CLEAR_EXPORT_DIRECTORY = ". .bashrc \n rm $AMS_CONFIG_DIR/" +
            SAMPLE_NAME + "/data/export/* \n";

    static final String[][] WITH_PROPERTIES = {
            {SAMPLE_NAME, "director.properties", "destination.ResultDest.withProperties", "true"},
            {SAMPLE_NAME, "director.properties", "source.RestServer.endpoint", "\\/ams-endpoint"}
    };

    static final String GET_PATTERN_DATA = ". .bashrc \n " +
            "mkdir /data/config/RestServer/test-data/ \n " +
            "cp $AMS_CONFIG_DIR/sampleMsg/pattern/RestServer/* " +
            "$AMS_CONFIG_DIR/" + SAMPLE_NAME + "/test-data/. \n";

    static final String EXPORT_PATH = "/data/config/RestServer/data/export/";

    //dedicated to avoid MGO-862
    static final String[][] ADD_AND_REMOVE_LINE2 = {
            {SAMPLE_NAME, "/data/export/", "Result-RestServer_*_0.txt", "new-TEST_0.txt"},
            //{SAMPLE_NAME, "/data/export/","Result-RestServer_*_1.txt", "new-TEST_1.txt"},
            {SAMPLE_NAME, "/test-data/", "Expected-RestServer.txt",
                    "Expected-RestServer_new.txt"},
            {SAMPLE_NAME, "/test-data/", "Expected-RestServer_signs.txt",
                    "Expected-RestServer_newsigns.txt"}
    };

//    static final String PREPARE_REST_SERVER_SECURE_SAMPLE =
//            "mkdir $AMS_CONFIG_DIR/RestServerSecure \n " +
//                    "cp -r $AMS_CONFIG_DIR/RestServer/* $AMS_CONFIG_DIR/RestServerSecure/. \n ";

    private static final String PROPERTIES_FILE = "config/ams-core-flow.params";

    Ams ams = new Ams("sshUserName", "host");

    @DataProvider(name = "provideData")
    public Object[][] provideData() {
        return new Object[][]{
                {"Expected-RestServer_new.txt", "new-TEST_0.txt"}
        };
    }

//    @DataProvider(name = "provideOtherData")
//    public Object[][] provideOtherData() {
//        return new Object[][]{
//                {"Expected-RestServer_newsigns.txt", "new-TEST_1.txt"}
//        };
//    }

    @BeforeTest
    public void prepareFileToJmsFlow() throws IOException, JSchException {
        //download jM and set project
        ams.sendCommand(GET_JM);

        //set properties for RestServer sample
        ams.sendCommand(PROPERTIES);
        ams.addNewProperties(BACKWARDS_COMPATIBILITY);

        //start AMS sample
        ams.startService(SAMPLE_NAME);
        System.out.println("***** Rest ServerTest results: \n");

    }

    //was AMS RestServer reached the request (HTTP_Request_RestServer.jmx)
    @Test(priority = 1)
    public void searchLogForReceivedRequest() throws JSchException, IOException {
        ams.sendCommand(SEND_REQUEST);
        Assert.assertEquals(ams.checkLogContains(SAMPLE_NAME, "received MESSAGE request"), true);

    }

    //was the response included the same message (as request)
    @Test(priority = 2)
    public void searchLogForHelloRequest() throws JSchException, IOException {
        Assert.assertEquals(ams.checkLogContains(SAMPLE_NAME, "\\\"id\\\" : \\\"1\\\", \\\"body\\\" : " +
                "\\\"hello world\\\", \\\"status\\\" : \\\"SUCCESSFUL\\\""), true);
    }


    // the message should be saved in the export directory
    @Test(priority = 3)
    public void checkExportedFile() throws JSchException, IOException {
        int folderShouldNotBeEmpty = ams.executeCommandAndGetCount("ls -A", "RestServer/data/export");
        Assert.assertEquals(folderShouldNotBeEmpty, 1);
    }


    // compare pattern message with exported (form files)
    // MGO-862 Warning: missing newline at end of file
    @Test(dataProvider = "provideData", priority = 4)
    public void provideData(String expectedFilePath, String receivedFilePath)
            throws IOException, JSchException {

        ams.sendCommand(GET_PATTERN_DATA);
        //dedicated to avoid MGO-862
        ams.removeEOL(ADD_AND_REMOVE_LINE2);
        boolean result = ams.compareFileResults
                (SAMPLE_NAME, SAMPLE_NAME, expectedFilePath, receivedFilePath);
        ams.sendCommand("sleep 5");
        Assert.assertEquals(result, true);
    }

//    // the message should be save in the export directory
//    @Test (priority = 5)
//    public void checkContentOfExportedDirectory() throws JSchException, IOException {
//
//        int folderShouldNotBeEmpty = ams.jschReturnSentCommand
//                ("ls -A", "RestServer/data/export");
//        Assert.assertEquals(folderShouldNotBeEmpty, 4);
//    }
//
//    // compare pattern message with exported (form files)
//    // MGO-862 Warning: missing newline at end of file
//    // request with message: "i_123Abc_ *-aaa"
//    @Test(dataProvider = "provideOtherData", priority = 6)
//    public void provideOtherData(String expectedFilePath, String receivedFilePath)
//            throws IOException, JSchException {
//
//        ams.sendCommand(CLEAR_EXPORT_DIRECTORY);
//        ams.sendCommand(SEND_REQUEST_CHANGE_BODY);
//        //dedicated to avoid MGO-862
//        ams.removeEOL(ADD_AND_REMOVE_LINE2);
//        boolean result = ams.compareFileResultFromExpected
//                ( SAMPLE_NAME, SAMPLE_NAME , expectedFilePath, receivedFilePath);
//        ams.sendCommand("sleep 5");
//        Assert.assertEquals(result, true);
//    }
//
//
//
//    //the valid message will not be save in the export directory (also check reason reported - error 404)
//    @Test (priority = 7)
//    public void checkExportedFileInvalidaMessage() throws JSchException, IOException {
//
//        ams.sendCommand(CLEAR_EXPORT_DIRECTORY);
//        ams.sendCommand(SEND_VALID_REQUEST);
//        Assert.assertEquals(ams.checkLogInfo( SAMPLE_NAME ,"< 404" ), true);
//        int folderShouldNotBeEmpty = ams.jschReturnSentCommand
//                ("ls -A", "RestServer/data/export");
//        Assert.assertEquals(folderShouldNotBeEmpty, 0);
//    }
//
//    // set withProperties=true
//    // was the message saved in the export directory
//    @Test (priority = 8)
//    public void checkExportedFileWithProperties() throws JSchException, IOException {
//
//        ams.setProperties(WITH_PROPERTIES);
//        ams.restartAms(SAMPLE_NAME);
//        ams.sendCommand(SEND_REQUEST_CHANGE_ENDPOINT);
//        Assert.assertEquals(ams.checkLogInfo( SAMPLE_NAME ,"received MESSAGE request" ), true);
//    }
//
//    // check message PROPERTIES
//    @Test (priority = 9)
//    public void checkMessageProperties() throws JSchException, IOException {
//
//        Assert.assertEquals(ams.checkFileInfo
//                ( "/data/config/RestServer/data/export",
//                        "Result-RestServer_*_0.txt", "^REQUEST_URI"), true);
//        Assert.assertEquals(ams.checkFileInfo
//                ( "/data/config/RestServer/data/export",
//                        "Result-RestServer_*_0.txt", "^METHOD=POST"), true);
//        Assert.assertEquals(ams.checkFileInfo
//                ( "/data/config/RestServer/data/export",
//                        "Result-RestServer_*_0.txt", "^PATH=message\\/"), true);
//    }

    @AfterTest
    public void closeSHHconnection() {
        ams.stopService(SAMPLE_NAME);
        ams.close();
    }
}
