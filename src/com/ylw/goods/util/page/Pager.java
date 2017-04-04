package com.ylw.goods.util.page;

import java.util.List;

/**
 * 用于分页
 * Created by 85243 on 2017/3/29.
 */
public class Pager<T> {
    private int pc ; //当前页面
    private int tr;  //总行数
    private int ps;  //每页数据多少
    private String url; //url地址
    private List<T> listBean;//返回的list对象,在书的分页里面是BookList,在别的分类里面是别的对象

    /**
     * 返回总页数
     * @return
     */
    public int getTp(){
        return tr%ps==0?tr/ps:(tr+ps)/ps;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getListBean() {
        return listBean;
    }

    public void setListBean(List<T> listBean) {
        this.listBean = listBean;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "pc=" + pc +
                ", tr=" + tr +
                ", ps=" + ps +
                ", url='" + url + '\'' +
                ", listBean=" + listBean +
                '}';
    }
}
