package for_testings;

import ru.edhunter.cache_implementation.interfaces.IService;
import ru.edhunter.cache_implementation.interfaces.annotation.Cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestService implements IService {
    @Override
    @Cache(listList = 5, identityBy = {String.class, double.class})
    public List<Person> run(String item, double value, Date date) {
        List<Person> personList = new ArrayList<>();
        Person person = new Person(item, value);
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            personList.add(person);
        }
        return personList;
    }

    @Override
    @Cache (listList = 5, identityBy = {String.class, double.class})
    public List<Person> work(String item, double value, Date date) {
        List<Person> personList = new ArrayList<>();
        Person person = new Person(item, value);
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            personList.add(person);
        }
        return personList;
    }
}
