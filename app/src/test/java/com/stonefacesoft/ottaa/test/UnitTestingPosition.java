package com.stonefacesoft.ottaa.test;

import com.stonefacesoft.ottaa.utils.ReturnPositionItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UnitTestingPosition {
   private ReturnPositionItem positionItem;
   private int position=0;
    @Before
    public void setUpClass(){
        positionItem=new ReturnPositionItem(5);
    }

    @Test
    public void runPositionItem(){
        runNtime(1,false);
        Assert.assertTrue(position==1);
        runNtime(3,true);
        Assert.assertTrue(position==3);
    }

    public void runNtime(int n,boolean isSustract){
        for (int i = 0; i <n ; i++) {
            if(isSustract)
                position=positionItem.subtract();
            else
                position=positionItem.add();
        }
    }

}
