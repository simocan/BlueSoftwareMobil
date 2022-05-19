package com.bluesoft.barkod.network;

import com.bluesoft.barkod.request.CommonActionRequest;
import com.bluesoft.barkod.request.MobilActionRequest;
import com.bluesoft.barkod.response.ActionResponse;
import com.bluesoft.barkod.response.MobilActionResponse;
import com.bluesoft.barkod.response.SanalBarkodResponse;
import com.bluesoft.barkod.response.UserResponse;
import com.bluesoft.barkod.request.UserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {
    @POST("auth/authenticate")
    Call<UserResponse> login(@Body UserRequest user);


    @POST("api/v3/toUserIslemAction")
    Call<MobilActionResponse> toUserIslemAction(@Body MobilActionRequest request,@Header("Authorization") String token);

    @POST("api/v3/commonAction")
    Call<ActionResponse> getCommonAction(@Body CommonActionRequest request, @Header("Authorization") String token);


    @GET("api/v3/detay/{sanalBarkodNo}/sanalbarkod")
    Call<SanalBarkodResponse> getSanalBarkodDetay(@Path("sanalBarkodNo") Long sanalBarkodNo, @Header("Authorization") String token);

}
