package com.sg.vue.controller;

import com.sg.vue.model.dto.MyUser;
import com.sg.vue.dao.MyUserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/db")
public class DBController {

    @Resource
    MyUserMapper mapper;

    @GetMapping("/batchCreate/{num}")
    public void batchCreate(@PathVariable(value = "num") int num) {

        for (int i = 0; i < num; i++) {
            MyUser p = new MyUser();
            StringBuilder sb2 = new StringBuilder();
            int jj = (int) (Math.random() * 8) + 1;
            for (int j = 0; j < jj; j++) {
                sb2.append(getRandomChar());
            }

            if (jj / 2 == 0) {
                p.setFlag("02");
            }
            p.setName(sb2.toString());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
//            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i);//让日期加1
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + i * 32);
            calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) + i * 8);

            p.setUpdateTime(calendar.getTime());

            mapper.insertSelective(p);
        }


    }


    private static char getRandomChar() {
        String str = "";
        int hightPos;
        int lowPos;
        Random random = new Random();
        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();
        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }
        return str.charAt(0);
    }


}
