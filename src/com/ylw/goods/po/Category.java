package com.ylw.goods.po;

import java.util.List;

/**
 * 分类的实体类
 * 这里有一个自身关联 和双向关联 ->双向自身关联
 * Created by 85243 on 2017/3/29.
 */
public class Category {
    private String cid;
    private String cname;
//    private String pid;//数据库中的外键,其实数据库的外键相当于这个表的对象,因为这个外键指向本身,所以需要自身类的对象
//    指向父分类的pid,这里不使用pid, 就是父分类也是一个category类,用这个表示更像面向对象,
// 而数据库中:用pid,类似于指针,pid指向一个Category对象,所以这里使用Category的对象parent
    private Category parent;
    private String desc;

//    因为有的分类有子分类,子分类也是Category对象,但是子分类可能有多个,所以使用list
    private List<Category> children;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public Category() {
    }

    @Override
    public String toString() {
        return "Category{" +
                "cid='" + cid + '\'' +
                ", cname='" + cname + '\'' +
                ", parent=" + parent +
                ", desc='" + desc + '\'' +
                ", children=" + children +
                '}';
    }
}
