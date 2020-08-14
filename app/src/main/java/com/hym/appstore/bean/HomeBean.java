package com.hym.appstore.bean;

import java.util.List;

public class HomeBean {

    private List<BannerBean> banners;

    private List<AppInfoBean> recommendApps;

    private List<AppInfoBean> recommendGames;

    public List<BannerBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerBean> banners) {
        this.banners = banners;
    }

    public List<AppInfoBean> getHomeApps() {
        return recommendApps;
    }

    public void setHomeApps(List<AppInfoBean> recommendApps) {
        this.recommendApps = recommendApps;
    }

    public List<AppInfoBean> getHomeGames() {
        return recommendGames;
    }

    public void setHomeGames(List<AppInfoBean> recommendGames) {
        this.recommendGames = recommendGames;
    }


}
