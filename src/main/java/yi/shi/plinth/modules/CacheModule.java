package yi.shi.plinth.modules;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class CacheModule extends AbstractModule {

    @Override
    public void configure() {
        bind(Cache.class).toProvider(CacheProvider.class).in(Singleton.class);
    }

    private static class CacheProvider implements Provider<Cache>{
        @Override
        public Cache get() {
            Cache<String,Object> cache = CacheBuilder.newBuilder().recordStats().maximumSize(64).build();
            return cache;
        }
    }
}
