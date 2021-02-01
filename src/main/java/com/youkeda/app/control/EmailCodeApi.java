package com.youkeda.app.control;

import com.youkeda.app.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/email/code")
public class EmailCodeApi {

    private static Logger logger = LoggerFactory.getLogger(EmailCodeApi.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JavaMailSender mailSender;
    @Value("${sendEmailAddress}")
    private String sendEmailAddress;
    private static String EMAIL_EMPTY = "100001";

    @RequestMapping(path = "/send")
    @ResponseBody
    public Result<SimpleMailMessage> sendMail(@RequestParam("email") String email) {
        Result<SimpleMailMessage> result = new Result<>();
        result.setSuccess(true);
        if (StringUtils.isEmpty(email)) {
            result.setSuccess(false);
            result.setMessage("邮箱地址不能为空");
            result.setCode(EMAIL_EMPTY);
            return result;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        String code =String.valueOf((int)(Math.random() * 1000000));
        if (StringUtils.isEmpty(sendEmailAddress)) {
            result.setSuccess(false);
            result.setMessage("发件人邮箱地址不能为空");
            return result;
        }
        message.setFrom(sendEmailAddress);
        message.setTo(email);
        message.setSubject("发送验证码");
        message.setText(code);
        mailSender.send(message);
        result.setSuccess(true);
        result.setData(message);
        stringRedisTemplate.opsForValue().set(email,code);
        return result;
    }

    @RequestMapping(path = "/verificate")
    @ResponseBody
    public Result verificateCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        Result result = new Result<>();
        result.setSuccess(true);
        if(StringUtils.isEmpty(email)){
            result.setSuccess(false);
            result.setCode("100001");
            result.setMessage("邮箱为空");
            return result;
        }

        if(StringUtils.isEmpty(code)){
            result.setSuccess(false);
            result.setCode("100002");
            result.setMessage("验证码为空");
            return result;
        }

        String existcode = stringRedisTemplate.opsForValue().get(email);
        if(StringUtils.isEmpty(existcode)){
            result.setSuccess(false);
            result.setMessage("从Redis里未取出对应的验证码");
            return result;
        }

        if(!existcode.equals(code)){
            result.setSuccess(false);
            result.setCode("100003");
            result.setMessage("邮箱验证码校验失败");
            return result;
        }
        logger.info(email + "校验成功");
        return result;
    }

}
