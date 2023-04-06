package com.wudgaby.downloadsample.download;

import com.wudgaby.downloadsample.download.DownloadProcessor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/6 0006 11:18
 * @desc :
 */
@Getter
public abstract class AbstractResultHandler<T, U> implements ResultHandler<T> {
    private DownloadProcessor<U> downloadProcessor;

    public AbstractResultHandler(DownloadProcessor downloadProcessor){
        this.downloadProcessor = downloadProcessor;
    }

    @Override
    @SneakyThrows
    public void handleResult(ResultContext<? extends T> resultContext){
        T obj = resultContext.getResultObject();
        if(obj != null) {
            downloadProcessor.process(processing(obj));;
        }
    }

    /**
     * 处理数据
     * @param t
     * @return
     */
    public abstract U processing(T t);
}