package com.cdqd.service;

import java.util.Map;

/**
 * Description: 提供网络调用服务
 * Created At 2020/2/8
 */
public interface HTTPService<T> {

    /**
     * 执行GET请求
     *
     * @param url 资源地址
     * @return
     */
    String get(String url);

    /**
     * 执行GET请求
     * @param url
     * @param params
     * @return
     */
    String get(String url, Map<String, String> params);

    /**
     * 执行POST请求
     * @param url       资源地址
     * @param paramMap  请求参数
     * @return
     */
    String post(String url, Map<String, Object> paramMap);
}
