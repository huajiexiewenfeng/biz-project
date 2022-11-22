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
        Object object = decoder.decode(response, type);
        if (object instanceof ApiResponse) {
            ApiResponse responseObj = (ApiResponse) object;
            return responseObj.getBody();
        }
        return object;
    }
}
