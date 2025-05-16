package com.panyu.yupictureback;

import com.panyu.yupictureback.mapper.PictureMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class YuPictureBackApplicationTests {

    @Resource
    private PictureMapper pictureMapper;
   @Test
    void contextLoads() {
   }
}
