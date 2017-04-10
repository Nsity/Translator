package com.translator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.translator.navigation.TranslationManager;
import com.translator.system.network.CallBack;

import org.junit.Test;

/**
 * Created by fedorova on 10.04.2017.
 */

public class RequesterTest {

    @Test
    public void getLangs() {
        Context context = InstrumentationRegistry.getTargetContext();

       /* TranslationManager.getLanguages(context, "ru", new CallBack() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onFailure(Object result) {

            }
        });*/
    }
}
