import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;

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

    @Override
    public void modifyRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (containsPerson(id1) && containsPerson(id2)
                && id1 != id2 && getPerson(id1).isLinked(getPerson(id2))
                && getPerson(id1).queryValue(getPerson(id2)) + value > 0) {
            //由于addRelation是一起add的所以单方面有则另一方面肯定有
            ((MyPerson) getPerson(id1)).modifyValue(value, id2);
            ((MyPerson) getPerson(id2)).modifyValue(value, id1);
        } else if (containsPerson(id1) && containsPerson(id2)
                && id1 != id2 && getPerson(id1).isLinked(getPerson(id2))
                && getPerson(id1).queryValue(getPerson(id2)) + value <= 0) {


            ((MyPerson) getPerson(id2)).subLink(getPerson(id1));
            ((MyPerson) getPerson(id1)).subLink(getPerson(id2));
            ((MyPerson) getPerson(id2)).subValue(getPerson(id1).getId());
            ((MyPerson) getPerson(id1)).subValue(getPerson(id2).getId());
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
