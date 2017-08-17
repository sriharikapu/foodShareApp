/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.fengnian.smallyellowo.foodie.clusterutil.clustering.algo;


import com.fengnian.smallyellowo.foodie.clusterutil.clustering.Cluster;
import com.fengnian.smallyellowo.foodie.clusterutil.clustering.ClusterItem;

import java.util.Collection;
import java.util.Set;

/**
 * Logic for computing clusters
 */
public interface Algorithm<T> {
    void addItem(T item);
    void addItems(Collection<T> items);

    void clearItems();

    void removeItem(T item);

    Set<? extends Cluster<T>> getClusters(double zoom);

    Collection<T> getItems();
}