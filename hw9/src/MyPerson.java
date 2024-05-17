import com.oocourse.spec1.main.Person;

import java.util.HashMap;
import java.util.Objects;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
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

    public Boolean idEquals(Person person) {
        return id == person.getId();
    }

    public Boolean accEquals(Person person) {
        if (acquaintance.size() != ((MyPerson) person).getAcquaintance().size()) {
            return false;
        } else {
            for (Integer key : acquaintance.keySet()) {
                int flag = 0;
                for (Integer key1 : ((MyPerson) person).getAcquaintance().keySet()) {
                    if (Objects.equals(key1, key)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    return false;
                } else {
                    if (acquaintance.get(key).getId() == ((MyPerson) person).
                            getAcquaintance().get(key).getId()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public Boolean valueEquals(Person person) {
        if (value.size() != ((MyPerson) person).getValue().size()) {
            return false;
        } else {
            for (Integer key : value.keySet()) {
                int flag = 0;
                for (Integer key1 : ((MyPerson) person).getValue().keySet()) {
                    if (Objects.equals(key1, key)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    return false;
                } else {
                    if (Objects.equals(value.get(key), ((MyPerson) person).getValue().get(key))) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public Boolean personEquals(Person person) {
        if (idEquals(person) && accEquals(person) && valueEquals(person)) {
            return true;
        }
        return false;
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public HashMap<Integer, Integer> getValue() {
        return value;
    }

    public int getAccNum() {
        return acquaintance.size();
    }

    public boolean strictEquals(Person person) {
        return true;
    }

}
