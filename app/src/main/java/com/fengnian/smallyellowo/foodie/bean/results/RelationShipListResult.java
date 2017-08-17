package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.RelationShipSorts;

import java.util.ArrayList;

/**
 * Created by lanbiao on 16/8/10.
 */
public class RelationShipListResult extends BaseResult {
    private ArrayList<RelationShipSorts> relationships;

    public void setRelationships(ArrayList<RelationShipSorts> relationships) {
        this.relationships = relationships;
    }

    public ArrayList<RelationShipSorts> getRelationships() {
        return relationships;
    }
}
