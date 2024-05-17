import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Person;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MyNetworkTest {

    @Test
    public void queryTripleSum() {
        long seed = System.currentTimeMillis();
        MyNetwork myNetwork = new MyNetwork();
        MyNetwork myShadowNetwork = new MyNetwork();

        int tripSum=0;
        int tripleSum=0;

        Person[] pastPersons = myShadowNetwork.getPersons();
        for(int i=0;i<pastPersons.length;i++){
            for(int j=i+1;j<pastPersons.length;j++){
                for(int k=j+1;k< pastPersons.length;k++){
                    if(pastPersons[i].isLinked(pastPersons[j])&&pastPersons[j].isLinked(pastPersons[k])&&pastPersons[k].isLinked(pastPersons[i])){
                        tripSum++;
                    }
                }
            }
        }
        tripleSum=myNetwork.queryTripleSum();
        assertEquals(tripSum, tripleSum);
        Person[] nowPersons=myNetwork.getPersons();

        assertEquals (pastPersons.length,nowPersons.length);
        for(int i=0;i<pastPersons.length;i++){
            assertTrue (((MyPerson)nowPersons[i]).strictEquals(pastPersons[i]));
        }



        Random random = new Random(seed);

        for (int i = 0; i < 100; i++) {
            try {
                myNetwork.addPerson(new MyPerson(i, "Person-" + i, 20));
                myShadowNetwork.addPerson(new MyPerson(i, "Person-" + i, 20));
            }catch (EqualPersonIdException ignored){

            }
        }
        int times = 300;
        for (int m = 0; m < times; m++) {
            if(m==0){
                tripSum=0;
                tripleSum=0;
                pastPersons = myShadowNetwork.getPersons();
                for(int i=0;i<pastPersons.length;i++){
                    for(int j=i+1;j<pastPersons.length;j++){
                        for(int k=j+1;k< pastPersons.length;k++){
                            if(pastPersons[i].isLinked(pastPersons[j])&&pastPersons[j].isLinked(pastPersons[k])&&pastPersons[k].isLinked(pastPersons[i])){
                                tripSum++;
                            }
                        }
                    }
                }
                tripleSum=myNetwork.queryTripleSum();
                assertEquals(tripSum, tripleSum);
                nowPersons=myNetwork.getPersons();

                assertEquals (pastPersons.length,nowPersons.length);
                for(int i=0;i<pastPersons.length;i++){
                    assertTrue (((MyPerson)nowPersons[i]).strictEquals(pastPersons[i]));
                }

            }



            int index1 = random.nextInt(100);
            int index2 = random.nextInt(100);
            try{
                myNetwork.addRelation(index1,index2,1);
                myShadowNetwork.addRelation(index1,index2,1);
            }catch (EqualRelationException|PersonIdNotFoundException ignored){

            }
            tripSum=0;
            tripleSum=0;
            pastPersons = myShadowNetwork.getPersons();
            for(int i=0;i<pastPersons.length;i++){
                for(int j=i+1;j<pastPersons.length;j++){
                    for(int k=j+1;k<pastPersons.length;k++){
                        if(pastPersons[i].isLinked(pastPersons[j])&&pastPersons[j].isLinked(pastPersons[k])&&pastPersons[k].isLinked(pastPersons[i])){
                            tripSum++;
                        }
                    }
                }
            }
            tripleSum=myNetwork.queryTripleSum();
            assertEquals(tripSum, tripleSum);
            nowPersons=myNetwork.getPersons();

            assertEquals (pastPersons.length,nowPersons.length);
            for(int i=0;i<pastPersons.length;i++){
                assertTrue (((MyPerson)nowPersons[i]).strictEquals(pastPersons[i]));
            }

        }


        MyNetwork myNetwork1=new MyNetwork();
        MyNetwork myShadowNetwork1=new MyNetwork();

        for(int i=0;i<30;i++){
            try{
                myNetwork1.addPerson(new MyPerson(i,"1Person-"+i,20));
                myShadowNetwork1.addPerson(new MyPerson(i,"1Person-"+i,20));
            }catch (EqualPersonIdException ignored){

            }

        }
        for(int i=0;i<30;i++){
            for(int j=i+1;j<30;j++){
                try{
                    myNetwork1.addRelation(i,j,1);
                    myShadowNetwork1.addRelation(i,j,1);
                }catch (EqualRelationException|PersonIdNotFoundException ignored){

                }
            }
        }
        tripSum=0;
        tripleSum=0;
        pastPersons = myShadowNetwork1.getPersons();
        for(int i=0;i<pastPersons.length;i++){
            for(int j=i+1;j<pastPersons.length;j++){
                for(int k=j+1;k<pastPersons.length;k++){
                    if(pastPersons[i].isLinked(pastPersons[j])&&pastPersons[j].isLinked(pastPersons[k])&&pastPersons[k].isLinked(pastPersons[i])){
                        tripSum++;
                    }
                }
            }
        }
        tripleSum=myNetwork1.queryTripleSum();
        assertEquals(tripSum, tripleSum);
        nowPersons=myNetwork1.getPersons();

        assertEquals (pastPersons.length,nowPersons.length);
        for(int i=0;i<pastPersons.length;i++){
            assertTrue (((MyPerson)nowPersons[i]).strictEquals(pastPersons[i]));
        }


        MyNetwork myNetwork2=new MyNetwork();
        MyNetwork myShadowNetwork2=new MyNetwork();
        try{
            myNetwork2.addPerson(new MyPerson(0,"0",20));
            myNetwork2.addPerson(new MyPerson(1,"1",20));
            myNetwork2.addPerson(new MyPerson(2,"2",20));
            myShadowNetwork2.addPerson(new MyPerson(0,"0",20));
            myShadowNetwork2.addPerson(new MyPerson(1,"1",20));
            myShadowNetwork2.addPerson(new MyPerson(2,"2",20));
        }catch (EqualPersonIdException ignored){

        }

        try{
            myNetwork2.addRelation(0,1,1);
            myNetwork2.addRelation(1,2,1);
            myNetwork2.addRelation(2,0,1);

            myShadowNetwork2.addRelation(0,1,1);
            myShadowNetwork2.addRelation(1,2,1);
            myShadowNetwork2.addRelation(2,0,1);
        }catch (EqualRelationException|PersonIdNotFoundException ignored){

        }

        tripSum=0;
        tripleSum=0;
        pastPersons = myShadowNetwork2.getPersons();
        for(int i=0;i<pastPersons.length;i++){
            for(int j=i+1;j<pastPersons.length;j++){
                for(int k=j+1;k<pastPersons.length;k++){
                    if(pastPersons[i].isLinked(pastPersons[j])&&pastPersons[j].isLinked(pastPersons[k])&&pastPersons[k].isLinked(pastPersons[i])){
                        tripSum++;
                    }
                }
            }
        }
        tripleSum=myNetwork2.queryTripleSum();
        assertEquals(tripSum, tripleSum);
        nowPersons=myNetwork2.getPersons();

        assertEquals (pastPersons.length,nowPersons.length);
        for(int i=0;i<pastPersons.length;i++){
            assertTrue (((MyPerson)nowPersons[i]).strictEquals(pastPersons[i]));
        }
        /*
        MyNetwork myNetwork = new MyNetwork();
        MyNetwork myShadowNetwork=new MyNetwork();

        MyNetwork myNetwork1 = new MyNetwork();
        MyNetwork myShadowNetwork1=new MyNetwork();

        MyNetwork myNetwork2 = new MyNetwork();
        MyNetwork myShadowNetwork2=new MyNetwork();

        MyNetwork myNetwork3 = new MyNetwork();
        MyNetwork myShadowNetwork3=new MyNetwork();

        MyNetwork myNetwork4 = new MyNetwork();
        MyNetwork myShadowNetwork4=new MyNetwork();


        int tripSum=0;
        int tripSum1=0;
        int tripSum2=0;
        int tripSum3=0;
        int tripSum4=0;

        int tripleSum=0;
        int tripleSum1=0;
        int tripleSum2=0;
        int tripleSum3=0;
        int tripleSum4=0;

        /////////////没人////////////////////////////
        Person[] pastPersons = myShadowNetwork.getPersons();
        for(int i=0;i<pastPersons.length;i++){
            for(int j=i+1;j<pastPersons.length;j++){
                for(int k=j+1;k<pastPersons.length;k++){
                    if(pastPersons[i].isLinked(pastPersons[j])&&pastPersons[j].isLinked(pastPersons[k])&&pastPersons[k].isLinked(pastPersons[i])){
                        tripSum++;
                    }
                }
            }
        }
        tripleSum=myNetwork.queryTripleSum();
        Person[] nowPersons=myNetwork.getPersons();

        assertEquals (pastPersons.length,nowPersons.length);
        for(int i=0;i<nowPersons.length;i++){
            assert (((MyPerson)nowPersons[i]).strictEquals(pastPersons[i]));
        }
        assertEquals(tripSum, tripleSum);
        /////////////////////////////////////////////////////////////////

        /////////////////有人没关系///////////////////////////////////////////////
        for(int i=0;i<10;i++){
            myNetwork1.addPerson(new MyPerson(i,"Person-"+i,i+1));
            myShadowNetwork1.addPerson(new MyPerson(i,"Person-"+i,i+1));
        }
        Person[] pastPersons1 = myShadowNetwork1.getPersons();
        for(int i=0;i<pastPersons1.length;i++){
            for(int j=i+1;j<pastPersons1.length;j++){
                for(int k=j+1;k<pastPersons1.length;k++){
                    if(pastPersons1[i].isLinked(pastPersons1[j])&&pastPersons1[j].isLinked(pastPersons1[k])&&pastPersons1[k].isLinked(pastPersons1[i])){
                        tripSum1++;
                    }
                }
            }
        }
        tripleSum1=myNetwork1.queryTripleSum();
        Person[] nowPersons1=myNetwork1.getPersons();

        assertEquals (pastPersons1.length,nowPersons1.length);
        for(int i=0;i<nowPersons1.length;i++){
            assert (((MyPerson)nowPersons1[i]).strictEquals(pastPersons1[i]));
        }
        assertEquals(tripSum1, tripleSum1);
        //////////////////////////////////////////////////////////////////

        /////////////////有人有关系无三角关系///////////////////////////////////////////////////
        for(int i=0;i<10;i++){
            myNetwork2.addPerson(new MyPerson(i,"Person-"+i,i+1));
            myShadowNetwork2.addPerson(new MyPerson(i,"Person-"+i,i+1));
        }
        for(int i=0;i<9;i++){
            myNetwork2.addRelation(i,i+1,i+1);
            myShadowNetwork2.addRelation(i,i+1,i+1);
        }
        Person[] pastPersons2 = myShadowNetwork2.getPersons();
        for(int i=0;i<pastPersons2.length;i++){
            for(int j=i+1;j<pastPersons2.length;j++){
                for(int k=j+1;k<pastPersons2.length;k++){
                    if(pastPersons2[i].isLinked(pastPersons2[j])&&pastPersons2[j].isLinked(pastPersons2[k])&&pastPersons2[k].isLinked(pastPersons2[i])){
                        tripSum2++;
                    }
                }
            }
        }
        tripleSum2=myNetwork2.queryTripleSum();
        Person[] nowPersons2=myNetwork2.getPersons();

        assertEquals (pastPersons2.length,nowPersons2.length);
        for(int i=0;i<nowPersons2.length;i++){
            assert (((MyPerson)nowPersons2[i]).strictEquals(pastPersons2[i]));
        }
        assertEquals(tripSum2, tripleSum2);
        ///////////////////////////////////////////////////////////////////

        //////////////基础三角///////////////////////////////////////////////////
        myNetwork3.addPerson(new MyPerson(0,"0",1));
        myNetwork3.addPerson(new MyPerson(1,"1",1));
        myNetwork3.addPerson(new MyPerson(2,"2",1));
        myNetwork3.addRelation(0,1,1);
        myNetwork3.addRelation(1,2,1);
        myNetwork3.addRelation(2,0,1);

        myShadowNetwork3.addPerson(new MyPerson(0,"0",1));
        myShadowNetwork3.addPerson(new MyPerson(1,"1",1));
        myShadowNetwork3.addPerson(new MyPerson(2,"2",1));
        myShadowNetwork3.addRelation(0,1,1);
        myShadowNetwork3.addRelation(1,2,1);
        myShadowNetwork3.addRelation(2,0,1);

        Person[] pastPersons3 = myShadowNetwork3.getPersons();
        for(int i=0;i<pastPersons3.length;i++){
            for(int j=i+1;j<pastPersons3.length;j++){
                for(int k=j+1;k<pastPersons3.length;k++){
                    if(pastPersons3[i].isLinked(pastPersons3[j])&&pastPersons3[j].isLinked(pastPersons3[k])&&pastPersons3[k].isLinked(pastPersons3[i])){
                        tripSum3++;
                    }
                }
            }
        }
        tripleSum3=myNetwork3.queryTripleSum();
        Person[] nowPersons3=myNetwork3.getPersons();

        assertEquals (pastPersons3.length,nowPersons3.length);
        for(int i=0;i<nowPersons3.length;i++){
            assert (((MyPerson)nowPersons3[i]).strictEquals(pastPersons3[i]));
        }
        assertEquals(tripSum3, tripleSum3);
        ////////////////////////////////////////////////////////////////

        /////////////////全关系///////////////////////////////////////////////
        for(int i=0;i<10;i++){
            myNetwork4.addPerson(new MyPerson(i,"Person-"+i,i+1));
            myShadowNetwork4.addPerson(new MyPerson(i,"Person-"+i,i+1));
        }
        for(int i=0;i<10;i++){
            for(int j=i+1;j<10;j++){
                myNetwork4.addRelation(i,j,1);
                myShadowNetwork4.addRelation(i,j,1);
            }
        }
        Person[] pastPersons4 = myShadowNetwork4.getPersons();
        for(int i=0;i<pastPersons4.length;i++){
            for(int j=i+1;j<pastPersons4.length;j++){
                for(int k=j+1;k<pastPersons4.length;k++){
                    if(pastPersons4[i].isLinked(pastPersons4[j])&&pastPersons4[j].isLinked(pastPersons4[k])&&pastPersons4[k].isLinked(pastPersons4[i])){
                        tripSum4++;
                    }
                }
            }
        }
        tripleSum4=myNetwork4.queryTripleSum();
        Person[] nowPersons4=myNetwork4.getPersons();

        assertEquals (pastPersons4.length,nowPersons4.length);
        for(int i=0;i<nowPersons4.length;i++){
            assert (((MyPerson)nowPersons4[i]).strictEquals(pastPersons4[i]));
        }
        assertEquals(tripSum4, tripleSum4);
        ////////////////////////////////////////////////////////////////

        */

    }

}