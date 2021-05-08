package com.wudgaby.platform.flowable.helper.controller;

import com.github.pagehelper.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.flowable.helper.entity.FlowNode;
import com.wudgaby.platform.flowable.helper.form.RejectForm;
import com.wudgaby.platform.flowable.helper.form.TaskCompleteForm;
import com.wudgaby.platform.flowable.helper.service.FlowableTaskService;
import com.wudgaby.platform.flowable.helper.vo.*;
import com.wudgaby.platform.security.core.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : FlowableTaskController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/27 14:36
 * @Desc :   TODO
 */
@Slf4j
@Api(tags = "任务审批")
@RestController
@RequestMapping("/workflow/tasks")
public class FlowableTaskController {
    @Autowired
    private FlowableTaskService flowableTaskService;
    @Autowired
    protected RuntimeService runtimeService;

    @PostMapping("/audit")
    @ApiOperation("审批任务")
    public ApiResult audit(@Validated @RequestBody TaskCompleteForm taskCompleteForm, String userId){
        flowableTaskService.complete(taskCompleteForm, userId);
        return ApiResult.success();
    }

    @PostMapping("/repeal")
    @ApiOperation("撤销流程")
    public ApiResult repeal(@Validated @RequestBody RejectForm rejectForm, String userId){
        flowableTaskService.repeal(rejectForm, userId);
        return ApiResult.success();
    }

    @GetMapping("/applying")
    @ApiOperation("待办任务")
    public ApiResult applying(@RequestParam(required = false) String userId){
        if(StringUtils.isBlank(userId)){
            userId = String.valueOf(SecurityUtils.getCurrentUser().get().getId());
        }

        TaskQueryVo params = new TaskQueryVo();
        params.setUserCode(userId);

        PageForm pageForm = new PageForm();
        pageForm.setPageCount(100L);
        pageForm.setPageNum(1L);

        Page<TaskVo> page = flowableTaskService.getApplyingTasks(params, pageForm);
        return ApiResult.success().data(page.getResult());
    }

    @GetMapping("/applied")
    @ApiOperation("已办任务")
    public ApiResult applied(@RequestParam(required = false) String userId){
        if(StringUtils.isBlank(userId)){
            userId = String.valueOf(SecurityUtils.getCurrentUser().get().getId());
        }

        TaskQueryVo params = new TaskQueryVo();
        params.setUserCode(userId);

        PageForm pageForm = new PageForm();
        pageForm.setPageCount(100L);
        pageForm.setPageNum(1L);

        Page<TaskVo> page = flowableTaskService.getApplyedTasks(params, pageForm);
        return ApiResult.success().data(page.getResult());
    }

    @GetMapping("/allApprovers")
    @ApiOperation(value = "获取所有用户/组", notes = "获取系统中所有进行中的流程 和 最新的流程 所有的用户和组")
    public ApiResult getApproverAndRoleList(){
        return ApiResult.success().data(flowableTaskService.getApproverAndRoleList());
    }

    @ApiOperation(value = "转办任务")
    @PutMapping(value = "/turnTask")
    public ApiResult assign(@RequestBody TurnTaskVo turnTaskVo) {
        flowableTaskService.turnTask(turnTaskVo);
        return ApiResult.success();
    }

    @ApiOperation(value = "委派任务")
    @PutMapping(value = "/delegate")
    public ApiResult delegate(@RequestBody DelegateTaskVo delegateTaskVo) {
        flowableTaskService.delegateTask(delegateTaskVo);
        return ApiResult.success();
    }

    @ApiOperation(value = "签收任务")
    @PutMapping(value = "/claim")
    public ApiResult claim(@RequestBody ClaimTaskVo claimTaskVo) {
        flowableTaskService.claimTask(claimTaskVo);
        return ApiResult.success();
    }

    @ApiOperation(value = "取消签收任务")
    @PutMapping(value = "/unClaim")
    public ApiResult unClaim(@RequestBody ClaimTaskVo claimTaskVo) {
        flowableTaskService.unClaimTask(claimTaskVo);
        return ApiResult.success();
    }

    /*@ApiOperation(value = "完成任务")
    @PutMapping(value = "/complete")
    public ApiResult complete(@RequestBody TaskRequest taskRequest) {
        flowableTaskService.completeTask(taskRequest);
        return ApiResult.success();
    }*/

    @ApiOperation(value = "结束流程实例")
    @PutMapping(value = "/stopProcessInstance")
    public ApiResult stopProcessInstance(@RequestBody EndProcessVo endProcessVo) {
        flowableTaskService.stopProcessInstance(endProcessVo);
        return ApiResult.success();
    }

    @ApiOperation("查询可退回节点")
    @GetMapping(value = "/backNodes")
    public ApiResult backNodes(@RequestParam String taskId) {
        List<FlowNodeVo> datas = flowableTaskService.getBackNodes(taskId);
        return ApiResult.success(datas);
    }

    @ApiOperation(value = "退回任务")
    @PutMapping(value = "/back")
    public ApiResult back(@RequestBody BackTaskVo backTaskVo) {
        flowableTaskService.backTask(backTaskVo);
        return ApiResult.success();
    }

    @ApiOperation("任务已阅")
    @PutMapping(value = "/read")
    public ApiResult read(@RequestBody ReadTaskVo readTaskVo) {
        flowableTaskService.readTask(readTaskVo);
        return ApiResult.success();
    }
}
