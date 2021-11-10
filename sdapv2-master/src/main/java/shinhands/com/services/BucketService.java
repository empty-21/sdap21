package shinhands.com.services;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;

// import org.infinispan.Cache;
// import org.infinispan.configuration.cache.CacheMode;
// import org.infinispan.configuration.cache.ConfigurationBuilder;
// import org.infinispan.manager.DefaultCacheManager;
// import org.infinispan.manager.EmbeddedCacheManager;
// import org.infinispan.configuration.global.GlobalConfigurationBuilder;

@Slf4j
@ApplicationScoped
public class BucketService {

    // Cache<Object, Bucket> bucketCache;

    // EmbeddedCacheManager cacheManager;

    // public List<Bucket> getAll() {
    //     return new ArrayList<>(bucketCache.values());
    // }

    // public void save(Bucket entry) {
    //     bucketCache.put(getKey(entry), entry);
    // }

    // public void save(String key, String name, Object value) {
    //     Bucket entry = new Bucket(key,name,value);
    //     bucketCache.put(key, entry);
    // }

    // public void save(String key, Object value) {
    //     Bucket entry = new Bucket(key,null,value);
    //     bucketCache.put(key, entry);
    // }

    // public void delete(Bucket entry) {
    //     bucketCache.remove(getKey(entry));
    // }

    // public static String getKey(Bucket entry){
    //     return entry.getKey();
    // }

    // public Bucket findById(String key) {
    //     return bucketCache.get(key);
    // }

    // void onStart(@Observes @Priority(value = 1) StartupEvent ev){
    //     GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
    //     global.transport().clusterName("Bucket");
    //     cacheManager = new DefaultCacheManager(global.build());

    //     ConfigurationBuilder config = new ConfigurationBuilder();

    //     config.expiration().lifespan(365, TimeUnit.DAYS)
    //             .persistence()
    //             // https://github.com/automenta/spangraph/blob/master/sqrc/main/java/com/syncleus/spangraph/InfiniPeer.java
    //             // 여기서 부터
    //             .addSingleFileStore()
    //             .location("./bucket_infinispan")
    //             .maxEntries(1000)
    //             // 요기까지
    //             // .clustering().cacheMode(CacheMode.REPL_SYNC);
    //             .clustering().cacheMode(CacheMode.DIST_SYNC);

    //     cacheManager.defineConfiguration("bucket", config.build());
    //     bucketCache = cacheManager.getCache("bucket");
    //     bucketCache.addListener(new CacheListener());


    //     log.info("bucket Cache initialized");

    // }

}