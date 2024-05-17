import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.EqualTagIdException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.TagIdNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.Tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> persons;
    private DisjointSet disjointSet;
    private static Counter counter;
    private static int blockSum;
    private static int tripleSum;
    private HashMap<Integer, Message> messages;
    private HashMap<Integer, Integer> emojis;//emoji.id+emoji.heat

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.disjointSet = new DisjointSet();
        blockSum = 0;
        tripleSum = 0;
        counter = new Counter();
        emojis = new HashMap<>();
        messages = new HashMap<>();
    }

    @Override
    public boolean containsPerson(int id) {
        if (persons.containsKey(id)) { return true; }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        if (containsPerson(id)) { return persons.get(id); }
        else { return null; }
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (!containsPerson(person.getId())) {
            persons.put(person.getId(), person);
            disjointSet.add(person.getId());
            blockSum++;
        } else { throw new MyEqualPersonIdException(person.getId(), counter); }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (containsPerson(id1) && containsPerson(id2)
                && !getPerson(id1).isLinked(getPerson(id2))) {
            updateTripleSum(id1, id2);
            updateBlockSum(id1, id2);
            disjointSet.merge(id1, id2);
            ((MyPerson) getPerson(id1)).addLink(getPerson(id2));
            ((MyPerson) getPerson(id2)).addLink(getPerson(id1));
            ((MyPerson) getPerson(id1)).addValue(value, getPerson(id2).getId());
            ((MyPerson) getPerson(id2)).addValue(value, getPerson(id1).getId());
            addTagValue(id1, id2, value);
            changeBestAcquaintanceId(id2, id1, value);
            changeBestAcquaintanceId(id1, id2, value);
        } else if (!containsPerson(id1) || !containsPerson(id2)
                || getPerson(id1).isLinked(getPerson(id2))) {
            if (!containsPerson(id1)) { throw new MyPersonIdNotFoundException(id1, counter); }
            else if (!containsPerson(id2)) { throw new MyPersonIdNotFoundException(id2, counter); }
            else if (getPerson(id1).isLinked(getPerson(id2))) {
                throw new MyEqualRelationException(id1, id2, counter); }
        }
    }

    private void changeBestAcquaintanceId(int id1, int id2, int value) {
        if (((MyPerson) getPerson(id2)).getBestAcquaintanceId() == id2) {
            ((MyPerson) getPerson(id2)).setBestAcquaintanceId(id1);
        } else if (value >= getPerson(id2).queryValue(getPerson(((MyPerson) getPerson(id2))
                .getBestAcquaintanceId()))) {
            if (value == getPerson(id2).queryValue(getPerson(((MyPerson) getPerson(id2))
                    .getBestAcquaintanceId()))) {
                ((MyPerson) getPerson(id2)).setBestAcquaintanceId(Math.min(id1,
                        ((MyPerson) getPerson(id2)).getBestAcquaintanceId()));
            } else { ((MyPerson) getPerson(id2)).setBestAcquaintanceId(id1); }
        }
    }

    @Override
    public void modifyRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (containsPerson(id1) && containsPerson(id2)
                && id1 != id2 && getPerson(id1).isLinked(getPerson(id2))
                && getPerson(id1).queryValue(getPerson(id2)) + value > 0) {
            if (value > 0) {
                int nowValue = getPerson(id1).queryValue(getPerson(id2)) + value;
                changeBestAcquaintanceId(id1, id2, nowValue);
                changeBestAcquaintanceId(id2, id1, nowValue); }
            ((MyPerson) getPerson(id1)).modifyValue(value, id2);
            ((MyPerson) getPerson(id2)).modifyValue(value, id1);
            addTagValue(id1, id2, value);
            if (value < 0) {
                if (((MyPerson) getPerson(id1)).getBestAcquaintanceId() == id2) {
                    ((MyPerson) getPerson(id1)).modifyBestAcquaintanceId(); }
                if (((MyPerson) getPerson(id2)).getBestAcquaintanceId() == id1) {
                    ((MyPerson) getPerson(id2)).modifyBestAcquaintanceId(); }
            }
        } else if (containsPerson(id1) && containsPerson(id2)
                && id1 != id2 && getPerson(id1).isLinked(getPerson(id2))
                && getPerson(id1).queryValue(getPerson(id2)) + value <= 0) {
            subValueSum(id1, id2);
            delPerson(id1, id2);
            ((MyPerson) getPerson(id2)).subLink(getPerson(id1));
            ((MyPerson) getPerson(id1)).subLink(getPerson(id2));
            ((MyPerson) getPerson(id2)).subValue(getPerson(id1).getId());
            ((MyPerson) getPerson(id1)).subValue(getPerson(id2).getId());
            if (((MyPerson) getPerson(id1)).getBestAcquaintanceId() == id2) {
                ((MyPerson) getPerson(id1)).modifyBestAcquaintanceId(); }
            if (((MyPerson) getPerson(id2)).getBestAcquaintanceId() == id1) {
                ((MyPerson) getPerson(id2)).modifyBestAcquaintanceId(); }
            Boolean past = disjointSet.find(id1) == disjointSet.find(id2);
            subTripleSum(id1, id2);
            disjointSet.sub((MyPerson) getPerson(id1), (MyPerson) getPerson(id2));
            addBlockSum(id1, id2, past);
        } else if (!containsPerson(id1) || !containsPerson(id2)
                || id1 == id2 || !getPerson(id1).isLinked(getPerson(id2))) {
            if (!containsPerson(id1)) {
                throw new MyPersonIdNotFoundException(id1, counter);
            } else if (!containsPerson(id2)) {
                throw new MyPersonIdNotFoundException(id2, counter);
            } else if (id1 == id2) {
                throw new MyEqualPersonIdException(id1, counter);
            } else if (!getPerson(id1).isLinked(getPerson(id2))) {
                throw new MyRelationNotFoundException(id1, id2, counter); }
        }
    }

    public void delPerson(int id1, int id2) {
        for (Integer key : ((MyPerson) getPerson(id1)).getTags().keySet()) {
            if (getPerson(id1).getTag(key).hasPerson(getPerson(id2))) {
                getPerson(id1).getTag(key).delPerson(getPerson(id2)); }
        }
        for (Integer key : ((MyPerson) getPerson(id2)).getTags().keySet()) {
            if (getPerson(id2).getTag(key).hasPerson(getPerson(id1))) {
                getPerson(id2).getTag(key).delPerson(getPerson(id1)); }
        }
    }

    public void addTagValue(int id1, int id2, int value) {
        for (Integer key : persons.keySet()) {
            if (persons.get(key).isLinked(getPerson(id1)) &&
                    persons.get(key).isLinked(getPerson(id2))) {
                for (Integer key1 : ((MyPerson) persons.get(key)).getTags().keySet()) {
                    MyTag myTag = (MyTag) ((MyPerson) persons.get(key)).getTags().get(key1);
                    if (myTag.hasPerson(getPerson(id1)) && myTag.hasPerson(getPerson(id2))) {
                        myTag.addValueSum(2 * value); }
                }
            }
        }
    }

    public void subValueSum(int id1, int id2) {
        for (Integer key : persons.keySet()) {
            if (persons.get(key).isLinked(getPerson(id1)) &&
                    persons.get(key).isLinked(getPerson(id2))) {
                for (Integer key1 : ((MyPerson) persons.get(key)).getTags().keySet()) {
                    MyTag myTag = (MyTag) ((MyPerson) persons.get(key)).getTags().get(key1);
                    if (myTag.hasPerson(getPerson(id1)) && myTag.hasPerson(getPerson(id2))) {
                        myTag.addValueSum(-2 * getPerson(id1).queryValue(getPerson(id2))); } } } } }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (containsPerson(id1) && containsPerson(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else if (!containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1, counter);
        } else if (!containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2, counter);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2, counter); }
        return -1; }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (containsPerson(id1) && containsPerson(id2)) {
            return (disjointSet.find(id1) == disjointSet.find(id2));
        } else if (!containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1, counter);
        } else if (!containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2, counter); }
        return false; }

    @Override
    public int queryBlockSum() {
        return blockSum;
    }

    @Override
    public int queryTripleSum() {
        return tripleSum;
    }

    @Override
    public void addTag(int personId, Tag tag) throws
            PersonIdNotFoundException, EqualTagIdException {
        if (containsPerson(personId) && !getPerson(personId).containsTag(tag.getId())) {
            getPerson(personId).addTag(tag);
        } else if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId, counter);
        } else if (getPerson(personId).containsTag(tag.getId())) {
            throw new MyEqualTagIdException(tag.getId(), counter); } }

    @Override
    public void addPersonToTag(int personId1, int personId2, int tagId)
            throws PersonIdNotFoundException, RelationNotFoundException,
            TagIdNotFoundException, EqualPersonIdException {
        if (containsPerson(personId1) && containsPerson(personId2) &&
                personId1 != personId2 && getPerson(personId2).isLinked(getPerson(personId1)) &&
                getPerson(personId2).containsTag(tagId) && !getPerson(personId2).getTag(tagId).
                hasPerson(getPerson(personId1)) &&
                getPerson(personId2).getTag(tagId).getSize() <= 1111) {
            getPerson(personId2).getTag(tagId).addPerson(getPerson(personId1));
        } else if (!containsPerson(personId1)) {
            throw new MyPersonIdNotFoundException(personId1, counter);
        } else if (!containsPerson(personId2)) {
            throw new MyPersonIdNotFoundException(personId2, counter);
        } else if (personId1 == personId2) {
            throw new MyEqualPersonIdException(personId1, counter);
        } else if (!getPerson(personId2).isLinked(getPerson(personId1))) {
            throw new MyRelationNotFoundException(personId1, personId2, counter);
        } else if (!getPerson(personId2).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId, counter);
        } else if (getPerson(personId2).getTag(tagId).hasPerson(getPerson(personId1))) {
            throw new MyEqualPersonIdException(personId1, counter); } }

    @Override
    public int queryTagValueSum(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (containsPerson(personId) && getPerson(personId).containsTag(tagId)) {
            return getPerson(personId).getTag(tagId).getValueSum();
        } else if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId, counter);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId, counter); }
        return -1; }

    @Override
    public int queryTagAgeVar(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (containsPerson(personId) && getPerson(personId).containsTag(tagId)) {
            return getPerson(personId).getTag(tagId).getAgeVar();
        } else if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId, counter);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId, counter); }
        return -1; }

    @Override
    public void delPersonFromTag(int personId1, int personId2, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (containsPerson(personId1) && containsPerson(personId2) &&
                getPerson(personId2).containsTag(tagId) &&
                getPerson(personId2).getTag(tagId).hasPerson(getPerson(personId1))) {
            getPerson(personId2).getTag(tagId).delPerson(getPerson(personId1));
        } else if (!containsPerson(personId1)) {
            throw new MyPersonIdNotFoundException(personId1, counter);
        } else if (!containsPerson(personId2)) {
            throw new MyPersonIdNotFoundException(personId2, counter);
        } else if (!getPerson(personId2).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId, counter);
        } else if (!getPerson(personId2).getTag(tagId).hasPerson(getPerson(personId1))) {
            throw new MyPersonIdNotFoundException(personId1, counter); } }

    @Override
    public void delTag(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (containsPerson(personId) && getPerson(personId).containsTag(tagId)) {
            getPerson(personId).delTag(tagId);
        } else if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId, counter);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId, counter); } }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EmojiIdNotFoundException, EqualPersonIdException {
        if (!containsMessage(message.getId()) && ((!(message instanceof EmojiMessage)) ||
                (containsEmojiId(((EmojiMessage) message).getEmojiId()))) &&
                (!(message.getType() == 0) || !(message.getPerson1().
                        equals(message.getPerson2())))) { messages.put(message.getId(), message); }
        else if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId(), counter);
        } else if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId(), counter);
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId(), counter); } }

    @Override
    public Message getMessage(int id) {
        if (containsMessage(id)) { return messages.get(id); }
        else { return null; } }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, TagIdNotFoundException {
        if (containsMessage(id) && getMessage(id).getType() == 0 &&
                getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()) &&
                getMessage(id).getPerson1() != getMessage(id).getPerson2()) {
            Message oldMessage = getMessage(id);
            oldMessage.getPerson1().addSocialValue(oldMessage.getSocialValue());
            oldMessage.getPerson2().addSocialValue(oldMessage.getSocialValue());
            if (oldMessage instanceof RedEnvelopeMessage) {
                oldMessage.getPerson1().addMoney(-(((RedEnvelopeMessage) oldMessage).getMoney()));
                oldMessage.getPerson2().addMoney(((RedEnvelopeMessage) oldMessage).getMoney());
            } else if (oldMessage instanceof EmojiMessage) {
                int oldValue = emojis.get(((EmojiMessage) oldMessage).getEmojiId());
                emojis.replace(((EmojiMessage) oldMessage).getEmojiId(), oldValue + 1); }
            ((MyPerson) oldMessage.getPerson2()).insertHead(getMessage(id));
            messages.remove(id);
        } else if (containsMessage(id) && getMessage(id).getType() == 1 &&
                getMessage(id).getPerson1().containsTag(getMessage(id).getTag().getId())) {
            Message oldMessage = getMessage(id);
            oldMessage.getPerson1().addSocialValue(oldMessage.getSocialValue());
            for (Integer key : ((MyTag) oldMessage.getTag()).getPersons().keySet()) {
                ((MyTag) oldMessage.getTag()).getPersons().get(key).
                        addSocialValue(oldMessage.getSocialValue()); }
            if (oldMessage instanceof RedEnvelopeMessage && oldMessage.getTag().getSize() > 0) {
                int k = ((RedEnvelopeMessage) oldMessage).
                        getMoney() / oldMessage.getTag().getSize();
                oldMessage.getPerson1().addMoney(-k * oldMessage.getTag().getSize());
                for (Integer key : ((MyTag) oldMessage.getTag()).getPersons().keySet()) {
                    ((MyTag) oldMessage.getTag()).getPersons().get(key).addMoney(k); }
            } else if (oldMessage instanceof EmojiMessage) {
                int oldValue = emojis.get(((EmojiMessage) oldMessage).getEmojiId());
                emojis.replace(((EmojiMessage) oldMessage).getEmojiId(), oldValue + 1); }
            messages.remove(id);
        } else if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id, counter);
        } else if (getMessage(id).getType() == 0 && !(getMessage(id).getPerson1().
                isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId(), counter);
        } else if (getMessage(id).getType() == 1 && !getMessage(id).getPerson1().
                containsTag(getMessage(id).getTag().getId())) {
            throw new MyTagIdNotFoundException(getMessage(id).getTag().getId(), counter); } }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (containsPerson(id)) { return getPerson(id).getSocialValue(); }
        else { throw new MyPersonIdNotFoundException(id, counter); } }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (containsPerson(id)) { return getPerson(id).getReceivedMessages(); }
        else { throw new MyPersonIdNotFoundException(id, counter); } }

    @Override
    public boolean containsEmojiId(int id) { return emojis.containsKey(id); }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (!containsEmojiId(id)) { emojis.put(id, 0); }
        else { throw new MyEqualEmojiIdException(id, counter); } }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (containsPerson(id)) { return getPerson(id).getMoney(); }
        else { throw new MyPersonIdNotFoundException(id, counter); } }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (containsEmojiId(id)) { return emojis.get(id); }
        else {  throw new MyEmojiIdNotFoundException(id, counter); } }

    @Override
    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer, Integer>> it = emojis.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            if ((entry.getValue()) < limit) {
                it.remove(); } }
        Iterator<Map.Entry<Integer,Message>> it1 = messages.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<Integer, Message> entry1 = it1.next();
            if ((entry1.getValue()) instanceof MyEmojiMessage &&
                    !containsEmojiId(((MyEmojiMessage)(entry1.getValue())).getEmojiId())) {
                it1.remove(); } }
        return emojis.size(); }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (containsPerson(personId)) {
            List<Message> messages1 = getPerson(personId).getMessages();
            Iterator<Message> iterator = messages1.iterator();
            while (iterator.hasNext()) {
                Message message = iterator.next();
                if (message instanceof NoticeMessage) { iterator.remove(); } }
        } else { throw new MyPersonIdNotFoundException(personId, counter); } }

    @Override
    public int queryBestAcquaintance(int id)
            throws PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (containsPerson(id) && !((MyPerson) getPerson(id)).getAcquaintance().isEmpty()) {
            return ((MyPerson) getPerson(id)).getBestAcquaintanceId();
        } else if (!containsPerson(id)) { throw new MyPersonIdNotFoundException(id, counter); }
        else if (((MyPerson) getPerson(id)).getAcquaintance().isEmpty()) {
            throw new MyAcquaintanceNotFoundException(id, counter); }
        return -1; }

    @Override
    public int queryCoupleSum() {
        int ans = 0;
        for (Integer key : persons.keySet()) {
            if (((MyPerson) getPerson(key)).getBestAcquaintanceId() == key) { continue; }
            else if (((MyPerson) getPerson(((MyPerson) getPerson(key)).getBestAcquaintanceId())).
                    getBestAcquaintanceId() == key) { ans++; }
        } return ans / 2; }

    @Override
    public int queryShortestPath(int id1, int id2)
            throws PersonIdNotFoundException, PathNotFoundException {
        if (containsPerson(id1) && containsPerson(id2) && isCircle(id1, id2)) {
            if (id1 == id2) { return 0; } else {
                HashMap<Integer, Boolean> visited = new HashMap<>();
                PriorityQueue<Node> heap = new PriorityQueue<>();
                HashMap<Integer, Integer> distance = new HashMap<>();
                for (Integer key : persons.keySet()) {
                    visited.put(key, false);
                    distance.put(key, Integer.MAX_VALUE); }
                distance.replace(id1, -1);
                heap.add(new Node(id1, -1));
                while (!heap.isEmpty()) {
                    Node cur = heap.poll();
                    int curId = cur.getAcqId();
                    if (curId == id2) { return distance.get(id2); }
                    if (visited.get(curId)) { continue; }
                    visited.put(curId, true);
                    MyPerson person = (MyPerson) persons.get(curId);
                    for (Integer key : person.getAcquaintance().keySet()) {
                        if (!visited.get(key) && 1 + distance.get(curId) < distance.get(key)) {
                            distance.put(key, distance.get(curId) + 1);
                            heap.add(new Node(key, distance.get(key))); } } }
                return distance.get(id2); }
        } else if (!containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1, counter);
        } else if (!containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2, counter);
        } else if (!isCircle(id1, id2)) {
            throw new MyPathNotFoundException(id1, id2, counter); }
        return Integer.MIN_VALUE; }

    public void updateBlockSum(int id1, int id2) throws PersonIdNotFoundException {
        if (disjointSet.find(id1) != disjointSet.find(id2)) { blockSum--; } }

    public void addBlockSum(int id1, int id2, Boolean past) {
        if (disjointSet.find(id1) != disjointSet.find(id2) && past) { blockSum++; } }

    public void updateTripleSum(int id1, int id2) {
        MyPerson myPerson1 = (MyPerson) getPerson(id1);
        MyPerson myPerson2 = (MyPerson) getPerson(id2);
        for (Integer key : myPerson1.getAcquaintance().keySet()) {
            if (myPerson1.getAcquaintance().get(key).isLinked(myPerson2)) { tripleSum++; } }
    }

    public void subTripleSum(int id1, int id2) {
        MyPerson myPerson1 = (MyPerson) getPerson(id1);
        MyPerson myPerson2 = (MyPerson) getPerson(id2);
        for (Integer key : myPerson1.getAcquaintance().keySet()) {
            if (myPerson1.getAcquaintance().get(key).isLinked(myPerson2)) { tripleSum--; } }
    }

    public Message[] getMessages() { return null; }

    public int[] getEmojiIdList() { return null; }

    public int[] getEmojiHeatList() { return null; }
}
