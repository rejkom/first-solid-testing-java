package com.rejkom.ams;

import org.testng.annotations.*;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Xml2jsonTest {

    private static final String PROPERTIES_FILE = "config/ams-core-flow.params";

    Ams ams = new Ams("sshUserName", "host");

    private boolean compareFiles(String outputFile, String patternFile) throws IOException {

        Path oFile = Paths.get(outputFile);
        Path pFile = Paths.get(patternFile);
        boolean ret = Files.isSameFile(oFile, pFile);
        //System.out.print(ret);
        return ret;
    }

    static final String DESTINATION_SAMPLE = "XML2JSON";

    // files with messages from output directory
    static final String MSG_OUTPUT_PATH = "$AMS_CONFIG_DIR/XML2JSON/data/" +
            "output/final/";

    static final String MSG_OUTPUT1 = MSG_OUTPUT_PATH + "filePoller_*_0";
    static final String MSG_OUTPUT2 = MSG_OUTPUT_PATH + "filePoller_*_1";
    static final String MSG_OUTPUT3 = MSG_OUTPUT_PATH + "filePoller_*_2";
    static final String MSG_OUTPUT4 = MSG_OUTPUT_PATH + "filePoller_*_3";
    static final String MSG_OUTPUT5 = MSG_OUTPUT_PATH + "filePoller_*_4";
    static final String MSG_OUTPUT6 = MSG_OUTPUT_PATH + "filePoller_*_5";
    static final String MSG_OUTPUT7 = MSG_OUTPUT_PATH + "filePoller_*_6";
    static final String MSG_OUTPUT8 = MSG_OUTPUT_PATH + "filePoller_*_7";
    static final String MSG_OUTPUT9 = MSG_OUTPUT_PATH + "filePoller_*_8";
    static final String MSG_OUTPUT10 = MSG_OUTPUT_PATH + "filePoller_*_9";
    static final String MSG_OUTPUT11 = MSG_OUTPUT_PATH + "filePoller_*_10";
    static final String MSG_OUTPUT12 = MSG_OUTPUT_PATH + "filePoller_*_11";
    static final String MSG_OUTPUT13 = MSG_OUTPUT_PATH + "filePoller_*_12";
    static final String MSG_OUTPUT14 = MSG_OUTPUT_PATH + "filePoller_*_13";
    static final String MSG_OUTPUT15 = MSG_OUTPUT_PATH + "filePoller_*_14";
    static final String MSG_OUTPUT16 = MSG_OUTPUT_PATH + "filePoller_*_15";
    static final String MSG_OUTPUT17 = MSG_OUTPUT_PATH + "filePoller_*_16";
    static final String MSG_OUTPUT18 = MSG_OUTPUT_PATH + "filePoller_*_17";
    static final String MSG_OUTPUT19 = MSG_OUTPUT_PATH + "filePoller_*_18";
    static final String MSG_OUTPUT20 = MSG_OUTPUT_PATH + "filePoller_*_19";
    static final String MSG_OUTPUT21 = MSG_OUTPUT_PATH + "filePoller_*_20";

    // files with pattern messages
    static final String MSG_PATTERN_PATH = "$AMS_CONFIG_DIR/XML2JSON/" +
            "samples/pattern/";

    static final String MSG_PATTERN1 = MSG_PATTERN_PATH + "msg1";
    static final String MSG_PATTERN2 = MSG_PATTERN_PATH + "msg2";
    static final String MSG_PATTERN3 = MSG_PATTERN_PATH + "msg3";
    static final String MSG_PATTERN4 = MSG_PATTERN_PATH + "msg4";
    static final String MSG_PATTERN5 = MSG_PATTERN_PATH + "msg5";
    static final String MSG_PATTERN6 = MSG_PATTERN_PATH + "msg6";
    static final String MSG_PATTERN7 = MSG_PATTERN_PATH + "msg7";
    static final String MSG_PATTERN8 = MSG_PATTERN_PATH + "msg8";
    static final String MSG_PATTERN9 = MSG_PATTERN_PATH + "msg9";
    static final String MSG_PATTERN10 = MSG_PATTERN_PATH + "msg10";
    static final String MSG_PATTERN11 = MSG_PATTERN_PATH + "msg11";
    static final String MSG_PATTERN12 = MSG_PATTERN_PATH + "msg12";
    static final String MSG_PATTERN13 = MSG_PATTERN_PATH + "msg13";
    static final String MSG_PATTERN14 = MSG_PATTERN_PATH + "msg14";
    static final String MSG_PATTERN15 = MSG_PATTERN_PATH + "msg15";
    static final String MSG_PATTERN16 = MSG_PATTERN_PATH + "msg16";
    static final String MSG_PATTERN17 = MSG_PATTERN_PATH + "msg17";
    static final String MSG_PATTERN18 = MSG_PATTERN_PATH + "msg18";
    static final String MSG_PATTERN19 = MSG_PATTERN_PATH + "msg19";
    static final String MSG_PATTERN20 = MSG_PATTERN_PATH + "msg20";
    static final String MSG_PATTERN21 = MSG_PATTERN_PATH + "msg21";

    @BeforeTest
    private void prepareXml2jsonToTest() throws SQLException, IOException {

        //establishSSH.sendCommand(OperateOnAms.startOrchestrator());
        ams.startService("XML2JSON");

    }

    @DataProvider(name = "Xml2jsonData")
    private Object[][] xml2jsonData() {

        Object[][] msgFromDB = new Object[][]{
                {MSG_OUTPUT1, MSG_PATTERN1},
                {MSG_OUTPUT2, MSG_PATTERN2},
                {MSG_OUTPUT3, MSG_PATTERN3},
                {MSG_OUTPUT4, MSG_PATTERN4},
                {MSG_OUTPUT5, MSG_PATTERN5},
                {MSG_OUTPUT6, MSG_PATTERN6},
                {MSG_OUTPUT7, MSG_PATTERN7},
                {MSG_OUTPUT8, MSG_PATTERN8},
                {MSG_OUTPUT9, MSG_PATTERN9},
                {MSG_OUTPUT10, MSG_PATTERN10},
                {MSG_OUTPUT11, MSG_PATTERN11},
                {MSG_OUTPUT12, MSG_PATTERN12},
                {MSG_OUTPUT13, MSG_PATTERN13},
                {MSG_OUTPUT14, MSG_PATTERN14},
                {MSG_OUTPUT15, MSG_PATTERN15},
                {MSG_OUTPUT16, MSG_PATTERN16},
                {MSG_OUTPUT17, MSG_PATTERN17},
                {MSG_OUTPUT18, MSG_PATTERN18},
                {MSG_OUTPUT19, MSG_PATTERN19},
                {MSG_OUTPUT20, MSG_PATTERN20},
                {MSG_OUTPUT21, MSG_PATTERN21}

        };
        return msgFromDB;
    }

    //Compare resulting file with pattern file
    @Test(dataProvider = "Xml2jsonData")
    public void xml2jsonTest(String outputFile, String patternFile) throws SQLException, IOException {
        Assert.assertEquals(compareFiles(outputFile, patternFile), true);
    }

    //stop ams and close SSH connection
    @AfterTest
    private void stopAMSandCloseConnection() {
        ams.stopService(DESTINATION_SAMPLE);
        //ams.close();
    }
}
