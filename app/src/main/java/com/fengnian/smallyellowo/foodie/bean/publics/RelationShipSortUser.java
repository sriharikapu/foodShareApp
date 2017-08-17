package com.fengnian.smallyellowo.foodie.bean.publics;

/**
 * Created by lanbiao on 16/8/15.
 */
public class RelationShipSortUser implements Comparable{
    private String key;
    private SYUser user;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public SYUser getUser() {
        return user;
    }

    @Override
    public int compareTo(Object another) {
        if(another == null || !(another instanceof RelationShipSortUser))
            return -1;

        RelationShipSortUser relationShip = (RelationShipSortUser)another;
        return key.compareTo(relationShip.key);
    }
}
