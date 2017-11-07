package for_testings.service_users.test_services;
import ru.edhunter.cache_implementation.interfaces.CachedService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestService implements CachedService{
    @Override
    public List<String> run(String item, double value, Date date) {
        List<String> listOfStrings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listOfStrings.add(item + i);
        }
        return listOfStrings;
    }

    @Override
    public String work(String item, int value) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return item+value;
    }
}
