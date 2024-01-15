package com.wudgaby.starter.ipaccess;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.wudgaby.starter.ipaccess.enums.BwType;
import com.wudgaby.starter.ipaccess.enums.StrategyType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2022.02.23
 * @Desc :  ip黑白名单过滤
 */
@Slf4j
@AllArgsConstructor
public class IpAccessFilter implements Filter {
    private final StrategyType strategy;
    private static final String[] ALLOW_IPS = new String[]{"0:0:0:0:0:0:0:1", "127.0.0.1"};

    @Override
    public void init(FilterConfig filterConfig){
        log.info("[IP限制模式] : {}", strategy);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        TimeInterval timer = DateUtil.timer();

        boolean pass;
        String reqIp = ServletUtil.getClientIP(httpServletRequest);
        if(ArrayUtil.contains(ALLOW_IPS, reqIp)){
            pass = true;
        } else {
            pass = isPass(reqIp);
        }

        log.info("IP访问控制处理结束. 请求IP: <{}> <{}> 花费时间: {} MS", reqIp, pass ? "通过" : "限制", timer.interval());

        if(pass) {
            chain.doFilter(request,response);
        } else{
            httpServletResponse.setStatus(HttpStatus.OK.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setCharacterEncoding("utf-8");

            Map<String, Object> resultMap = Maps.newTreeMap();
            resultMap.put("code", 0);
            resultMap.put("message", reqIp + ". 该IP已被限制访问.");
            resultMap.put("data", null);
            httpServletResponse.getWriter().write(JSONUtil.toJsonStr(resultMap));
            httpServletResponse.getWriter().flush();
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * @param ip
     * @return
     */
    private boolean isPass(String ip){
        switch (strategy) {
            case AUTHORITY_BLACK:
                return isPassByBlack(ip);
            case AUTHORITY_WHITE:
                return isPassByWhite(ip);
            case AUTHORITY_MIXTURE:
            default:
                return isPassByMixture(ip);
        }
    }

    private boolean isPassByBlack(String ip){
        IpManage ipManage = SpringUtil.getBean(IpManage.class);
        boolean contain = ipManage.containIp(ip, BwType.BLACK);
        return contain ? false : true;
    }

    private boolean isPassByWhite(String ip){
        IpManage ipManage = SpringUtil.getBean(IpManage.class);
        boolean contain = ipManage.containIp(ip, BwType.WHITE);
        return contain ? true : false;
    }

    /**
     * 白名单存在.直接放行
     * 黑名单存在.不放行.
     * 黑名单不存在. 默认放行
     * @param ip
     * @return
     */
    private boolean isPassByMixture(String ip){
        IpManage ipManage = SpringUtil.getBean(IpManage.class);
        boolean white = ipManage.containIp(ip, BwType.WHITE);
        if(white){
            return true;
        }

        boolean black = ipManage.containIp(ip, BwType.BLACK);
        if(black){
            return false;
        }

        return true;
    }

}
