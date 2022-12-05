package com.csdn.biz.api.openfeign;

import com.csdn.biz.api.ApiResponse;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.http.MediaType;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\22 0022 16:47
 */
public class ApiResponseDecoder implements Decoder {


    private final ResponseEntityDecoder decoder;

    public ApiResponseDecoder(ResponseEntityDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        String contextType = getContextType(response);
        MediaType mediaType = MediaType.parseMediaType(contextType);
        String version = mediaType.getParameter("v");
        if (null == version) {
            Object object = decoder.decode(response, ApiResponse.class);
            if (object instanceof ApiResponse) {
                ApiResponse responseObj = (ApiResponse) object;
                return responseObj.getBody();
            }
        }
        return decoder.decode(response, type);

    }

    private String getContextType(Response response) {
        Collection<String> types = response.headers().getOrDefault("Content-Type", Arrays.asList("application/json;v=3"));
        return types.iterator().next();
    }
}
