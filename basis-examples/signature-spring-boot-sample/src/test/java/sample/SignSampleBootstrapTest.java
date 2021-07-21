package sample;

import com.google.common.collect.Maps;
import com.wudgaby.platform.utils.FastJsonUtil;
import com.wudgaby.sign.api.SignConst;
import com.wudgaby.sign.api.SignatureHeaders;
import com.wudgaby.sign.api.SignatureUtils;
import com.wudgaby.sign.api.SignatureVo;
import com.wudgaby.sign.supoort.RequestCachingFilter;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sample.vo.Order;

import java.util.Map;
import java.util.UUID;

/**
 * @ClassName : SignSampleBootstrapTest
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/21 3:40
 * @Desc :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SignSampleBootstrapTest {
    @Autowired
    private WebApplicationContext context;
    //直接使用会导致过滤器一些东西丢失
    private MockMvc mockMvc;

    @Before
    @BeforeEach
    public void before() throws Exception {
        //获取mockmvc对象实例
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilter(new RequestCachingFilter())
                .build();
    }

    private HttpHeaders getHeaders(SignatureVo signatureVo){
        String tm = String.valueOf(System.currentTimeMillis());
        String nonce = UUID.randomUUID().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(SignatureHeaders.HEADER_APP_KEY, "appKey-demo");
        headers.set(SignatureHeaders.HEADER_NONCE, nonce);
        headers.set(SignatureHeaders.HEADER_TIMESTAMP, tm);
        headers.setContentType(MediaType.APPLICATION_JSON);

        SignatureHeaders signatureHeaders = new SignatureHeaders();
        signatureHeaders.setAppKey("appKey-demo");
        signatureHeaders.setNonce(nonce);
        signatureHeaders.setTimestamp(tm);

        signatureVo.setSignatureHeaders(signatureHeaders);
        signatureVo.setSecret(SignConst.DEFAULT_SECRET);

        String sign = SignatureUtils.sign(signatureVo);
        System.out.println(sign);
        headers.set(SignatureHeaders.HEADER_SIGNATURE, sign);
        return headers;
    }

    @Test
    void getOrder() throws Exception {
        Map<String, String[]> params = Maps.newHashMap();
        params.put("name", new String[]{"iphone"});
        params.put("amount", new String[]{"1"});

        SignatureVo signatureVo = new SignatureVo();
        signatureVo.setPath("/orders/order1");
        signatureVo.setContentType(MediaType.APPLICATION_JSON_VALUE);
        signatureVo.setHttpMethod("GET");
        signatureVo.setParams(params);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/orders/order1?name=iphone&amount=1")
                .headers(getHeaders(signatureVo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println("输出 " + mvcResult.getResponse().getContentAsString());
    }

    @Test
    void saveOrder() throws Exception {
        Order order = new Order();
        order.setAmount(1);
        order.setName("iphone");
        order.setPrice(4000d);

        SignatureVo signatureVo = new SignatureVo();
        signatureVo.setPath("/orders");
        signatureVo.setContentType(MediaType.APPLICATION_JSON_VALUE);
        signatureVo.setHttpMethod("POST");

        String body = FastJsonUtil.collectToString(order);
        System.out.println(body);
        System.out.println(SignatureUtils.buildContentMd5(body));
        signatureVo.setContentMD5(SignatureUtils.buildContentMd5(body));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .headers(getHeaders(signatureVo)).content(FastJsonUtil.collectToString(order)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println("输出 " + mvcResult.getResponse().getContentAsString());
    }
}