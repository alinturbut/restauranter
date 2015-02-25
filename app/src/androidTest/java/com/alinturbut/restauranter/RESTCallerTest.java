package com.alinturbut.restauranter;

import com.alinturbut.restauranter.helper.RESTCaller;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * @author alinturbut.
 */
@RunWith(RobolectricTestRunner.class)
public class RESTCallerTest {
    @Test
    public void testCallGet() {
        Assert.assertNotNull(RESTCaller.callGetByUrl("http://localhost:8089/waiter/all"));
        //System.out.println(RESTCaller.callGetByUrl("http://localhost:8089/waiter/all"));
    }

}
