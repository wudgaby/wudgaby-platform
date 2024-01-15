package com.wudgaby.starter.resource.scan.scanner;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.wudgaby.platform.security.core.annotations.AnonymousAccess;
import com.wudgaby.starter.resource.scan.consts.ApiScanConst;
import com.wudgaby.starter.resource.scan.enums.ApiStatus;
import com.wudgaby.starter.resource.scan.enums.ApiType;
import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
import com.wudgaby.starter.resource.scan.service.ApiRegisterService;
import com.wudgaby.starter.resource.scan.util.AopTargetUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 资源扫描器
 * @author
 */
@Slf4j
@RequiredArgsConstructor
public class ApiResourceScanner implements BeanPostProcessor {
    private final ApiRegisterService apiRegisterService;
    @Value("${spring.application.name:unknown_app}")
    public String serviceName;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 如果controller是代理对象,则需要获取原始类的信息
        Object aopTarget = AopTargetUtils.getTarget(bean);
        if (aopTarget == null) {
            aopTarget = bean;
        }
        Class<?> clazz = aopTarget.getClass();

        // 判断是不是控制器,不是控制器就略过
        boolean controllerFlag = getControllerFlag(clazz);
        if (!controllerFlag) {
            return bean;
        }
        if (clazz == BasicErrorController.class) {
            return bean;
        }

        // 扫描控制器的所有带ApiResource注解的方法
        List<ResourceDefinition> apiResources = doScan(clazz);

        // 将扫描到的注解转化为资源实体存储到缓存
        apiRegisterService.batchRegister(apiResources);
        return bean;
    }

    /**
     * 判断一个类是否是控制器
     *
     * @author fengshuonan
     * @date 2020/12/9 11:21
     */
    private boolean getControllerFlag(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (RestController.class.equals(annotation.annotationType()) || Controller.class.equals(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 扫描整个类中包含的所有@Api资源
     */
    private List<ResourceDefinition> doScan(Class<?> clazz) {
        ArrayList<ResourceDefinition> apiResources = new ArrayList<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        ApiIgnore apiIgnoreType = AnnotationUtils.findAnnotation(clazz, ApiIgnore.class);

        int ignoreMethodCount = 0;
        for (Method declaredMethod : declaredMethods) {
            ApiIgnore apiIgnoreMethod = AnnotationUtils.findAnnotation(declaredMethod, ApiIgnore.class);
            if(apiIgnoreMethod != null) {
                ignoreMethodCount++;
                continue;
            }

            if(apiIgnoreType != null){
                ignoreMethodCount++;
                continue;
            }

            ResourceDefinition definition = createButtonDefinition(clazz, declaredMethod);
            apiResources.add(definition);
        }

        //忽略注册. 如果没有方法,或者全部方法都被忽略
        boolean ignoreRegister = false;
        boolean allIgnoreForMethods = apiIgnoreType != null && ArrayUtil.length(declaredMethods) == ignoreMethodCount;
        if(ArrayUtil.isEmpty(declaredMethods) || allIgnoreForMethods){
            ignoreRegister = true;
        }

        if(ignoreRegister){
            log.info("====== " + clazz.getSimpleName() + " 忽略注册.");
        }else{
            ResourceDefinition menuDefinition = createMenuDefinition(clazz);
            log.info("====== " + clazz.getSimpleName() + " 完成初始化【" + menuDefinition.getName() + "】模块API资源扫描,共:" + apiResources.size() + "个接口");

            apiResources.add(menuDefinition);
        }

        return apiResources;
    }

    private ResourceDefinition createMenuDefinition(Class<?> clazz) {
        String menuName = null;
        Api apiTag = AnnotationUtils.findAnnotation(clazz, Api.class);
        if (apiTag != null && ArrayUtil.isNotEmpty(apiTag.tags()) && StringUtils.isNotBlank(apiTag.value())) {
            menuName = Optional.ofNullable(apiTag.tags()).map(Arrays::toString).orElse(apiTag.value());
        }
        if (StringUtils.isBlank(menuName)) {
            menuName = getModularCode(clazz.getSimpleName());
        }
        menuName = StrUtil.toUnderlineCase(menuName);

        RequestMapping requestMappingTypeAnno = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
        String prePath = processPath(Optional.ofNullable(requestMappingTypeAnno).map(RequestMapping::path).orElse(new String[0]));
        ResourceDefinition resourceDefinition = new ResourceDefinition()
                .setCode(MD5.create().digestHex16(serviceName + menuName + prePath))
                .setMethod(StringUtils.EMPTY)
                .setUri(prePath)
                .setType(ApiType.MENU.name())
                .setServiceName(serviceName)
                .setStatus(ApiStatus.ENABLED)
                .setName(menuName)
                .setAuth(false)
                .setOpen(true)
                .setClassName(clazz.getSimpleName())
                .setMethodName(StringUtils.EMPTY)
                .setDesc(Optional.ofNullable(apiTag).map(Api::description).orElse(StringUtils.EMPTY))
                .setPermissionCode(getMenuPermissionCode(clazz.getSimpleName()));

        return resourceDefinition;
    }

    /**
     * 根据类信息，方法信息，注解信息创建ResourceDefinition对象
     */
    private ResourceDefinition createButtonDefinition(Class<?> clazz, Method method) {
        ApiOperation apiOperation = AnnotationUtils.findAnnotation(method, ApiOperation.class);
        String methodName = Optional.ofNullable(apiOperation).map(ApiOperation::value).orElse(method.getName());

        // 是否需要登录认证
        AnonymousAccess anonymousAccessType = AnnotationUtils.findAnnotation(clazz, AnonymousAccess.class);
        AnonymousAccess anonymousAccessMethod = AnnotationUtils.findAnnotation(method, AnonymousAccess.class);
        boolean isAuth = anonymousAccessType == null;
        if (anonymousAccessMethod != null) {
            isAuth = false;
        }

        RequestMapping requestMappingTypeAnno = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
        String prePath = processPath(Optional.ofNullable(requestMappingTypeAnno).map(RequestMapping::path).orElse(new String[0]));
        String url = getUrl(prePath, method);
        String reqMethod = getMethod(method);
        ResourceDefinition resourceDefinition = new ResourceDefinition()
                .setCode(MD5.create().digestHex16(serviceName + method + url))
                .setMethod(reqMethod)
                .setUri(url)
                .setType(ApiType.BUTTON.name())
                .setServiceName(serviceName)
                .setStatus(ApiStatus.ENABLED)
                .setName(methodName)
                .setAuth(isAuth)
                .setOpen(true)
                .setClassName(clazz.getSimpleName())
                .setMethodName(method.getName())
                .setDesc(Optional.ofNullable(apiOperation).map(ApiOperation::notes).orElse(StringUtils.EMPTY))
                .setPermissionCode(getPermissionCode(clazz.getSimpleName(), method.getName()));

        return resourceDefinition;
    }

    private String getUrl(String prePath, Method method) {
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);

        String requestUrl = "";
        if (ArrayUtil.isAllNull(requestMapping, postMapping, getMapping, deleteMapping, putMapping)) {
            requestUrl = prePath;
        }else{
            // 获取URL
            if (requestMapping != null && requestMapping.path().length > 0) {
                requestUrl = prePath + processPath(requestMapping.path());
            } else if (postMapping != null && postMapping.path().length > 0) {
                requestUrl = prePath + processPath(postMapping.path());
            } else if (getMapping != null && getMapping.path().length > 0) {
                requestUrl = prePath + processPath(getMapping.path());
            } else if (putMapping != null && putMapping.path().length > 0) {
                requestUrl = prePath + processPath(putMapping.path());
            } else if (deleteMapping != null && deleteMapping.path().length > 0) {
                requestUrl = prePath + processPath(deleteMapping.path());
            }
        }
        return requestUrl.replaceAll("/+", "/");
    }

    private String getMethod(Method method) {
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);

        String requestMethod = RequestMethod.GET.name();
        if (requestMapping != null && requestMapping.method().length > 0) {
            requestMethod = requestMapping.method()[0].name();
        } else if (postMapping != null) {
            requestMethod = RequestMethod.POST.name();
        } else if (getMapping != null) {
            requestMethod = RequestMethod.GET.name();
        } else if (putMapping != null) {
            requestMethod = RequestMethod.PUT.name();
        } else if (deleteMapping != null) {
            requestMethod = RequestMethod.DELETE.name();
        }
        return requestMethod;
    }


    private String getPermissionCode(String className, String methodName){
        return StrUtil.toUnderlineCase(serviceName)
                + ApiScanConst.LINK_SYMBOL + StrUtil.toUnderlineCase(getModularCode(className))
                + ApiScanConst.LINK_SYMBOL + StrUtil.toUnderlineCase(methodName)
                ;
    }

    private String getMenuPermissionCode(String className){
        return StrUtil.toUnderlineCase(serviceName)
                + ApiScanConst.LINK_SYMBOL + StrUtil.toUnderlineCase(getModularCode(className))
                ;
    }

    private String getModularCode(String className){
        // 填充模块编码，模块编码就是控制器名称截取Controller关键字前边的字符串
        int controllerIndex = className.indexOf("Controller");
        if (controllerIndex == -1) {
            log.error(StrUtil.format("{} 命令不规范.请以Controller结尾", className));
            return "";
        }
        return className.substring(0, controllerIndex);
    }

    /**
     * 获取到配置的path
     */
    private String processPath(String[] paths) {
        if (ArrayUtil.isEmpty(paths) || StringUtils.isEmpty(paths[0])) {
            return "";
        } else {
            String path = paths[0];
            // 保证前面有“/”后面没有
            if (!paths[0].startsWith("/")) {
                path = "/" + path;
            }
            if (paths[0].endsWith("}")) {
                int index = path.indexOf("{");
                path = path.substring(0, index - 1);
            }
            return path;
        }
    }
}
