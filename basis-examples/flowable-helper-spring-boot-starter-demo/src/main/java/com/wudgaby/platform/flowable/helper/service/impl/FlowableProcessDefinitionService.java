package com.wudgaby.platform.flowable.helper.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.enums.ProcessType;
import com.wudgaby.platform.flowable.helper.form.DeployForm;
import com.wudgaby.platform.flowable.helper.form.ProcessDefinitionQueryForm;
import com.wudgaby.platform.flowable.helper.mapper.FlowableProcessDefinitionDao;
import com.wudgaby.platform.flowable.helper.vo.ProcessDefinitionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * @ClassName : FlowableProcessDefinitionService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/18 16:52
 * @Desc :   流程定义管理
 */
@Slf4j
@Service
@AllArgsConstructor
public class FlowableProcessDefinitionService implements com.wudgaby.platform.flowable.helper.service.FlowableProcessDefinitionService {
    private final RepositoryService repositoryService;
    private final FlowableProcessDefinitionDao flowableProcessDefinitionDao;

    private static final String[] RAR_FILE = new String[]{"rar", "zip", "bar"};
    private static final String[] BPMN_FILE = new String[]{"bpmn", "bpmn20.xml", "xml"};

    @Override
    public IPage<ProcessDefinitionVo> getListByPage(ProcessDefinitionQueryForm queryForm) {
        return flowableProcessDefinitionDao.findProcessDefList(new Page(queryForm.getPageNum(), queryForm.getPageCount()), queryForm);
    }

    @Override
    public ProcessDefinitionVo getById(String processDefinitionId) {
        return null;
    }

    @Override
    public void suspendOrActivateProcessDefinitionById(String processDefinitionId, int suspensionState) {

    }

    @Override
    public Deployment deploy(ProcessType processType, String processName, MultipartFile file) throws IOException {
        DeployForm deployForm = new DeployForm();
        deployForm.setFileName(file.getOriginalFilename());
        deployForm.setProcessName(processName);
        deployForm.setProcessType(processType);
        return deploy(deployForm, file.getInputStream());
    }

    private Deployment deployFormFile(ProcessType processType, String processName, InputStream inputStream) throws IOException {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .category(processType.name())
                .name(processName)
                .tenantId(FlowConstant.TENANT);

        //resourceName必须以"bpmn20.xml", "bpmn" 结尾.
        deploymentBuilder.addInputStream(processName + ".bpmn20.xml", inputStream);
        Deployment deployment = deploymentBuilder.deploy();
        return deployment;
    }

    private Deployment deployFromZip(ProcessType processType, String processName, InputStream inputStream) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name(processName);
        deploymentBuilder.category(processType.name());
        deploymentBuilder.addZipInputStream(zipInputStream);
        deploymentBuilder.tenantId(FlowConstant.TENANT);
        Deployment deployment = deploymentBuilder.deploy();
        return deployment;
    }

    @Override
    public Deployment deploy(DeployForm deployForm) {
        InputStream inputStream = null;
        if(inputStream == null){
            throw new BusinessException("部署失败,部署文件下载失败.");
        }
        return deploy(deployForm, inputStream);
    }

    private Deployment deploy(DeployForm deployForm, InputStream inputStream){
        String ext = FilenameUtils.getExtension(deployForm.getFileName());
        Deployment deployment = null;
        try{
            if(ArrayUtils.contains(RAR_FILE, ext.toLowerCase())){
                deployment = deployFromZip(deployForm.getProcessType(), deployForm.getProcessName(), inputStream);
            }else if(ArrayUtils.contains(BPMN_FILE, ext.toLowerCase())){
                deployment = deployFormFile(deployForm.getProcessType(), deployForm.getProcessName(), inputStream);
            }else{
                throw new BusinessException("错误的文件格式.");
            }
            if(deployment != null){
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
                if(processDefinition == null){
                    throw new BusinessException("部署失败.");
                }
                repositoryService.setProcessDefinitionCategory(processDefinition.getId(), deployForm.getProcessType().name());
            }
        }catch (BusinessException be){
            throw be;
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            throw new BusinessException("解析流程文件失败, 错误的流程内容.");
        }
        return deployment;
    }
}
