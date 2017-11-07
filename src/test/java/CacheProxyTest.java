import for_testings.service_users.*;
import for_testings.service_users.test_services.ServiceUser;
import for_testings.service_users.test_services.TestService;
import org.junit.Test;
import ru.edhunter.cache_implementation.CacheProxy;
import ru.edhunter.cache_implementation.interfaces.CachedService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CacheProxyTest {

    @Test()
    public void testCache() throws Exception {
        List<ServiceUser> users = new ArrayList<>();
        List<Future<List<String>>> futures = new ArrayList<>();
        List<List<String>> result = new ArrayList<>();

        users.add(new ServiceUser1());
        users.add(new ServiceUser2());
        users.add(new ServiceUser3());
        users.add(new ServiceUser4());
        users.add(new ServiceUser5());

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (ServiceUser user : users) {
            futures.add(executorService.submit(user));
        }

        for (ServiceUser user : users) {
            futures.add(executorService.submit(user));
        }

        for (ServiceUser user : users) {
            futures.add(executorService.submit(user));
        }

        for (ServiceUser user : users) {
            futures.add(executorService.submit(user));
        }

        for (ServiceUser user : users) {
            futures.add(executorService.submit(user));
        }

        for (ServiceUser user : users) {
            futures.add(executorService.submit(user));
        }

        int count = 0;
        while (count<futures.size()) {
            if(futures.get(count).isDone()) {
                result.add(futures.get(count++).get());
            }
        }

        for (List<String> list : result) {
            for (String s : list) {
                System.out.println(s);
            }
        }
    }

    @Test
    public void testFileCache() {
        TestService testService = new TestService();
        CacheProxy cacheProxy = new CacheProxy();
        CachedService cachedService = cacheProxy.cache(testService);

        for (int i = 0; i < 10; i++) {
            System.out.println(cachedService.work("Vova", i));
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(cachedService.work("Vova", i));
        }

    }
}
