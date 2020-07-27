package com.jiangkang.ktools;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;


/**
 * Created by jiangkang on 2018/1/30.
 * description：
 */
@RunWith(RobolectricTestRunner.class)
public class SystemActivityTest {

    private AppCompatActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(SystemActivity.class).create().get();
    }


    @Test
    public void validateButtonContent() {

        Button button = activity.findViewById(R.id.btn_open_contacts);

        assertNotNull("open contacts not null",button);

    }


}