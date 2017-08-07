package com.expoagro.expoagrobrasil.controller;

import android.os.Bundle;
import android.support.test.runner.MonitoringInstrumentation;

import cucumber.api.android.CucumberInstrumentationCore;

/**
 * Created by PC on 06/08/2017.
 */

public class Instrumentation extends MonitoringInstrumentation {

    private final CucumberInstrumentationCore mInstrumentationCore = new CucumberInstrumentationCore(this);

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);

        mInstrumentationCore.create(arguments);
        start();
    }

    @Override
    public void onStart() {
        super.onStart();

        waitForIdleSync();
        mInstrumentationCore.start();
    }
}
