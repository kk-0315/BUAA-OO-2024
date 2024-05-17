import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;

import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;
    private int bestAcquaintanceId;
    private HashMap<Integer, Tag> tags;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
        this.bestAcquaintanceId = id;
        this.tags = new HashMap<>();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean containsTag(int id) {
        if (tags.containsKey(id)) {
            return true;
        }
        return false;
    }

    @Override
    public Tag getTag(int id) {
        if (containsTag(id)) {
            return tags.get(id);
        } else {
            return null;
        }
    }

    public HashMap<Integer, Tag> getTags() {
        return tags;
    }

    @Override
    public void addTag(Tag tag) {
        if (!containsTag(tag.getId())) {
            tags.put(tag.getId(), tag);
        }
    }

    @Override
    public void delTag(int id) {
        if (containsTag(id)) {
            tags.remove(id);
        }
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return ((Person) obj).getId() == this.id;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLinked(Person person) {
        if (this.acquaintance.containsKey(person.getId()) || person.getId() == id) {
            return true;
        }
        return false;
    }

    @Override
    public int queryValue(Person person) {
        if (this.value.containsKey(person.getId())) {
            return this.value.get(person.getId());
        }
        return 0;
    }

    public void addLink(Person person) {
        if (!acquaintance.containsKey(person.getId())) {
            this.acquaintance.put(person.getId(), person);
        }

    }

    public void addValue(int value, int personId) {
        if (!this.value.containsKey(personId)) {
            this.value.put(personId, value);

        }
    }

    public void modifyValue(int value, int personId) {
        int past = this.value.get(personId);
        this.value.replace(personId, past + value);
    }

    public void subLink(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            this.acquaintance.remove(person.getId());
        }
    }

    public void subValue(int personId) {
        if (this.value.containsKey(personId)) {
            this.value.remove(personId);
        }
    }

    public int getBestAcquaintanceId() {
        return this.bestAcquaintanceId;
    }

    public void setBestAcquaintanceId(int id) {

        this.bestAcquaintanceId = id;

    }

    public void modifyBestAcquaintanceId() {
        int max = 0;
        int flag = id;

        for (Integer key : value.keySet()) {
            if (value.get(key) >= max) {
                if (value.get(key) == max) {
                    flag = Math.min(flag, key);
                } else {
                    flag = key;
                }
                max = value.get(key);
            }
        }
        this.bestAcquaintanceId = flag;
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public boolean strictEquals(Person person) {
        return true;
    }

}
