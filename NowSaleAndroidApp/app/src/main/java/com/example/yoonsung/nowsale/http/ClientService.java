package com.example.yoonsung.nowsale.http;

import com.example.yoonsung.nowsale.VO.ClientCouponVO;
import com.example.yoonsung.nowsale.VO.ClientVO;
import com.example.yoonsung.nowsale.VO.CouponVO;
import com.example.yoonsung.nowsale.VO.LoginVO;

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

public interface ClientService {
    @GET("/all/favorite/is/{owner_key}/{client_key}")
    Call<String> isFavorite(@Path("owner_key") int owner_key, @Path("client_key") int client_key);

    @POST("/client/login")
    Call<List<ClientVO>> isLogin(@Body LoginVO loginVO);

    @GET("/client/coupon/have/{client_key}")
    Call<List<CouponVO>> getClientCouponList(@Path("client_key") int client_key);

    @HTTP(method = "DELETE",path="/client/coupon/delete",hasBody=true)
    Call<List<ClientCouponVO>> deleteClientCouponList(@Body ClientCouponVO clientCouponVO);

    @POST("/client/coupon/insert")
    Call<List<ClientCouponVO>> insertClientCouponList(@Body ClientCouponVO clientCouponVO);
}