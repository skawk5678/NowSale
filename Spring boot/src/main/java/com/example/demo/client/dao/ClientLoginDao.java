package com.example.demo.client.dao;

import com.example.demo.client.model.ClientVO;
import com.example.demo.client.model.ClientLoginVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ClientLoginDao {
    private SqlSessionFactory sqlSessionFactory;

    private ClientLoginVO clientLoginVO;
    public ClientLoginDao(ClientLoginVO clientLoginVO){
        InputStream is = null;
        try {
            is = Resources.getResourceAsStream(
                    "myBatisConfiguration.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

            this.clientLoginVO = clientLoginVO;
//            sqlSessionFactoryBuilder은 만듦과 동시에 builder함수만 쓰고 갖다버림


//            .build(is)하자마자 바로 뒤짐


//            언제든지 세션을 open하기 위해서는 sqlSessionFactory는 계속 있어야됨

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public List<ClientVO> clientLoginSelect(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
//            Map<String, ClientLoginVO> map = sqlSession.selectMap("dao.mybatisMapper.selectEveryClientIdPw","id");
//            String client = String.valueOf(map.get("qwe").getPw());
//            String clientPW = sqlSession.selectOne("dao.mybatisMapper.selectEveryClientIdPw",clientLoginVO);
//            System.out.println("clientPW : "+clientPW);
            List<ClientVO> list = sqlSession.selectList("dao.mybatisMapper.selectClientLogin",clientLoginVO);


            sqlSession.commit();
            return list;

        }
        finally {
            sqlSession.close();
        }
    }
}
