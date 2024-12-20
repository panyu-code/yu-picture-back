package com.panyu.yupictureback;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YuPictureBackApplicationTests {


    @Test
    void contextLoads() {
    }


    @Test
    void test1(){
        String s = RandomUtil.randomString(4);
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomUtil.randomString("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 4));
        }
    }

    @Test
    void importData(){
        int num = 3000;
        for (int i = 0; i < 1000; i++) {
            num++;
            System.out.println(num);
        }
    }
}
