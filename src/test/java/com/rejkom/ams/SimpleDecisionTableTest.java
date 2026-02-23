package com.rejkom.ams;

import com.jcraft.jsch.JSchException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class SimpleDecisionTableTest {

    static final String SAMPLE_NAME = "SimpleDecisionTable";

    static final String COPY_INVALID_TEST_DATA = "cd $AMS_CONFIG_DIR/SimpleDecisionTable/ \n " +
            "cp test-data/invalid.xml data/import/in/ \n sleep 10";

    static final String COPY_SPOT_TEST_DATA = "cd $AMS_CONFIG_DIR/SimpleDecisionTable/ \n " +
            "cp test-data/spot.xml data/import/in/ \n sleep 10";

    static final String COPY_IRS_TEST_DATA = "cd $AMS_CONFIG_DIR/SimpleDecisionTable/ \n " +
            "cp test-data/irs.xml data/import/in/ \n sleep 10";


    private static final String PROPERTIES_FILE = "config/ams-core-flow.params";

    Ams ams = new Ams("sshUserName", "host");

    @BeforeTest
    public void prepareFileToShellExecFlow() throws JSchException, IOException {
        ams.startService(SAMPLE_NAME);
    }

    @Test(priority = 1)
    public void checkInvalidTestData() throws JSchException, IOException {
        ams.sendCommand(COPY_INVALID_TEST_DATA);
        boolean invalidData = ams.checkLogContains(SAMPLE_NAME, "script CheckMarket.groovy returned an exception");
        Assert.assertEquals(invalidData, true);
    }

    @Test(priority = 2)
    public void checkSpotTestData() throws JSchException, IOException {
        ams.sendCommand(COPY_SPOT_TEST_DATA);
        boolean spotData = ams.checkLogContains(SAMPLE_NAME, "{DealType=SpotDeals, Market=Forex, #=1}");
        Assert.assertEquals(spotData, true);
    }

    @Test(priority = 3)
    public void checkIrsTestData() throws JSchException, IOException {
        ams.sendCommand(COPY_IRS_TEST_DATA);
        boolean IrsData = ams.checkLogContains(SAMPLE_NAME, "{DealType=SwapDeals, Market=DerivativeMarket, #=5}");
        Assert.assertEquals(IrsData, true);
    }

    @AfterTest
    public void closeSHHconnection() {
        ams.stopService(SAMPLE_NAME);
    }

}
