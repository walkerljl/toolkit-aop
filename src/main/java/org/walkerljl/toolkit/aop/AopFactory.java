package org.walkerljl.toolkit.aop;

import org.walkerljl.toolkit.aop.proxy.ProxyManager;
import org.walkerljl.toolkit.ioc.IocFactory;
import org.walkerljl.toolkit.aop.proxy.Proxy;
import org.walkerljl.toolkit.util.CollectionUtils;

import java.util.*;

/**
 * AOP 工厂
 *
 * @author lijunlin
 */
public class AopFactory {

    private Map<Class<?>, List<Class<?>>> proxyMap = new LinkedHashMap<Class<?>, List<Class<?>>>();

    private IocFactory iocFactory;

    public AopFactory(IocFactory iocFactory) {
        this.iocFactory = iocFactory;
    }

    public void init() {
        try {
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxyInstance = ProxyManager.createProxy(targetClass, proxyList);
                iocFactory.setBean(targetClass, proxyInstance);
            }
        } catch (Exception e) {
            throw new InitializationError("初始化 AopUtils 出错！", e);
        }
    }

    public void addProxy(Class<?> proxyClass, List<Class<?>> targetClassList) {
        if (proxyClass != null && CollectionUtils.isNotEmpty(targetClassList)) {
            proxyMap.put(proxyClass, targetClassList);
        }
    }

    private Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>, List<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            List<Class<?>> targetClassList = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassList) {
                Proxy baseAspect = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(baseAspect);
                } else {
                    List<Proxy> baseAspectList = new ArrayList<Proxy>();
                    baseAspectList.add(baseAspect);
                    targetMap.put(targetClass, baseAspectList);
                }
            }
        }
        return targetMap;
    }
}