package for_testings.service_users;

import for_testings.service_users.test_services.ServiceUser;
import for_testings.service_users.test_services.TestService2;
import ru.edhunter.cache_implementation.CacheProxy;
import ru.edhunter.cache_implementation.interfaces.CachedService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class ServiceUser6 implements ServiceUser {
    TestService2 testService2 = new TestService2();
    CacheProxy cacheProxy = new CacheProxy();
    CachedService cachedService = cacheProxy.cache(testService2);


    @Override
    public List<String> call() throws Exception {
        return cachedService.run("Grav", 1, new Date());
    }
}
