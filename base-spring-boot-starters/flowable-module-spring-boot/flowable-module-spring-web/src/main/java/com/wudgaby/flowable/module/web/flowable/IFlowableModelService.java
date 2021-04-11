package com.wudgaby.flowable.module.web.flowable;

import com.wudgaby.flowable.module.web.vo.ModelVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : bruce.liu
 * @projectName : flowable
 * @description: 模型service
 * @date : 2019/11/1920:56
 */
public interface IFlowableModelService {

    /**
     * 导入模型
     * @param file 文件
     * @return
     */
    void importProcessModel(MultipartFile file);

    /**
     * 添加模型
     * @param modelVo
     * @return
     */
    void addModel(ModelVo modelVo);
}
