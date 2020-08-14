package com.hym.appstore.data.nohttp;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestHandler;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.tools.MultiValueMap;

import java.util.HashMap;

public class Interceptor implements com.yanzhenjie.nohttp.rest.Interceptor {

    Gson mGson;
    @Override
    public <T> Response<T> intercept(RequestHandler handler, Request<T> request) {
        HashMap<String,Object> commomParamsMap = new HashMap<>();
        commomParamsMap.put("imei","111");
        commomParamsMap.put("model","222");
        commomParamsMap.put("language","333");
        commomParamsMap.put("os","6.0");
        commomParamsMap.put("resolution","44");
        commomParamsMap.put("sdk","23");
        commomParamsMap.put("density_scale_factor","66");
        RequestMethod requestMethod = request.getRequestMethod();
        if (requestMethod == RequestMethod.GET) {

            MultiValueMap<String, Object> paramKeyValues = request.getParamKeyValues();
            String oldParamJson = (String) paramKeyValues.getValue("p",0);
            HashMap rootHashMap = new HashMap();
            if (oldParamJson != null) {
                rootHashMap = mGson.fromJson(oldParamJson, HashMap.class);
            }

            rootHashMap.put("publicParams",commomParamsMap);
//            paramKeyValues.add("publicP",commomParamsMap);
            String s = paramKeyValues.toString();
            request.set("p",rootHashMap.toString());
        }else if (requestMethod == RequestMethod.POST){

        }
        return null;
    }
}
