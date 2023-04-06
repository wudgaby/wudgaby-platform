package com.wudgaby.downloadsample.download;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/6 0006 12:05
 * @desc :
 */
public interface DownloadProcessor<T> {
    /**
     * 下载处理
     * @param t
     */
    void process(T t);

    /**
     * 完成后处理
     */
    void finish();
}
