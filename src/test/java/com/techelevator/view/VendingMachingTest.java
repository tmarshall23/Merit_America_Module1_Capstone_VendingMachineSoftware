package com.techelevator.view;


import com.techelevator.VendingMachineCLI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class VendingMachingTest {

    @Before
public void testSetup(){
        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("A1",5);
        testMap.put("A2",5);
        testMap.put("A3",5);
        testMap.put("A4",5);
        testMap.put("A5",5);


    }






    @Test
        public void isMoneyProvidedAddedContinuously(){
        Menu menu = new Menu(System.in,System.out);
        VendingMachineCLI thisMachine = new VendingMachineCLI(menu);
        double thisDouble = thisMachine.testFeedMoney(5);
        Assert.assertEquals(5,thisDouble,0.000);
        thisDouble += thisMachine.testFeedMoney(10);
        Assert.assertEquals(15,thisDouble,0.000);
        thisDouble += thisMachine.testFeedMoney(0);
        Assert.assertEquals(15,thisDouble,0.000);
    }
    @Test
    public void doesFeedMoneyAccountForWrongInput(){
        Menu menu = new Menu(System.in,System.out);
        VendingMachineCLI thisMachine = new VendingMachineCLI(menu);

    }

    @Test
    public void DoesMethodGrabCorrectInformation(){
        Menu menu = new Menu(System.in,System.out);
        VendingMachineCLI thisMachine = new VendingMachineCLI(menu);
        double money = 1000;
        thisMachine.newMap();
        thisMachine.selectProduct("A1",2);
        Assert.assertTrue(thisMachine.newMap().containsKey("A1"));
        Assert.assertTrue(thisMachine.newMap().get("A1") > 2);
        Assert.assertTrue(thisMachine.itemPrice("A1")< money);
        Assert.assertEquals(3,thisMachine.newMap().get("A1") - 2);
        double thisDouble = Math.round(thisMachine.itemPrice("A1")*100*2);
        Assert.assertEquals(3.90,(money - thisDouble)/100,0.00000);
    }























}