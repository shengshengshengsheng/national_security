package com.shengsheng.police.caseManage;

import java.util.List;


public class TitleInfo {
    private String            title;
    private List<ContentInfo> info;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContentInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ContentInfo> info) {
        this.info = info;
    }
}
