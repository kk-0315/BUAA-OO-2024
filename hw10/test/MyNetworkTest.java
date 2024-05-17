import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.main.Person;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.*;

public class MyNetworkTest {

    @Test
    public void queryCoupleSum() {
        long seed = System.currentTimeMillis();
        MyNetwork myNetwork = new MyNetwork();
        MyNetwork myShadowNetwork = new MyNetwork();

        //无人
        int coupleSum = 0;
        int rightCoupleSum = 0;
        Person[] pastPersons = myShadowNetwork.getPersons();
        for (int i = 0; i < pastPersons.length; i++) {
            int acqId;
            try{
                acqId = myShadowNetwork.queryBestAcquaintance(pastPersons[i].getId());
            }catch (PersonIdNotFoundException| AcquaintanceNotFoundException e){
                continue;
            }
            int acqId1;
            try{
                acqId1 = myShadowNetwork.queryBestAcquaintance(acqId);
            }catch (PersonIdNotFoundException| AcquaintanceNotFoundException e){
                continue;
            }
            if (acqId1 == pastPersons[i].getId()) {
                rightCoupleSum++;
            }
        }
        rightCoupleSum /= 2;
        coupleSum = myNetwork.queryCoupleSum();
        assertEquals(rightCoupleSum, coupleSum);
        Person[] nowPersons = myNetwork.getPersons();
        assertEquals(pastPersons.length, nowPersons.length);
        for (int i = 0; i < pastPersons.length; i++) {
            assertTrue(((MyPerson) nowPersons[i]).strictEquals(pastPersons[i]));
        }


        Random random = new Random(seed);

        for (int i = 0; i < 100; i++) {
            try {
                myNetwork.addPerson(new MyPerson(i, "Person-" + i, 20));
                myShadowNetwork.addPerson(new MyPerson(i, "Person-" + i, 20));
            } catch (EqualPersonIdException ignored) {

            }
        }
        int times = 300;
        for (int m = 0; m < times; m++) {
            //有人无关系
            if (m == 0) {
                coupleSum = 0;
                rightCoupleSum = 0;
                pastPersons = myShadowNetwork.getPersons();
                for (int i = 0; i < pastPersons.length; i++) {
                    int acqId;
                    try{
                        acqId = myShadowNetwork.queryBestAcquaintance(pastPersons[i].getId());
                    }catch (PersonIdNotFoundException| AcquaintanceNotFoundException e){
                        continue;
                    }
                    int acqId1;
                    try{
                        acqId1 = myShadowNetwork.queryBestAcquaintance(acqId);
                    }catch (PersonIdNotFoundException| AcquaintanceNotFoundException e){
                        continue;
                    }
                    if (acqId1 == pastPersons[i].getId()) {
                        rightCoupleSum++;
                    }
                }
                rightCoupleSum /= 2;
                coupleSum = myNetwork.queryCoupleSum();
                assertEquals(rightCoupleSum, coupleSum);
                nowPersons = myNetwork.getPersons();
                assertEquals(pastPersons.length, nowPersons.length);
                for (int i = 0; i < pastPersons.length; i++) {
                    assertTrue(((MyPerson) nowPersons[i]).strictEquals(pastPersons[i]));
                }
            }

            int index1 = random.nextInt(100);
            int index2 = random.nextInt(100);
            //value在-100~100之间
            int value = random.nextInt(100);
            int minusFlag = random.nextInt(2);
            if (minusFlag == 1) {
                value *= -1;
            }
            try {
                myNetwork.addRelation(index1, index2, value);
                myShadowNetwork.addRelation(index1, index2, value);
            } catch (EqualRelationException | PersonIdNotFoundException ignored) {

            }
            coupleSum = 0;
            rightCoupleSum = 0;
            pastPersons = myShadowNetwork.getPersons();
            for (int i = 0; i < pastPersons.length; i++) {
                int acqId;
                try{
                    acqId = myShadowNetwork.queryBestAcquaintance(pastPersons[i].getId());
                }catch (PersonIdNotFoundException| AcquaintanceNotFoundException e){
                    continue;
                }
                int acqId1;
                try{
                    acqId1 = myShadowNetwork.queryBestAcquaintance(acqId);
                }catch (PersonIdNotFoundException| AcquaintanceNotFoundException e){
                    continue;
                }
                if (acqId1 == pastPersons[i].getId()) {
                    rightCoupleSum++;
                }
            }
            rightCoupleSum /= 2;
            coupleSum = myNetwork.queryCoupleSum();
            assertEquals(rightCoupleSum, coupleSum);
            nowPersons = myNetwork.getPersons();
            assertEquals(pastPersons.length, nowPersons.length);
            for (int i = 0; i < pastPersons.length; i++) {
                assertTrue(((MyPerson) nowPersons[i]).strictEquals(pastPersons[i]));
            }
        }

        //全连接
        MyNetwork myNetwork1 = new MyNetwork();
        MyNetwork myShadowNetwork1 = new MyNetwork();

        for (int i = 0; i < 30; i++) {
            try {
                myNetwork1.addPerson(new MyPerson(i, "1Person-" + i, 20));
                myShadowNetwork1.addPerson(new MyPerson(i, "1Person-" + i, 20));
            } catch (EqualPersonIdException ignored) {

            }

        }
        for (int i = 0; i < 30; i++) {
            for (int j = i + 1; j < 30; j++) {
                //value在-30到30之间
                int value1 = random.nextInt(30);
                int minus = random.nextInt(2);
                if (minus == 1) {
                    value1 *= -1;
                }
                try {
                    myNetwork1.addRelation(i, j, value1);
                    myShadowNetwork1.addRelation(i, j, value1);
                } catch (EqualRelationException | PersonIdNotFoundException ignored) {

                }
            }
        }
        coupleSum = 0;
        rightCoupleSum = 0;
        pastPersons = myShadowNetwork1.getPersons();
        for (int i = 0; i < pastPersons.length; i++) {
            int acqId;
            try{
                acqId = myShadowNetwork1.queryBestAcquaintance(pastPersons[i].getId());
            }catch (PersonIdNotFoundException| AcquaintanceNotFoundException e){
                continue;
            }
            int acqId1;
            try{
                acqId1 = myShadowNetwork1.queryBestAcquaintance(acqId);
            }catch (PersonIdNotFoundException| AcquaintanceNotFoundException e){
                continue;
            }
            if (acqId1 == pastPersons[i].getId()) {
                rightCoupleSum++;
            }
        }
        rightCoupleSum /= 2;
        coupleSum = myNetwork1.queryCoupleSum();
        assertEquals(rightCoupleSum, coupleSum);
        nowPersons = myNetwork1.getPersons();
        assertEquals(pastPersons.length, nowPersons.length);
        for (int i = 0; i < pastPersons.length; i++) {
            assertTrue(((MyPerson) nowPersons[i]).strictEquals(pastPersons[i]));
        }

    }

}