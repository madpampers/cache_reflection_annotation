package for_testings;

import java.io.Serializable;

public class Person implements Serializable{
    String name = "Vova";
    double age = 13;

    public Person(String item, double i) {
        this.name = item;
        this.age = i;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
