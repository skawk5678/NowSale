package com.chapchapbrothers.nowsale.http;

import com.chapchapbrothers.nowsale.AdvImgVO;
import com.chapchapbrothers.nowsale.VO.AllOwnerClientKeyVO;
import com.chapchapbrothers.nowsale.VO.ClientVO;
import com.chapchapbrothers.nowsale.VO.CouponVO;
import com.chapchapbrothers.nowsale.VO.DangolWithMarketMenuImg;
import com.chapchapbrothers.nowsale.VO.LoginVO;
import com.chapchapbrothers.nowsale.VO.MenuVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by yoonsung on 2018. 10. 8..
 */
public interface AllService {
    @GET("/all/adv/img")
    Call<List<AdvImgVO>> getAdvUri();

    @GET("/client/all")
    Call<List<ClientVO>> getUserRepositories(/*@Path("user") String userName*/);

    @GET("/all/category/coupon/get/{category}")
    Call<List<CouponVO>> getCategoryCoupon(@Path("category") String category);

    @GET("/all/category/sale/get/{category}")
    Call<List<CouponVO>> getCategorySale(@Path("category")String category);

    @GET("/all/market/list/get/{category}")
    Call<List<CouponVO>> getCategoryMarket(@Path("category")String category);

    @GET("/all/favorite/get/count/{owner_key}")
    Call<Integer> getFavoriteCount(@Path("owner_key") int owner_key);

    @GET("/all/favorite/is/get/count/{owner_key}/{client_key}")
    Call<DangolWithMarketMenuImg> dangolwithImg(@Path("owner_key") int owner_key, @Path("client_key") int client_key);

    @HTTP(method = "DELETE",path="/all/favorite/delete",hasBody = true)
    Call<String> deleteFavorite(@Body AllOwnerClientKeyVO allOwnerClientKeyVO);

    @POST("/all/favorite/insert")
    Call<String> insertFavorite(@Body AllOwnerClientKeyVO allOwnerClientKeyVO);

    @GET("/all/overlap/{id}")
    Call<String> checkAllOverlap(@Path("id")String id);

    @GET("/all/{owner_key}/menu")
    Call<List<MenuVO>> getMenuList(@Path("owner_key")int owner_key);

    @POST("/all/find/password")
    Call<Void> findPassword(@Body LoginVO loginVO);

}
