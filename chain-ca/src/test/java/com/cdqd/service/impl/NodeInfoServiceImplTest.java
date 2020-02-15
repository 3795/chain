package com.cdqd.service.impl;

import com.cdqd.service.NodeInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class NodeInfoServiceImplTest {


    @Autowired
    private NodeInfoService nodeInfoService;

    @Test
    public void auth() {
        nodeInfoService.auth(1, "01", "111", "151", 1);
    }

    @Test
    public void queryPublicKey() {
        System.out.println(nodeInfoService.queryPublicKey(01));
    }
}