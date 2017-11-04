import for_testings.TestService;
import org.junit.Test;
import ru.edhunter.cache_implementation.CacheProxy;
import ru.edhunter.cache_implementation.interfaces.IService;

import static org.junit.Assert.*;

import java.util.Date;

public class CacheProxyTest {
    @Test
    public void testMemCache(){
        TestService testService = new TestService();
        CacheProxy cacheProxy = new CacheProxy();
        IService cachedService = cacheProxy.cache(testService);

        long start = new Date().getTime();
        System.out.println(cachedService.work("Vova", 0, new Date()).size());
        long firstResult = new Date().getTime();
        System.out.println(cachedService.work("Vova", 0, new Date()).size());
        long secondResult = new Date().getTime();
        assertTrue(secondResult-firstResult<(firstResult-start)/10);
    }
    @Test
    public void testFileCache(){
        TestService testService = new TestService();
        CacheProxy cacheProxy = new CacheProxy();
        IService cachedService = cacheProxy.cache(testService);

        long start = new Date().getTime();
        System.out.println(cachedService.run("Vova", 0, new Date()).size());
        long firstResult = new Date().getTime();
        System.out.println(cachedService.run("Vova", 0, new Date()).size());
        long secondResult = new Date().getTime();
        assertTrue(secondResult-firstResult<(firstResult-start)/10);
    }
}
