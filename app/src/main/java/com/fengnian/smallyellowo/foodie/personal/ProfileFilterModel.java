package com.fengnian.smallyellowo.foodie.personal;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.List;

/**
 * Created by chenglin on 2017-3-28.
 */

public class ProfileFilterModel extends BaseResult {
    public List<FilterItem> foodNotePtypes;
    public List<ParentFilterItem> foodNoteStreets;

    public List<FilterItem> wantEatPtypes;
    public List<ParentFilterItem> wantEatStreets;

    public static final class FilterItem {
        public String key;
        public String value;
    }

    public static final class ParentFilterItem {
        public String key;
        public String value;
        public List<FilterItem> syStreets;
    }
}
