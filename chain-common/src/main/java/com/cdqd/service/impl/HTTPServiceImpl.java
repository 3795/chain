package com.cdqd.service.impl;

import com.alibaba.fastjson.JSON;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.exception.ServerException;
import com.cdqd.service.HTTPService;
import com.cdqd.vo.ServerResponseVO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Description: 提供网络调用服务
 * Created At 2020/2/8
 */
@Service
public class HTTPServiceImpl<T> implements HTTPService<T> {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public String get(String url) {
        ResponseEntity<ServerResponseVO> response = restTemplate.getForEntity(url, ServerResponseVO.class);
        return handleResponse(response);
    }

    @Override
    public String get(String url, Map<String, String> params) {
        url += "?";
        StringBuilder urlBuilder = new StringBuilder(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        url = urlBuilder.toString();
        ResponseEntity<ServerResponseVO> response = restTemplate.getForEntity(url, ServerResponseVO.class);
        return handleResponse(response);
    }

    @Override
    public String post(String url, Map<String, Object> paramMap) {
        MultiValueMap<String, Object> map = map2LinkedMultiValueMap(paramMap);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<ServerResponseVO> response = restTemplate.postForEntity(url, httpEntity, ServerResponseVO.class);
        return handleResponse(response);
    }

    @Override
    public String postForObject(String url, Object object) {
        ResponseEntity<ServerResponseVO> response = restTemplate.postForEntity(url, object, ServerResponseVO.class);
        return handleResponse(response);
    }

    /**
     * Map转换
     * @param paramsMap
     * @return
     */
    private MultiValueMap<String, Object> map2LinkedMultiValueMap(Map<String, Object> paramsMap) {
        MultiValueMap<String, Object> linkedMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            linkedMap.add(entry.getKey(), entry.getValue());
        }
        return linkedMap;
    }

    /**
     * 处理请求的返回结果
     * @param response
     * @return
     */
    private String handleResponse(ResponseEntity<ServerResponseVO> response) {
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new ServerException(ResponseCodeEnum.REMOTE_CALL_FAILED);
        } else {
            ServerResponseVO serverResponsevo = response.getBody();
            if (serverResponsevo.getCode() == ResponseCodeEnum.SUCCESS.getCode()) {
                return JSON.toJSONString(serverResponsevo.getData());
            } else {
                // 重组该异常，并返回
                throw new ServerException(serverResponsevo.getCode(), serverResponsevo.getMessage());
            }
        }
    }
}
