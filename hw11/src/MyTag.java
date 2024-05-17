import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

import java.util.HashMap;

public class MyTag implements Tag {
    private int id;
    private HashMap<Integer, Person> persons;
    private int valueSum;
    private int ageSum;
    private int agePowSum;

    public MyTag(int id) {
        this.id = id;
        this.persons = new HashMap<>();
        valueSum = 0;
        ageSum = 0;
        agePowSum = 0;

    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Tag) {
            return (((Tag) obj).getId() == id);
        } else {
            return false;
        }
    }

    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {

            updateValueSum(person);
            persons.put(person.getId(), person);
            ageSum += person.getAge();
            agePowSum += person.getAge() * person.getAge();
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return persons.containsKey(person.getId());
    }

    public void addValueSum(int n) {
        valueSum += n;
    }

    @Override
    public int getValueSum() {
        /*int valueSum = 0;
        for (Integer key : persons.keySet()) {
            for (Integer key1 : persons.keySet()) {
                valueSum += persons.get(key).queryValue(persons.get(key1));
            }
        }*/
        return valueSum;
    }

    public void updateValueSum(Person person) {
        for (Integer key : persons.keySet()) {
            if (persons.get(key).isLinked(person)) {
                valueSum += persons.get(key).queryValue(person) * 2;
            }
        }
    }

    @Override
    public int getAgeMean() {
        return persons.isEmpty() ? 0 : ageSum / persons.size();
    }

    @Override
    public int getAgeVar() {
        return persons.isEmpty() ? 0 : (agePowSum - 2 * getAgeMean() * ageSum + persons.size() *
                getAgeMean() * getAgeMean()) / persons.size();
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            persons.remove(person.getId());
            ageSum -= person.getAge();
            agePowSum -= person.getAge() * person.getAge();
            for (Integer key : persons.keySet()) {
                if (persons.get(key).isLinked(person)) {
                    valueSum -= persons.get(key).queryValue(person) * 2;
                }
            }

        }
    }

    public HashMap<Integer, Person> getPersons() {
        return persons;
    }

    @Override
    public int getSize() {
        return persons.size();
    }
}
