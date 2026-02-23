package com.rejkom.ams;

import com.jcraft.jsch.JSchException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

public class XMapProcessorTest {

    static final String SAMPLE_NAME = "XsltMapProcessor";

    private static final String PROPERTIES_FILE = "config/ams-core-flow.params";

    Ams ams = new Ams("sshUserName", "host");


    public void operateOnMessageSequenceAdjust(int sequenceAdjust) {
        ams.restartService("XsltMapProcessor");
        ams.copyMsgTestData2Export("XsltMapProcessor",
                "fxforward-sample.json", "Transform_EMIR/");
    }

    public int outputIndentAmountParameter() throws IOException, JSchException {
        ams.restartService(SAMPLE_NAME);
        ams.copyMsgTestData2Export(SAMPLE_NAME,
                "file1.json", "sample/");
        return (ams.executeCommandAndGetCount(SAMPLE_NAME, "out"));
    }

    @DataProvider(name = "provideSequenceAdjustData")
    public Object[][] provideSequenceAdjustData() {
        return new Object[][]{{3, 7}, {50, 54}, {123, 127}, {456, 460}};
    }

    @DataProvider(name = "indentAmountData")
    public Object[][] indentAmountData() {
        return new Object[][]{{50, 552}, {30, 332}, {10, 112}, {4, 46}};
    }

         /*Test 'message.sequence.adjust' parameter -the integer amount should be added to
          the outgoing messages sequence numbers depending on the set value in "provideData" */

    @Test(dataProvider = "provideSequenceAdjustData", priority = 1)
    public void checkSequenceAdjustAmountInOutputFile(int sequenceAdjust, int expectedSequence)
            throws SQLException {
        operateOnMessageSequenceAdjust(sequenceAdjust);
        Assert.assertEquals(expectedSequence, expectedSequence);
        ams.stopService(SAMPLE_NAME);
    }

    /* Test 'outputOmitXmlDeclaration' parameter - output file should not include
     the XML declaration */
    @Test(priority = 3)
    public void outputOmitXmlDeclarationEqualsTrue() throws IOException, JSchException {
        ams.restartService("XsltMapProcessor");
        ams.copyMsgTestData2Export
                ("XsltMapProcessor", "fxforward-sample.json", "Transform_EMIR/");
        boolean shouldNotBeXmlDeclaration = ams.enterCommandReturnValue
                ("cat $AMS_CONFIG_DIR/XsltMapProcessor/data/"
                        + "export/* | grep \"<\\?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"\\?>.*\" ");

        Assert.assertEquals(shouldNotBeXmlDeclaration, false);
    }
    //Test 'outputOmitXmlDeclaration' parameter - output file should include the XML declaration

    @Test(priority = 4)
    public void outputOmitXmlDeclarationEqualsFalse() throws IOException, JSchException {
        String[][] props = {{SAMPLE_NAME, ".*outputOmitXmlDeclaration.*/processor.XsltMapper.outputOmitXmlDeclaration=false"}};
        ams.setProperties(props);
        ams.restartService(SAMPLE_NAME);
        ams.copyMsgTestData2Export(SAMPLE_NAME,
                "fxforward-sample.json", "Transform_EMIR/");
        boolean shouldBeXmlDeclaration = ams.enterCommandReturnValue
                ("cat $AMS_CONFIG_DIR/XsltMapProcessor/data/"
                        + "export/* | grep \"<\\?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"\\?>.*\" ");
        Assert.assertEquals(shouldBeXmlDeclaration, true);
    }

    /* Test 'ignoreWhiteSpace' parameter - XML elements into input file with only whitespace
    characters should be ignored and filtered out because filterEmptyElementsEqualsTrue*/
    @Test(priority = 5)
    public void ignoreWhiteSpaceEqualsTrue() throws IOException, JSchException {
        ams.restartService("XsltMapProcessor");
        ams.copyTestData("XsltMapProcessor/data/import/in/sample/");
        boolean sdiff = ams.compareFileResults("XsltMapProcessor", "XsltMapProcessor2",
                "XsltMapProcessor/ignore_white_space_and_filter_them.xml",
                "XsltMapProcessor2/data/export/*");
        Assert.assertEquals(sdiff, true);
    }

    @Test(priority = 10)
    public void outputIndentEqualsFalse() throws IOException, JSchException {
        ams.sendCommand("XsltMapProcessor");
        ams.sendCommand(".*outputIndent=.*/processor.XsltMapper.outputIndent=false");
        ams.restartService("XsltMapProcessor");
        ams.copyMsgTestData2Export("XsltMapProcessor",
                "file1.json", "sample/");
        Assert.assertEquals((ams.sendCommand("XsltMapProcessor")), 0);
    }

    //stop ams and close SSH connection
    @AfterTest
    private void stopAMSandCloseConnection() {
        ams.stopService("XsltMapProcessor");
        //ams.close();
    }

}
