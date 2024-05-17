import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import org.junit.Test;

import java.util.Objects;
import java.util.Random;

import static org.junit.Assert.*;

public class MyNetworkTest {

    @Test
    public void deleteColdEmoji() {

        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        int times = 3;

        for (int m = 0; m < times; m++) {
            MyNetwork myNetwork = new MyNetwork();
            MyNetwork myShadowNetwork = new MyNetwork();
            //50个人全连接
            for (int i = 0; i < 50; i++) {
                try {
                    myNetwork.addPerson(new MyPerson(i, "Person-" + i, 20));
                    myShadowNetwork.addPerson(new MyPerson(i, "Person-" + i, 20));
                } catch (Exception ignored) {

                }
            }
            for (int i = 0; i < 50; i++) {
                for (int j = i + 1; j < 50; j++) {
                    try {
                        myNetwork.addRelation(i, j, 1);
                        myShadowNetwork.addRelation(i, j, 1);
                    } catch (Exception ignored) {

                    }
                }
            }
            int limit = random.nextInt(10);
            for(int k=0;k<50;k++){
                try {
                    myNetwork.storeEmojiId(k);
                    myShadowNetwork.storeEmojiId(k);
                } catch (Exception ignored) {

                }
            }
            for (int k = 0; k < 1000; k++) {
                int id=random.nextInt(50);
                int messageId = k;
                int redMessageId = k + 1000;
                int noticeMessageId = k + 2000;
                int luckyMoney = random.nextInt(200);
                int personId1 = random.nextInt(50);
                int personId2 = random.nextInt(50);
                MyPerson person1 = (MyPerson) myNetwork.getPerson(personId1);
                MyPerson person2 = (MyPerson) myNetwork.getPerson(personId2);
                MyPerson person3 = (MyPerson) myShadowNetwork.getPerson(personId1);
                MyPerson person4 = (MyPerson) myShadowNetwork.getPerson(personId2);
                //

                try {
                    myNetwork.addMessage(new MyEmojiMessage(messageId, id, person1, person2));
                    myShadowNetwork.addMessage(new MyEmojiMessage(messageId, id, person3, person4));
                    myNetwork.addMessage(new MyNoticeMessage(noticeMessageId, "Notice" + noticeMessageId, person1, person2));
                    myShadowNetwork.addMessage(new MyNoticeMessage(noticeMessageId, "Notice" + noticeMessageId, person3, person4));
                    myNetwork.addMessage(new MyRedEnvelopeMessage(redMessageId, luckyMoney, person1, person2));
                    myShadowNetwork.addMessage(new MyRedEnvelopeMessage(redMessageId, luckyMoney, person3, person4));
                } catch (Exception e) {
                    continue;
                }

            }

            for (int n = 0; n < 1000; n++) {
                int messageId = random.nextInt(3000);
                try {
                    myNetwork.sendMessage(messageId);
                    myShadowNetwork.sendMessage(messageId);

                } catch (Exception e) {
                    continue;
                }
            }

            int[] oldEmojiIdList = myShadowNetwork.getEmojiIdList();
            int[] oldEmojiHeatList = myShadowNetwork.getEmojiHeatList();
            Message[] oldMessages = myShadowNetwork.getMessages();
            assertEquals(oldEmojiHeatList.length, oldEmojiIdList.length);
            int lenth = myNetwork.deleteColdEmoji(limit);
            Message[] nowMessages = myNetwork.getMessages();
            int[] nowEmojiIdList = myNetwork.getEmojiIdList();
            int[] nowEmojiHeatList = myNetwork.getEmojiHeatList();
            assertEquals(nowEmojiIdList.length, nowEmojiHeatList.length);
            int num = 0;
            for (int j = 0; j < oldEmojiHeatList.length; j++) {
                if (oldEmojiHeatList[j] >= limit) {
                    num++;
                    assertTrue(hasEmojiId(oldEmojiIdList[j], nowEmojiIdList));
                    assertTrue(hasEmojiHeat(oldEmojiHeatList[j], nowEmojiHeatList));
                }
            }
            assertEquals(num, nowEmojiIdList.length);
            assertEquals(lenth, num);
            for (int j = 0; j < nowEmojiIdList.length; j++) {
                assertTrue(hasEmojiId(nowEmojiIdList[j], oldEmojiIdList));
            }
            for (int j = 0; j < nowEmojiHeatList.length; j++) {
                assertTrue(hasEmojiHeat(nowEmojiHeatList[j], oldEmojiHeatList));
            }
            int messageNum = 0;
            for (int j = 0; j < oldMessages.length; j++) {
                if(oldMessages[j] instanceof MyEmojiMessage){
                    if(myNetwork.containsEmojiId(((MyEmojiMessage)oldMessages[j]).getEmojiId())){
                        assertTrue(hasMessage(oldMessages[j],nowMessages));
                    }else {
                        assertTrue(!hasMessage(oldMessages[j],nowMessages));
                    }
                }else {
                    assertTrue(hasMessage(oldMessages[j],nowMessages));
                }
                assertTrue((!(oldMessages[j] instanceof MyEmojiMessage && myNetwork.containsEmojiId(((EmojiMessage) oldMessages[j]).getEmojiId()))) || hasMessage(oldMessages[j], nowMessages));
                assertTrue(oldMessages[j] instanceof MyEmojiMessage || hasMessage(oldMessages[j], nowMessages));
            }
            for (int j = 0; j < oldMessages.length; j++) {
                if ((!(oldMessages[j] instanceof MyEmojiMessage)) || myNetwork.containsEmojiId(((EmojiMessage) oldMessages[j]).getEmojiId())) {
                    messageNum++;
                }
            }
            assertEquals(nowMessages.length, messageNum);


        }
    }

    public boolean hasEmojiId(int id, int[] List) {

        for (int i = 0; i < List.length; i++) {
            if (List[i] == id) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEmojiHeat(int heat, int[] List) {
        for (int i = 0; i < List.length; i++) {
            if (List[i] == heat) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMessage(Message message, Message[] messages) {
        for (int i = 0; i < messages.length; i++) {
            if (messageEquals(message, messages[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean messageEquals(Message message1, Message message2) {
        if(message1.getTag()==null){
            if(message1.getId()!=message2.getId()){
                return false;
            }
            if(message2.getTag()!=null){
                return false;
            }
            if(!message1.getPerson1().equals(message2.getPerson1())){
                return false;
            }
            if(!message1.getPerson2().equals(message2.getPerson2())){
                return false;
            }
            if(message1.getSocialValue()!=message2.getSocialValue()){
                return false;
            }

        }else {
            if(message1.getId()!=message2.getId()){
                return false;
            }
            if(!message1.getTag().equals(message2.getTag())){
                return false;
            }
            if(!message1.getPerson1().equals(message2.getPerson1())){
                return false;
            }
            if(!message1.getPerson2().equals(message2.getPerson2())){
                return false;
            }
            if(message1.getSocialValue()!=message2.getSocialValue()){
                return false;
            }

        }
        if(message1 instanceof MyEmojiMessage){
            if(!(message2 instanceof MyEmojiMessage)){
                return false;
            }
            if(((MyEmojiMessage)message1).getEmojiId()!=((MyEmojiMessage)message2).getEmojiId()){
                return false;
            }
        }else if(message1 instanceof MyRedEnvelopeMessage){
            if(!(message2 instanceof  MyRedEnvelopeMessage)){
                return false;
            }
            if(((MyRedEnvelopeMessage)message1).getMoney()!=((MyRedEnvelopeMessage)message2).getMoney()){
                return false;
            }
        }else if(message1 instanceof MyNoticeMessage){
            if(!(message2 instanceof MyNoticeMessage)){
                return false;
            }
            if(!Objects.equals(((MyNoticeMessage) message1).getString(), ((MyNoticeMessage) message2).getString())){
                return false;
            }

        }
        return true;

    }
}