package com.rejkom.ams;

import com.jcraft.jsch.JSchException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UdpSampleTest {

    private static final String DESTINATION_SAMPLE = "FileToUdp";
    private static final String SOURCE_SAMPLE = "UdpToFile";

    private static final String PATTERN_FILE =
            "$AMS_CONFIG_DIR/FileToUdp/test-data/sample-text-file.txt";
    private static final String RECEIVED_FILE =
            "$AMS_CONFIG_DIR/UdpToFile/data/output/txt-UdpSource_*_0.txt";


    private static final String COPY_TEST_DATA = ". .bashrc \n "
            + "cp $AMS_CONFIG_DIR/FileToUdp/test-data/sample-text-file.txt "
            + "$AMS_CONFIG_DIR/FileToUdp/data/import/in/ \n sleep 5";

    private static final String PREPARE_SAMPLES_FOR_RELEASE = "cd $AMS_CONFIG_DIR \n "
            + "ls -1 | egrep -v \"^(sampleMsg|CamelDestination|ServiceBusRequest|"
            + "TXT2XML|XML2JSON)$\" | xargs rm -r \n "
            + "cp $AMS_HOME/ams/config/sample/* . \n "
            + "cp -rf $HOME/resources/sampleMsg . \n ";

    private static final String PROPERTIES_FILE = "config/ams-flow.params";

    Ams ams = new Ams("sshUserName", "host");

    @BeforeTest
    public void prepareUdpFlow() throws IOException, JSchException {

//        ////////// uncomment only for release test /////////
//        ams.sendCommand(PREPARE_SAMPLES_FOR_RELEASE);
//        //additionally change DB password
//        ///////////////////////////////////////////////////

        //only in 1st test
        ams.startOrchestrator();

        //Operate on FileToUdp sample
        ams.startService(DESTINATION_SAMPLE);
        //Operate on UdpToFile sample
        ams.startService(SOURCE_SAMPLE);
        //copy test data
        ams.sendCommand(COPY_TEST_DATA);
    }

    private boolean compareFiles() throws IOException {

        Path outputFile = Paths.get(RECEIVED_FILE);
        Path patternFile = Paths.get(PATTERN_FILE);
        boolean ret = Files.isSameFile(outputFile, patternFile);
        return ret;
    }

    @Test
    public void compareReceivedFile() throws IOException {
        Assert.assertEquals(compareFiles(), true);
    }

    @AfterTest
    public void closeSHHcon() {
        ams.stopService(DESTINATION_SAMPLE);
        ams.stopService(SOURCE_SAMPLE);
        //ams.close();
    }
}
