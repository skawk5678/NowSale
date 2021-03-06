package com.chapchapbrothers.nowsale.http;

import com.chapchapbrothers.nowsale.VO.ClientCouponVO;
import com.chapchapbrothers.nowsale.VO.ClientSaleVO;
import com.chapchapbrothers.nowsale.VO.ClientVO;
import com.chapchapbrothers.nowsale.VO.CouponVO;
import com.chapchapbrothers.nowsale.VO.LoginVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("/client/sale/have/{client_key}")
    Call<List<CouponVO>> getClientSaleList(@Path("client_key")int client_key);

    @HTTP(method = "DELETE",path="/client/coupon/use",hasBody=true)
    Call<ClientCouponVO> useClientCouponList(@Body ClientCouponVO clientCouponVO);

    @HTTP(method = "DELETE",path="/client/coupon/delete",hasBody=true)
    Call<ClientCouponVO> deleteClientCouponList(@Body ClientCouponVO clientCouponVO);

    @HTTP(method = "DELETE",path="/client/sale/delete",hasBody=true)
    Call<ClientSaleVO> deleteClientSaleList(@Body ClientSaleVO clientSaleVO);

    @HTTP(method = "DELETE",path="/client/info/delete/{client_key}",hasBody=true)
    Call<ClientVO> deleteClient(@Path("client_key")int client_key);

    @POST("/client/coupon/insert")
    Call<ClientCouponVO> insertClientCouponList(@Body ClientCouponVO clientCouponVO);

    @POST("/client/sale/insert")
    Call<Void> insertClientSaleList(@Body ClientSaleVO clientSaleVO);

    @GET("/client/favorite/market/{client_key}")
    Call<List<CouponVO>> getClientFavoriteMarket(@Path("client_key") int client_key);

    @PUT("/client/info/update/{client_key}")
    Call<Void> updateClientInfo(@Path("client_key")int client_key,@Body ClientVO clientVO);

    @POST("/client/signup")
    Call<Void> signUpClient(@Body ClientVO clientVO);

}
