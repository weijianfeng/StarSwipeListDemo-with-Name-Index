package com.example.mysidebar.model;

import org.litepal.crud.DataSupport;

/**
 * 实体类：一个联系人
 * 
 * @author owen
 */
public class Contact extends DataSupport {

    /**
     * id litepal 需要
     */
    private int id;

    /**
     * 联系人姓名
     */
    private String name = null;

    /**
     * 姓名首字母
     */
    private String firstLetter = null;

    /**
     * 是否被收藏
     */
    private boolean isStar = false;

    public Contact() {
    }

    // public Contact(String name, String firstLetter, boolean isStar) {
    // this.name = name;
    // this.firstLetter = firstLetter;
    // this.isStar = isStar;
    // }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean isStar) {
        this.isStar = isStar;
    }

}
