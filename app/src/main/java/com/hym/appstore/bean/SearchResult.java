package com.hym.appstore.bean;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    private boolean hasMore;

    private List<AppInfoBean> listApp;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<AppInfoBean> getListApp() {
        return listApp;
    }

    public void setListApp(List<AppInfoBean> listApp) {
        this.listApp = listApp;
    }
}
