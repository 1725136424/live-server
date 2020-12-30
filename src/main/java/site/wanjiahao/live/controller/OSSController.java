package site.wanjiahao.live.controller;

import cn.hutool.http.HttpStatus;
import com.aliyun.oss.OSS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import site.wanjiahao.live.utils.CommonUtils;
import site.wanjiahao.live.utils.R;
import site.wanjiahao.live.utils.oss.OSSUtils;
import site.wanjiahao.live.entity.PolicyEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/oss")
@Slf4j
public class OSSController {

    @Value("${alibaba.cloud.oss.bucket-name}")
    private String bucket;

    @Autowired
    private OSS ossClient;

    @Value("${alibaba.cloud.access-key}")
    private String accessId;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    @Value("${alibaba.cloud.oss.callback}")
    private String callback;

    // 获取Token
    @GetMapping("/policy")
    public R policy() {
        try {
            PolicyEntity policyEntity = OSSUtils.buildOSSTokenObject(ossClient, accessId, bucket, endpoint, null);
            return R.ok().put("data", policyEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("oss获取token接口错误{}", e.getMessage());
            return R.error();
        }
    }

    // 回调接口 OOS发送post请求，只接受post请求
    @PostMapping("/callback")
    public Map<String, Object> callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 解析OSS传递过来的文件信息
        String ossCallbackBody = OSSUtils.GetPostBody(request.getInputStream(),
                Integer.parseInt(request.getHeader("content-length")));
        // 验证签名的正确性
        boolean ret = OSSUtils.VerifyOSSCallbackRequest(request, ossCallbackBody);
        // 解析请求体数据，返回 包括一些文件信息
        Map<String, Object> resultMap = CommonUtils.parseUrl(CommonUtils.URLDecoderString(ossCallbackBody));
        if (ret) {
            // 设置响应码 (特别注意的是此响应数据是返回给OSS服务器的，OSS根据返回的响应编码来判断，验证的正确性。而请求体的数据OSS会直接返回给上传文件的请求)
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return resultMap;
    }
}
