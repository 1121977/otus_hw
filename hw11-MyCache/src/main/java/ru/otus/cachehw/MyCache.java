package ru.otus.cachehw;

import ru.otus.core.model.Client;

import java.util.*;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private final WeakHashMap<K,V> cache = new WeakHashMap<>();
    private final List<K> keyList = new ArrayList<>();
    private final Set<HwListener> listenerSet = new HashSet<>();

    @Override
    public void put(K key, V value) {
        if(!cache.containsKey(key)){
            if (keyList.size()>=5){
                this.remove(keyList.get(0));
            }
            cache.put(key,value);
            keyList.add(key);
            for (HwListener listener:listenerSet){
                listener.notify(key,value,"Adding to cache: ");
            }
        }
    }

    @Override
    public void remove(K key) {
        for(HwListener listener:listenerSet){
            listener.notify(key,cache.get(key),"Remove from cache: ");
        }
        cache.remove(key);
        keyList.remove(key);
    }

    @Override
    public V get(K key) {
        if (cache.containsKey(key)) {
            for(HwListener listener: listenerSet){
                listener.notify(key,cache.get(key),"Get from cache: ");
            }
            return cache.get(key);
        }
        return null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listenerSet.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listenerSet.remove(listener);
    }
}