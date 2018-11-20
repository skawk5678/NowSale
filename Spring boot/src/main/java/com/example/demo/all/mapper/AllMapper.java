package com.example.demo.all.mapper;

import com.example.demo.all.model.MenuVO;
import com.example.demo.client.model.ClientCouponVO;
import com.example.demo.client.model.ClientVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("com.example.demo.all.mapper.AllMapper")
@Mapper
public interface AllMapper {
    //광고판 이미지 갯수
    @Select("SELECT * FROM adv_img_cnt")
    int getAdvImgCnt();

    //단골 숫자 세는 것
    @Select("SELECT count(*) FROM favorite_market_list WHERE owner_key=#{owner_key}")
    int getFavoriteCount(@Param("owner_key")int owner_key);

    //해당 오너 정보를 누른 client가 단골인지 아닌지 체크
    @Select("SELECT client_key FROM favorite_market_list WHERE client_key=#{client_key} AND owner_key=#{owner_key}")
    List<Integer> isFavorite(@Param("owner_key") int owner_key, @Param("client_key") int client_key);

    @Select("SELECT id FROM client_list WHERE id=#{id}")
    String clientOverlapId(String id);

    @Select("SELECT id FROM owner_list WHERE id=#{id}")
    String ownerOverlapId(String id);

    @Select("SELECT market_img_cnt FROM owner_list WHERE owner_key=#{owner_key}")
    int getMarketImgCount(@Param("owner_key")int owner_key);

    @Select("SELECT count(*) FROM owner_menu_list WHERE owner_key=#{owner_key}")
    int getMenuImgCount(@Param("owner_key")int owner_key);

    @Select("SELECT menu_img_name,menu_name,menu_money FROM owner_menu_list WHERE owner_key=#{owner_key}")
    List<MenuVO> getMenuItem(@Param("owner_key")int owner_key);

    @Select("SELECT count(*) FROM client_list WHERE ID=#{email}")
    int isClientExistEmail(String email);

    @Select("SELECT count(*) FROM owner_list WHERE ID=#{email}")
    int isOwnerExistEmail(String email);
}

