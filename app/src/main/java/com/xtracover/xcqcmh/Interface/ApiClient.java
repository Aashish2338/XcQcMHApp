package com.xtracover.xcqcmh.Interface;

import com.xtracover.xcqcmh.Models.LoginResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiClient {

    @FormUrlEncoded
    @POST("warrantybazzarLogindeatil")
    Single<LoginResponse> getUsersAccountLogin(@Field("EmpCode") String uerName, @Field("password") String password);

}
