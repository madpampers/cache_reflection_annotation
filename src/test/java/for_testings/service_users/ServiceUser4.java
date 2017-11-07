package for_testings.service_users;

import for_testings.service_users.test_services.ServiceUser;
import for_testings.service_users.test_services.TestService;
import ru.edhunter.cache_implementation.CacheProxy;
import ru.edhunter.cache_implementation.interfaces.CachedService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class ServiceUser4 implements ServiceUser {
    TestService testService = new TestService();
    CacheProxy cacheProxy = new CacheProxy();
    CachedService cachedService = cacheProxy.cache(testService);


    @Override
    public List<String> call() throws Exception {
        return cachedService.run("Petya", 1, new Date());
    }
}
