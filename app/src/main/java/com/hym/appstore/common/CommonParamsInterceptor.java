package com.hym.appstore.common;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hym.appstore.common.utils.ACache;
import com.hym.appstore.common.utils.DensityUtil;
import com.hym.appstore.common.utils.DeviceUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class CommonParamsInterceptor implements Interceptor {

    public static final MediaType JSON = MediaType.parse("application/json;charset=uft-8");
    private Gson mGson;
    private Context mContext;

    public CommonParamsInterceptor(Context context, Gson mGson) {
        this.mGson = mGson;
        this.mContext = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();

        String method = request.method();

        try {
            HashMap<String,Object> commonParamsMap = new HashMap<>();
            commonParamsMap.put(Constant.IMEI, DeviceUtils.getIMEI(mContext));
//            commonParamsMap.put(Constant.IMEI, "1111111111");
            commonParamsMap.put(Constant.MODEL, DeviceUtils.getModel());
            commonParamsMap.put(Constant.LANGUAGE, DeviceUtils.getLanguage());
            commonParamsMap.put(Constant.OS, DeviceUtils.getOSVersion());
            commonParamsMap.put(Constant.RESOLUTION, DensityUtil.getScreenW(mContext) + "*" + DensityUtil.getScreenH(mContext));
            commonParamsMap.put(Constant.SDK,DeviceUtils.getBuildVersionSDK());
            commonParamsMap.put(Constant.DENSITY_SCALE_FACTOR,mContext.getResources().getDisplayMetrics().density + "");

            ACache aCache = ACache.get(mContext);
            String token = aCache.getAsString(Constant.TOKEN);
            commonParamsMap.put(Constant.TOKEN, token == null ? "" : token);

            if (method.equals("GET")) {
                HttpUrl httpUrl = request.url();
                HashMap<String,Object> rootMap = new HashMap<>();
                Set<String> paramNames = httpUrl.queryParameterNames();
                for (String key : paramNames){
                    if (Constant.PARAM.equals(key)) {
                        String oldParamJson = httpUrl.queryParameter(Constant.PARAM);
                        if (oldParamJson != null) {
                            HashMap<String,Object> p = mGson.fromJson(oldParamJson, HashMap.class);//原始参数
                            if (p != null) {
                                for (Map.Entry<String,Object> entry : p.entrySet()){
                                    rootMap.put(entry.getKey(),entry.getValue());
                                }
                            }
                        }
                    }else {
                        rootMap.put(key,httpUrl.queryParameter(key));
                    }
                }

                rootMap.put("publicParams",commonParamsMap);//重新组装
                String newJsonParams = mGson.toJson(rootMap);//组装好的参数

                String url = httpUrl.toString();
                int index = url.indexOf("?");
                if (index > 0) {
                    url = url.substring(0,index);
                }
                url = url + "?" + Constant.PARAM + "=" + newJsonParams;
                request = request.newBuilder().url(url).build();

            }else if (method.equals("POST")){
                RequestBody body = request.body();
                HashMap<String, Object> rootMap = new HashMap<>();
                if (body instanceof FormBody) {
                    for (int i = 0; i < ((FormBody) body).size(); i++) {
                        rootMap.put(((FormBody) body).encodedName(i),((FormBody) body).encodedValue(i));
                    }
                }else {
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);
                    String oldJsonParams = buffer.readUtf8();
                    if (!TextUtils.isEmpty(oldJsonParams)){
                        rootMap = mGson.fromJson(oldJsonParams, HashMap.class);//原始参数
                        if (rootMap != null) {
                            rootMap.put("publicParams",commonParamsMap);//重新组装
                            String newJsonParams = mGson.toJson(rootMap);//组装好的参数
                            request = request.newBuilder().post(RequestBody.create(JSON,newJsonParams)).build();
                        }

                    }

                }
            }

        }catch (JsonSyntaxException e){

        }

        return chain.proceed(request);
    }
}
