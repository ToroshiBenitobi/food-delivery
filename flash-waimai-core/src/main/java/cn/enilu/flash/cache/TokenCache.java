package cn.enilu.flash.cache;

import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.bean.constant.cache.CacheKey;
import cn.enilu.flash.bean.core.ShiroUser;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.utils.HttpKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 用户登录时，生成的Token与用户ID的对应关系
 */
@Service
public   class TokenCache {

    @Autowired
    private RedisCacheDao ehcacheDao;
    public void put(String key,Object value){
        ehcacheDao.hset(RedisCacheDao.SESSION,key,value);
    }
     public <T> T get(String key,Class<T> klass){
         return ehcacheDao.hget(RedisCacheDao.SESSION, key,klass);
     }
    public   void put(String token, Long idUser) {
        ehcacheDao.hset(RedisCacheDao.SESSION,token, idUser);
    }

    public   Long getToken(String token) {
        return ehcacheDao.hget(RedisCacheDao.SESSION,token,Long.class);
    }
    public Long getIdUser(){
        return ehcacheDao.hget(RedisCacheDao.SESSION,HttpKit.getToken(),Long.class );
    }

    public   void remove(String token) {
        ehcacheDao.hdel(RedisCacheDao.SESSION,token+"user");
    }

    public void setUser(String token, ShiroUser shiroUser){
        ehcacheDao.hset(RedisCacheDao.SESSION,token+"user",shiroUser);
    }
    public ShiroUser getUser(String token){
        return ehcacheDao.hget(RedisCacheDao.SESSION,token+"user",ShiroUser.class);
    }
}
