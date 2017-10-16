package com.example.currencyconverter;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by user on 17.05.17.
 */

@RunWith(AndroidJUnit4.class)
public class PageLoaderServiceTest {

    @Test
    public void testServiceStartedAndStopped() {
        final Context context = InstrumentationRegistry.getTargetContext();

        Intent serviceIntent = new Intent(context,PageLoaderService.class);
        context.startService(serviceIntent);

        // wait while service is being started
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(PageLoaderService.sStarted);

        // wait while service is being stopped
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(PageLoaderService.sStarted);
    }

}
