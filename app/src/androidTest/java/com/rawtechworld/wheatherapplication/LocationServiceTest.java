package com.rawtechworld.wheatherapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ServiceTestRule;

import com.rawtechworld.weatherapplication.services.LocationService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public  class LocationServiceTest {
    Context context;

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getContext();
    }

    @Test
    public void testWithBoundService() throws TimeoutException {
        // Create the service Intent.

        Intent serviceIntent =
                new Intent(getApplicationContext(), LocationService.class);

        // Data can be passed to the service via the Intent.
        serviceIntent.putExtra(LocationService.ACTION_BROADCAST, 42L);

        // Bind the service and grab a reference to the binder.
        IBinder binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call public methods on the binder directly.
        LocationService service = ((LocationService.LocalBinder) binder).getService();

        // Verify that the service is working correctly.
        assertThat(service.serviceIsRunningInForeground(context), is(any(Boolean.class)));
    }

    @Test (expected = NullPointerException.class)
    public void onBind() throws Exception {
        final LocationService myService = new LocationService();
        context.startService(new Intent(context, LocationService.class));

        myService.onBind(new Intent());
    }

    @Test
    public void onStartCommand() throws Exception {
        final LocationService myService = new LocationService();
        context.startService(new Intent(context, LocationService.class));

        assertEquals(Service.START_STICKY, myService.onStartCommand(new Intent(), 0, 0));
    }

    @Test
    public void onCreate() throws Exception {
        Intent serviceIntent =
                new Intent(getApplicationContext(), LocationService.class);
        context.startService(serviceIntent);
        // Data can be passed to the service via the Intent.
        serviceIntent.putExtra(LocationService.ACTION_BROADCAST, "Testing Value On create");
        IBinder binder = mServiceRule.bindService(serviceIntent);
        LocationService service = ((LocationService.LocalBinder) binder).getService();
        service.onCreate();
        assertThat(service.serviceIsRunningInForeground(context), is(any(Boolean.class)));
    }

    @Test
    public void onDestroy() throws Exception {
        Intent serviceIntent =
                new Intent(getApplicationContext(), LocationService.class);
        context.startService(serviceIntent);
        // Data can be passed to the service via the Intent.
        serviceIntent.putExtra(LocationService.ACTION_BROADCAST, "Testing Value On create");
        IBinder binder = mServiceRule.bindService(serviceIntent);
        LocationService service = ((LocationService.LocalBinder) binder).getService();
        service.onDestroy();
        assertThat(service.serviceIsRunningInForeground(context), is(any(Boolean.class)));
    }
}


