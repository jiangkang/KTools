package com.jiangkang.tools.mock;

import android.content.Context;

import com.jiangkang.tools.R;
import com.jiangkang.tools.utils.AppUtils;
import com.jiangkang.tools.utils.FileUtils;
import com.jiangkang.tools.utils.SpUtils;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnitTestSample {

    private static final String FAKE_STRING = "tools";

    private static final String SP_KEY = "sp_key";

    @Mock
    Context mMockContext;


    @Test
    public void readStringFromContext_LocalizedString(){
//        when(mMockContext.getString(R.string.app_name))
//                .thenReturn(FAKE_STRING);

        SpUtils spUtils = SpUtils.getInstance(mMockContext,SP_KEY);

        spUtils.put("key_test","value_test");

        String result = spUtils.getString("key_test","");

        Assert.assertEquals("value_test",result);

    }




}
