package com.wudi.td;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Ignore
class TdApplicationTests {

    @Test
    void contextLoads() {
        String hashAlgorithmName = "MD5";
        String credentials = "123456";
        Object salt = ByteSource.Util.bytes("123456");
        int hashIterations = 3;
        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);

    }

}
