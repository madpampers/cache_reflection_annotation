package for_testings.service_users;

import for_testings.service_users.test_services.ServiceUser;
import for_testings.service_users.test_services.TestService3;
import ru.edhunter.cache_implementation.CacheProxy;
import ru.edhunter.cache_implementation.interfaces.CachedService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class ServiceUser3 implements ServiceUser {
    TestService3 testService3 = new TestService3();
    CacheProxy cacheProxy3 = new CacheProxy();
    CachedService cachedService3 = cacheProxy3.cache(testService3);


    @Override
    public List<String> call() throws Exception {
        return cachedService3.run("Dima", 1, new Date());
    }
}
