package com.youkeda.app.control;

import com.youkeda.app.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/code")
public class SMSCodeApi {
    private static Logger logger = LoggerFactory.getLogger(SMSCodeApi.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RequestMapping(path = "/send")
    @ResponseBody
    public Result send(@RequestParam("mobile") String mobile) {
        Result result = new Result<>();
        result.setSuccess(true);

        // 步骤一
        if (StringUtils.isEmpty(mobile)) {
            result.setSuccess(false);
            result.setMessage("手机号信息不能为空！");
            return result;
        }
        // 生成四位数验证码
        String code = String.valueOf((int)((Math.random() * 10) * 1000));

        //  步骤二
        // 调用第三方发送服务(暂不用实现)
        Boolean sendResult = sendMobileCode(mobile, code);
        if(!sendResult) {
            result.setSuccess(false);
            result.setMessage("发送验证码失败！");
            return result;
        }

        // 步骤三
        stringRedisTemplate.opsForValue().set(mobile, code);
        // 将手机号对应验证码存储到redis里
        return result;
    }

    /**
     * 阿里云调用服务
     *
     * @param mobile 手机号
     * @param code   验证码
     */
    private boolean sendMobileCode(String mobile, String code) {
        logger.info("mobile is:" + mobile + "code is:" + code);
        return true;
    }
}


