package com.stonefacesoft.ottaa.test.utils;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import junit.framework.TestResult;

public class UnitTestinTestResultUtils implements TestListener {
    public TestResult testResult;

    public UnitTestinTestResultUtils(){
        testResult=new TestResult();
        testResult.addListener(this);
    }

    @Override
    public void addError(Test test, Throwable e) {
        testResult.addError(test,e);
    }

    @Override
    public void addFailure(Test test, AssertionFailedError e) {
        testResult.addFailure(test,e);
    }

    @Override
    public void endTest(Test test) {
        testResult.endTest(test);
    }

    @Override
    public void startTest(Test test) {
        test.run(testResult);
    }
}
