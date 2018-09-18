package com.semicolon.rests.service;

import com.semicolon.rests.models.BanksModel;
import com.semicolon.rests.models.CityModel;
import com.semicolon.rests.models.ResponsModel;
import com.semicolon.rests.models.SocialContactModel;
import com.semicolon.rests.models.TermsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Services {

    @GET("AppUser/Cites")
    Call<List<CityModel>> getCity();


    @GET("AboutApp/TermsAndConditions")
    Call<TermsModel> getTermsAndConditions();


    @GET("AboutApp/Banks")
    Call<List<BanksModel>> getBanks();


    @GET("AboutApp/SocialMedia")
    Call<SocialContactModel> getContacts();

    @FormUrlEncoded
    @POST("AboutApp/ContactUs")
    Call<ResponsModel> sendProblemViaContact(@Field("name") String name,
                                             @Field("email") String email,
                                             @Field("subject") String subject,
                                             @Field("message") String messag);
}
