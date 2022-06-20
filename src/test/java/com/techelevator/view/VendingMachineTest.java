package com.techelevator.view;


import com.techelevator.VendingMachineCLI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;


   public class VendingMachineTest {
   private Menu menu;
   private VendingMachineCLI thisMachine;
   private File file;

    @Before
    public void testSetup(){
    this.menu = new Menu(System.in,System.out);
    this.thisMachine = new VendingMachineCLI(menu);
    this.file = this.thisMachine.getVendingFile();

    }

    @Test
    public void isMoneyProvidedAddedContinuously(){
    double thisDouble = this.thisMachine.testFeedMoney(5);
    Assert.assertEquals(5,thisDouble,0.000);
    thisDouble += this.thisMachine.testFeedMoney(10);
    Assert.assertEquals(15,thisDouble,0.000);
    thisDouble += this.thisMachine.testFeedMoney(0);
    Assert.assertEquals(15,thisDouble,0.000);
    }

    @Test
    public void doesMethodGrabCorrectInformation(){
    double money = 1000;
    this.thisMachine.newMap(this.file);
     this.thisMachine.selectProduct("A1",2);
     Assert.assertTrue(this.thisMachine.newMap(this.file).containsKey("A1"));
     Assert.assertTrue(this.thisMachine.newMap(this.file).get("A1") > 2);
     Assert.assertTrue(this.thisMachine.itemPrice("A1",this.file)< money);
     Assert.assertEquals(3,this.thisMachine.newMap(this.file).get("A1") - 2);
     double thisDouble = Math.round(this.thisMachine.itemPrice("A1",this.file)*100*2);
     Assert.assertEquals(3.90,(money - thisDouble)/100,0.00000);
    }

    @Test
    public void testGetChangeValues(){


    String actual = this.thisMachine.getChange(3.90);

    int numberOfQuarters = 15;
    int numberOfDimes = 1;
    int numberOfNickels = 1;
    double totalQuarterAmount = 3.75;
    double totalDimeAmount = 0.10;
    double totalNickelAmount = 0.05;
    double currentMoney = 0.00;
    String expected = numberOfQuarters + " " + String.format("%.2f",totalQuarterAmount) + " " + numberOfDimes + " " + String.format("%.2f",totalDimeAmount) +
                      " " + numberOfNickels + " " + String.format("%.2f",totalNickelAmount) + " " + String.format("%.2f",currentMoney);
    Assert.assertEquals(expected,actual);

    }
    @Test
    public void testGetChangeValues2(){


    String actual = this.thisMachine.getChange(.20);

    int numberOfQuarters = 0;
    int numberOfDimes = 2;
    int numberOfNickels = 0;
    double totalQuarterAmount = 0.00;
    double totalDimeAmount = 0.20;
    double totalNickelAmount = 0.00;
    double currentMoney = 0.00;
    String expected = numberOfQuarters + " " + String.format("%.2f",totalQuarterAmount) + " " + numberOfDimes + " " + String.format("%.2f",totalDimeAmount) +
                          " " + numberOfNickels + " " + String.format("%.2f",totalNickelAmount) + " " + String.format("%.2f",currentMoney);
        Assert.assertEquals(expected,actual);

    }
    @Test
    public void testGetChangeValues3(){


     String actual = this.thisMachine.getChange(.05);

     int numberOfQuarters = 0;
     int numberOfDimes = 0;
     int numberOfNickels = 1;
     double totalQuarterAmount = 0.00;
     double totalDimeAmount = 0.00;
     double totalNickelAmount = 0.05;
     double currentMoney = 0.00;
     String expected = numberOfQuarters + " " + String.format("%.2f",totalQuarterAmount) + " " + numberOfDimes + " " + String.format("%.2f",totalDimeAmount) +
                       " " + numberOfNickels + " " + String.format("%.2f",totalNickelAmount) + " " + String.format("%.2f",currentMoney);
     Assert.assertEquals(expected,actual);

    }

    @Test
    public void mapIsInitializedProperly(){
    File newFile = new File("vendingmachine.csv");
    Map<String,Integer> actualMap = this.thisMachine.newMap(this.file);
    Map<String,Integer> expectedMap = this.thisMachine.newMap(newFile);

    Assert.assertEquals(actualMap,expectedMap);
    }

    @Test
    public void doesMethodReturnPrice(){
     double thisPrice = thisMachine.itemPrice("A1",thisMachine.getVendingFile());
     Assert.assertEquals(thisPrice,3.05,0.00);
     thisPrice = thisMachine.itemPrice("D4",thisMachine.getVendingFile());
     Assert.assertEquals(thisPrice,0.75,0.00);
    }

    @Test
    public void doesMethodReturnFoodString(){
    String thisString = thisMachine.itemOutput("A1",thisMachine.getVendingFile());
    Assert.assertEquals(thisString,"Crunch Crunch Yum",thisString);
    thisString = thisMachine.itemOutput("D4",thisMachine.getVendingFile());
    Assert.assertEquals(thisString,"Chew Chew Yum",thisString);
    }

    @Test
    public void doesMethodReturnName(){
     String thisString = thisMachine.itemName("A1",thisMachine.getVendingFile());
     Assert.assertEquals(thisString,"Potato Crisps",thisString);
     thisString = thisMachine.itemName("D4",thisMachine.getVendingFile());
     Assert.assertEquals(thisString,"Triplemint",thisString);
     }










}