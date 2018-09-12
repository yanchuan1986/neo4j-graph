package com.dx.graph.neo4j.test;

import com.dx.graph.neo4j.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserServiceTest {


    /**
     * 因为是通过http连接到Neo4j数据库的，所以要预先启动Neo4j：neo4j console
     */
    @Test
    public void testInitData() {

    }

    @Test
    public void testGetUserByName() {

    }
}
