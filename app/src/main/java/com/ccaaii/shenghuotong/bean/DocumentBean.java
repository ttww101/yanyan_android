package com.ccaaii.shenghuotong.bean;

/**
 */

public class DocumentBean {

    /**
     * id : HT2e5BBC
     * category : 3
     * name : 5种食物堪称隐形肥肉 想减肥就忌口
     * description : 吃多了肥肉会导致胆固醇含量超标，引起动脉硬化、高血压等疾病，这是人们都知道的常识。但餐桌上一些看似并不油腻的食物，如动物内脏、蛋黄等，却含有大量的胆固醇，因此又被称为“隐形肥肉”。
     * isHot : false
     * isNew : false
     * playcount : 12
     */

    public static final int CATEGORY_COMMON = 0;
    public static final int CATEGORY_TIPS = 1;
    public static final int CATEGORY_CHILD = 2;
    public static final int CATEGORY_HEALTH = 3;
    public static final int CATEGORY_SPORT = 4;
    public static final int CATEGORY_FACIAL = 5;
    public static final int CATEGORY_AID = 6;
    public static final int CATEGORY_BUSINESS = 7;

    private String id;
    private int category;
    private String name;
    private String description;
    private boolean isHot;
    private boolean isNew;
    private int playcount;
    private long creaetTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public long getCreaetTime() {
        return creaetTime;
    }

    public void setCreaetTime(long creaetTime) {
        this.creaetTime = creaetTime;
    }
}
