package com.sec.assignment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class DataInputFileRowTest {

    //@Test
    public void parseRowTest(){
        DataInputFileRow obj = new DataInputFileRow();
        String row = "CheckIn:\tOBJECT_NAME=ocsvm.pdf\tDATETIME=5/1/2013\t0:26:18\t1:45:10\tOBJECTNAME=ocsvm.pdf\tAPPLICATIONNAME=SECX-SHARE\tOBJECT_TYPE=secx_document\tHOST_NAME=secxmecsprd05\tMESSAGE=View/Export\tEVENT_DESCRIPTION=View/Export\tDATETIME_5=06/01/2012\t1:45:10\tUSER_NAME=2629\tCOMMANDNAME=secx_document\tSOURCEUSERNAME=USERNAME";
        obj.parseRow(row);
    }

    // 5/1/2013	0:26:18	1:45:10
    @Test
    public void testGetHour0(){
        DataInputFileRow obj = new DataInputFileRow();
        AssertionErrors.assertEquals("Invalid hours", "0", FileHandlerUtil.getHour("5/1/2013\t0:26:18"));
     }

    @Test
    public void testGetHour12(){
        AssertionErrors.assertEquals("Invalid hours", "12", FileHandlerUtil.getHour("5/1/2013\t12:26:18"));
    }

    @Test
    public void testHourCount(){

        DataInputFileRow obj = new DataInputFileRow();
        String row1 = "CheckIn:\tOBJECT_NAME=ocsvm.pdf\tDATETIME=5/1/2013\t0:26:18\t1:45:10\tOBJECTNAME=ocsvm.pdf\tAPPLICATIONNAME=SECX-SHARE\tOBJECT_TYPE=secx_document\tHOST_NAME=secxmecsprd05\tMESSAGE=View/Export\tEVENT_DESCRIPTION=View/Export\tDATETIME_5=06/01/2012\t1:45:10\tUSER_NAME=2629\tCOMMANDNAME=secx_document\tSOURCEUSERNAME=USERNAME";
        obj.parseRow(row1);

        String row2 = "CheckIn:\tOBJECT_NAME=ocsvm3.pdf\tDATETIME=5/1/2013\t0:36:18\t1:45:10\tOBJECTNAME=ocsvm.pdf\tAPPLICATIONNAME=SECX-SHARE\tOBJECT_TYPE=secx_document\tHOST_NAME=secxmecsprd05\tMESSAGE=View/Export\tEVENT_DESCRIPTION=View/Export\tDATETIME_5=06/01/2012\t1:45:10\tUSER_NAME=2629\tCOMMANDNAME=secx_document\tSOURCEUSERNAME=USERNAME";
        obj.parseRow(row2);

        List<KVPair> map = obj.getDateActivitCountMap();
        AssertionErrors.assertEquals("Invalid hour", "0",map.get(0).getKey());
        AssertionErrors.assertEquals("Invalid count", 2,map.get(0).getValue());
    }

    @Test
    public void testUserNameCount(){

        DataInputFileRow obj = new DataInputFileRow();
        String row1 = "CheckIn:\tOBJECT_NAME=ocsvm.pdf\tDATETIME=5/1/2013\t0:26:18\t1:45:10\tOBJECTNAME=ocsvm.pdf\tAPPLICATIONNAME=SECX-SHARE\tOBJECT_TYPE=secx_document\tHOST_NAME=secxmecsprd05\tMESSAGE=View/Export\tEVENT_DESCRIPTION=View/Export\tDATETIME_5=06/01/2012\t1:45:10\tUSER_NAME=2629\tCOMMANDNAME=secx_document\tSOURCEUSERNAME=USERNAME";
        obj.parseRow(row1);

        String row2 = "CheckIn:\tOBJECT_NAME=ocsvm3.pdf\tDATETIME=5/1/2013\t0:36:18\t1:45:10\tOBJECTNAME=ocsvm.pdf\tAPPLICATIONNAME=SECX-SHARE\tOBJECT_TYPE=secx_document\tHOST_NAME=secxmecsprd05\tMESSAGE=View/Export\tEVENT_DESCRIPTION=View/Export\tDATETIME_5=06/01/2012\t1:45:10\tUSER_NAME=2629\tCOMMANDNAME=secx_document\tSOURCEUSERNAME=USERNAME";
        obj.parseRow(row2);

        String row3 = "CheckIn:\tOBJECT_NAME=ocsvm3.pdf\tDATETIME=5/1/2013\t0:36:18\t1:45:10\tOBJECTNAME=ocsvm.pdf\tAPPLICATIONNAME=SECX-SHARE\tOBJECT_TYPE=secx_document\tHOST_NAME=secxmecsprd05\tMESSAGE=View/Export\tEVENT_DESCRIPTION=View/Export\tDATETIME_5=06/01/2012\t1:45:10\tUSER_NAME=2629\tCOMMANDNAME=secx_document\tSOURCEUSERNAME=USERNAME";
        obj.parseRow(row2);

        List<KVPair> map = obj.getTopUserNames();
        AssertionErrors.assertEquals("Invalid hour", "2629",map.get(0).getKey());
        AssertionErrors.assertEquals("Invalid count", 3,map.get(0).getValue());
    }
}
