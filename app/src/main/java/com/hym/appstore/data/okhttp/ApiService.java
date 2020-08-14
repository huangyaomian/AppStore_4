package com.hym.appstore.data.okhttp;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.bean.LoginBean;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.bean.SearchResult;
import com.hym.appstore.bean.SortBean;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.bean.SubjectDetail;
import com.hym.appstore.bean.requestbean.LoginRequestBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
//    http://112.124.22.238:8081/course_api/cniaoplay/featured?p={%22page%22:0}
    String BASE_URL = "http:112.124.22.238:8081/course_api/cniaoplay/";

    /*@GET("featured")
    public Call<RecommendBean2> getApps(@Query("p") String jsonParam);*/

    @GET("featured")
    public Observable<BaseBean<List<AppInfoBean>>> getApps(@Query("p") String jsonParam);

    //http://112.124.22.238:8081/course_api/cniaoplay/index?p={"publicParams":{"model":"12","imei":"12","la":"12","os":"12","resolution":"12","sdk":"12","densityScaleFactor":"12"}}
    @GET("index")
    public Observable<BaseBean<HomeBean>> getHome();

    @GET("toplist")
    public Observable<BaseBean<PageBean<AppInfoBean>>> getTopList(@Query("page") int page);

    @GET("game")
    public Observable<BaseBean<PageBean<AppInfoBean>>> getGames(@Query("page") int page);

    @POST("login")
    public Observable<BaseBean<LoginBean>> login(@Body LoginRequestBean bean);

    @GET("category")
    Observable<BaseBean<List<SortBean>>> getCategories();

    @GET("category/featured/{categoryid}")
    Observable<BaseBean<PageBean<AppInfoBean>>> getFeaturedAppsBySort(@Path("categoryid") int categoryid, @Query("page") int page);

    @GET("category/toplist/{categoryid}")
    Observable<BaseBean<PageBean<AppInfoBean>>> getTopListAppsBySort(@Path("categoryid") int categoryid,@Query("page") int page);

    @GET("category/newlist/{categoryid}")
    Observable<BaseBean<PageBean<AppInfoBean>>> getNewListAppsBySort(@Path("categoryid") int categoryid,@Query("page") int page);

    @GET("app/{id}")
    Observable<BaseBean<AppInfoBean>> getAppDetail(@Path("id")int id);

    @GET("apps/updateinfo")
    Observable<BaseBean<List<AppInfoBean>>> getAppsUpdateinfo(@Query("packageName") String packageName,@Query("versionCode") String versionCode);

    @GET("subject/hot")
    Observable<BaseBean<PageBean<Subject>>> subjects(@Query("page") int page);

    @GET("subject/{id}")
    Observable<BaseBean<SubjectDetail>> subjectDetail(@Path("id") int id);

    @GET("search/suggest")
    Observable<BaseBean<List<String>>> searchSuggest(@Query("keyword") String keyword);

    @GET("search")
    Observable<BaseBean<SearchResult>> search(@Query("keyword") String keyword);

}
