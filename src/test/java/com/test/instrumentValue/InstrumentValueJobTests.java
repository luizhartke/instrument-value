package com.test.instrumentValue;

import com.test.InstrumentValueApplication;
import com.test.instrumentValue.helper.InstrumentValueJobHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = { InstrumentValueApplication.class, InstrumentValueJobHelper.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("hsqldb")
public class InstrumentValueJobTests {

    @Autowired
    private InstrumentValueJobHelper helper;

    @Test
    public void testProcessPushMessage() {
        helper.runInstrumentValue();
    }

}
