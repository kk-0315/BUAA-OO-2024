import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.EqualTagIdException;
import com.oocourse.spec2.exceptions.PathNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.TagIdNotFoundException;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;

import java.util.HashMap;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> persons;
    private DisjointSet disjointSet;
    private static Counter counter;
    private static int blockSum;
    private static int tripleSum;

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.disjointSet = new DisjointSet();
        blockSum = 0;
        tripleSum = 0;
        counter = new Counter();
    }

    @Override
    public boolean containsPerson(int id) {
        if (persons.containsKey(id)) {
            return true;
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        if (containsPerson(id)) {

            return persons.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (!containsPerson(person.getId())) {

            persons.put(person.getId(), person);
            disjointSet.add(person.getId());
            blockSum++;
        } else {
            throw new MyEqualPersonIdException(person.getId(), counter);
        }
    }

    public int min(int id1, int id2) {
        if (id1 < id2) {
            return id1;
        } else {
            return id2;
        }
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

            //要是有两个相同的最大的value?
        } else if (!containsPerson(id1) || !containsPerson(id2)
                || getPerson(id1).isLinked(getPerson(id2))) {
            if (!containsPerson(id1)) {
                throw new MyPersonIdNotFoundException(id1, counter);
            } else if (!containsPerson(id2)) {
                throw new MyPersonIdNotFoundException(id2, counter);
            } else if (getPerson(id1).isLinked(getPerson(id2))) {
                throw new MyEqualRelationException(id1, id2, counter);
            }
        }
    }

    private void changeBestAcquaintanceId(int id1, int id2, int value) {
        if (((MyPerson) getPerson(id2)).getBestAcquaintanceId() == id2) {
            ((MyPerson) getPerson(id2)).setBestAcquaintanceId(id1);
        } else if (value >= getPerson(id2).queryValue(getPerson(((MyPerson) getPerson(id2))
                .getBestAcquaintanceId()))) {
            if (value == getPerson(id2).queryValue(getPerson(((MyPerson) getPerson(id2))
                    .getBestAcquaintanceId()))) {
                ((MyPerson) getPerson(id2)).setBestAcquaintanceId(min(id1,
                        ((MyPerson) getPerson(id2)).getBestAcquaintanceId()));
            } else {
                ((MyPerson) getPerson(id2)).setBestAcquaintanceId(id1);
            }
        }
    }

    @Override
    public void modifyRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (containsPerson(id1) && containsPerson(id2)
                && id1 != id2 && getPerson(id1).isLinked(getPerson(id2))
                && getPerson(id1).queryValue(getPerson(id2)) + value > 0) {
            //由于addRelation是一起add的所以单方面有则另一方面肯定有
            if (value > 0) {
                int nowValue = getPerson(id1).queryValue(getPerson(id2)) + value;
                changeBestAcquaintanceId(id1, id2, nowValue);
                changeBestAcquaintanceId(id2, id1, nowValue);
            }

            ((MyPerson) getPerson(id1)).modifyValue(value, id2);
            ((MyPerson) getPerson(id2)).modifyValue(value, id1);
            addTagValue(id1, id2, value);
            if (value < 0) {
                if (((MyPerson) getPerson(id1)).getBestAcquaintanceId() == id2) {
                    ((MyPerson) getPerson(id1)).modifyBestAcquaintanceId();
                }
                if (((MyPerson) getPerson(id2)).getBestAcquaintanceId() == id1) {
                    ((MyPerson) getPerson(id2)).modifyBestAcquaintanceId();
                }
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
                ((MyPerson) getPerson(id1)).modifyBestAcquaintanceId();
            }
            if (((MyPerson) getPerson(id2)).getBestAcquaintanceId() == id1) {
                ((MyPerson) getPerson(id2)).modifyBestAcquaintanceId();
            }

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
                throw new MyRelationNotFoundException(id1, id2, counter);
            }
        }
    }

    public void delPerson(int id1, int id2) {
        for (Integer key : ((MyPerson) getPerson(id1)).getTags().keySet()) {
            if (getPerson(id1).getTag(key).hasPerson(getPerson(id2))) {
                getPerson(id1).getTag(key).delPerson(getPerson(id2));
            }
        }
        for (Integer key : ((MyPerson) getPerson(id2)).getTags().keySet()) {
            if (getPerson(id2).getTag(key).hasPerson(getPerson(id1))) {
                getPerson(id2).getTag(key).delPerson(getPerson(id1));
            }
        }
    }

    public void addTagValue(int id1, int id2, int value) {
        for (Integer key : persons.keySet()) {
            if (persons.get(key).isLinked(getPerson(id1)) &&
                    persons.get(key).isLinked(getPerson(id2))) {
                for (Integer key1 : ((MyPerson) persons.get(key)).getTags().keySet()) {
                    MyTag myTag = (MyTag) ((MyPerson) persons.get(key)).getTags().get(key1);
                    if (myTag.hasPerson(getPerson(id1)) && myTag.hasPerson(getPerson(id2))) {
                        myTag.addValueSum(2 * value);
                    }
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
                        myTag.addValueSum(-2 * getPerson(id1).queryValue(getPerson(id2)));
                    }
                }
            }
        }
    }

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
            throw new MyRelationNotFoundException(id1, id2, counter);
        }
        return -1;//?
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (containsPerson(id1) && containsPerson(id2)) {
            return (disjointSet.find(id1) == disjointSet.find(id2));
        } else if (!containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1, counter);
        } else if (!containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2, counter);
        }
        return false;
    }

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
            throw new MyEqualTagIdException(tag.getId(), counter);
        }
    }

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
        } else if (containsPerson(personId1) && containsPerson(personId2) &&
                personId1 != personId2 && getPerson(personId2).isLinked(getPerson(personId1)) &&
                getPerson(personId2).containsTag(tagId) && !getPerson(personId2).getTag(tagId).
                hasPerson(getPerson(personId1)) &&
                getPerson(personId2).getTag(tagId).getSize() > 1111) {
            //什么都不做吗
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
            throw new MyEqualPersonIdException(personId1, counter);
        }
    }

    @Override
    public int queryTagValueSum(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (containsPerson(personId) && getPerson(personId).containsTag(tagId)) {
            return getPerson(personId).getTag(tagId).getValueSum();
        } else if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId, counter);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId, counter);
        }
        return -1;
    }

    @Override
    public int queryTagAgeVar(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (containsPerson(personId) && getPerson(personId).containsTag(tagId)) {
            return getPerson(personId).getTag(tagId).getAgeVar();
        } else if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId, counter);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId, counter);
        }
        return -1;
    }

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
            throw new MyPersonIdNotFoundException(personId1, counter);
        }
    }

    @Override
    public void delTag(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (containsPerson(personId) && getPerson(personId).containsTag(tagId)) {
            getPerson(personId).delTag(tagId);
        } else if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId, counter);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId, counter);
        }
    }

    @Override
    public int queryBestAcquaintance(int id)
            throws PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (containsPerson(id) && !((MyPerson) getPerson(id)).getAcquaintance().isEmpty()) {
            return ((MyPerson) getPerson(id)).getBestAcquaintanceId();
        } else if (!containsPerson(id)) {
            throw new MyPersonIdNotFoundException(id, counter);
        } else if (((MyPerson) getPerson(id)).getAcquaintance().isEmpty()) {
            throw new MyAcquaintanceNotFoundException(id, counter);
        }
        return -1;
    }

    @Override
    public int queryCoupleSum() {
        int ans = 0;
        for (Integer key : persons.keySet()) {
            if (((MyPerson) getPerson(key)).getBestAcquaintanceId() == key) {
                continue;
            } else if (((MyPerson) getPerson(((MyPerson) getPerson(key)).getBestAcquaintanceId())).
                    getBestAcquaintanceId() == key) {
                ans++;
            }

        }
        return ans / 2;
    }

    @Override
    public int queryShortestPath(int id1, int id2)
            throws PersonIdNotFoundException, PathNotFoundException {
        if (containsPerson(id1) && containsPerson(id2) && isCircle(id1, id2)) {
            if (id1 == id2) {
                return 0;
            } else {
                HashMap<Integer, Boolean> visited = new HashMap<>();
                PriorityQueue<Node> heap = new PriorityQueue<>();
                HashMap<Integer, Integer> distance = new HashMap<>();
                for (Integer key : persons.keySet()) {
                    visited.put(key, false);
                    distance.put(key, Integer.MAX_VALUE);
                }
                distance.replace(id1, -1);
                heap.add(new Node(id1, -1));
                while (!heap.isEmpty()) {
                    Node cur = heap.poll();
                    int curId = cur.getAcqId();
                    if (curId == id2) { // 如果当前节点是id2，直接返回结果
                        return distance.get(id2);
                    }
                    if (visited.get(curId)) {
                        continue;
                    }
                    visited.put(curId, true);
                    MyPerson person = (MyPerson) persons.get(curId);
                    for (Integer key : person.getAcquaintance().keySet()) {
                        if (!visited.get(key) && 1 + distance.get(curId) < distance.get(key)) {
                            distance.put(key, distance.get(curId) + 1);
                            heap.add(new Node(key, distance.get(key)));
                        }
                    }
                }
                return distance.get(id2);
            }
        } else if (!containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1, counter);
        } else if (!containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2, counter);
        } else if (!isCircle(id1, id2)) {
            throw new MyPathNotFoundException(id1, id2, counter);
        }
        return Integer.MIN_VALUE;
    }

    public void updateBlockSum(int id1, int id2) throws PersonIdNotFoundException {

        if (disjointSet.find(id1) != disjointSet.find(id2)) {
            blockSum--;
        }

    }

    public void addBlockSum(int id1, int id2, Boolean past) {
        if (disjointSet.find(id1) != disjointSet.find(id2) && past) {
            blockSum++;
        }
    }

    public void updateTripleSum(int id1, int id2) {

        MyPerson myPerson1 = (MyPerson) getPerson(id1);
        MyPerson myPerson2 = (MyPerson) getPerson(id2);
        for (Integer key : myPerson1.getAcquaintance().keySet()) {
            if (myPerson1.getAcquaintance().get(key).isLinked(myPerson2)) {
                tripleSum++;
            }
        }


    }

    public void subTripleSum(int id1, int id2) {
        MyPerson myPerson1 = (MyPerson) getPerson(id1);
        MyPerson myPerson2 = (MyPerson) getPerson(id2);
        for (Integer key : myPerson1.getAcquaintance().keySet()) {
            if (myPerson1.getAcquaintance().get(key).isLinked(myPerson2)) {
                tripleSum--;
            }
        }
    }

    public Person[] getPersons() {
        return null;
    }

}
