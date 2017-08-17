package com.fan.framework.http;

public class FFUnsupportedImageTypeException extends IllegalArgumentException {
    private static final long serialVersionUID = -5697276896105705445L;

    public FFUnsupportedImageTypeException(int fileType) {
        super("不支持的图片类型" + fileType);
    }
}
