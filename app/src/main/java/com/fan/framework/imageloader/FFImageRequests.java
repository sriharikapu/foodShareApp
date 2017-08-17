package com.fan.framework.imageloader;

import com.fan.framework.utils.FFUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 图片请求队列封装类
 */
public class FFImageRequests {
    private HashMap<Object, ArrayList<FFImageRequest>> map;

    public ArrayList<FFImageRequest> getQueue(Object url) {
        return map.get(url);
    }

    public FFImageRequests() {
        map = new HashMap<Object, ArrayList<FFImageRequest>>();
    }
//
//    public void addQueue(String key, FFImageRequest request) {
//        ArrayList<FFImageRequest> queue = new ArrayList<FFImageRequest>();
//        queue.add(request);
//        map.put(key, queue);
//    }

    public boolean containsKey(Object imageUrl) {
        return map.containsKey(imageUrl);
    }

    public void add(Object key, FFImageRequest request) {
        if (map.get(key) == null) {
            ArrayList<FFImageRequest> queue = new ArrayList<FFImageRequest>();
            queue.add(request);
            map.put(key, queue);
            return;
        }
        if (!map.get(key).contains(request))
            map.get(key).add(request);
    }

    public void remove(Object imageUrl) {
        map.remove(imageUrl);
    }

    public void addAll(ArrayList<FFImageRequest> list) {
        if (FFUtils.isListEmpty(list)) {
            return;
        }
        if (map.get(list.get(0).getImageUrl()) != null) {
            map.get(list.get(0).getImageUrl()).addAll(list);
        } else {
            map.put(list.get(0).getImageUrl(), list);
        }
    }


    public int getWidth(FFImageRequest request) {
        int width = 0;
        ArrayList<FFImageRequest> list = getQueue(request.getImageUrl());
        for (FFImageRequest fRequest : list) {
            int w = fRequest.getWidth();
            if (w < 0) {
                w = FFUtils.getDisWidth();
            }
            width = Math.max(width, w);
        }
        return width;
    }

    public int getHeight(FFImageRequest request) {
        int height = 0;
        ArrayList<FFImageRequest> list = getQueue(request.getImageUrl());
        for (FFImageRequest fRequest : list) {
            int w = fRequest.getHeight();
            if (w < 0) {
                w = FFUtils.getDisHight();
            }
            height = Math.max(height, w);
        }
        return height;
    }


}
