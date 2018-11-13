package com.semicolon.rests.service;

import com.semicolon.rests.models.BanksModel;
import com.semicolon.rests.models.CityModel;
import com.semicolon.rests.models.NotificationCountModel;
import com.semicolon.rests.models.Notification_Model;
import com.semicolon.rests.models.PlacesModel;
import com.semicolon.rests.models.ResponsModel;
import com.semicolon.rests.models.SocialContactModel;
import com.semicolon.rests.models.TermsModel;
import com.semicolon.rests.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
                                             @Field("message") String message);

    @FormUrlEncoded
    @POST("AppUser/SignUp")
    Call<UserModel> register(@Field("user_pass")String user_pass,
                             @Field("user_phone")String user_phone,
                             @Field("user_full_name")String user_full_name,
                             @Field("user_token_id")String user_token_id,
                             @Field("user_google_lat")String user_google_lat,
                             @Field("user_google_long")String user_google_long,
                             @Field("user_email")String user_email,
                             @Field("user_city")String user_city

    );

    @FormUrlEncoded
    @POST("AppUser/Login")
    Call<UserModel> login(@Field("user_email_phone")String user_email_phone,
                             @Field("user_pass")String user_pass);

    @GET("AppUser/Logout/{user_id}")
    Call<ResponsModel> logout(@Path("user_id") String user_id);

    @GET("Api/Places/{user_id}/{type}")
    Call<List<PlacesModel>> getPlaces(@Path("user_id") String user_id,@Path("type") String type);

    @FormUrlEncoded
    @POST("AppUser/UpdateLocation/{user_id}")
    Call<ResponsModel> updateLocation(@Path("user_id") String user_id,
                                      @Field("user_google_lat") double user_google_lat,
                                      @Field("user_google_long") double user_google_long );

    @FormUrlEncoded
    @POST("AppUser/UpdateTokenId/{user_id}")
    Call<ResponsModel> updateToken(@Path("user_id") String user_id,@Field("user_token_id") String user_token_id);

    @Multipart
    @POST("Api/Reservation")
    Call<ResponsModel> reserve(@Part("place_id") RequestBody place_id,
                               @Part("user_id") RequestBody user_id,
                               @Part("reservation_cost") RequestBody reservation_cost,
                               @Part("reservation_date") RequestBody reservation_date,
                               @Part("transformation_person") RequestBody transformation_person,
                               @Part("transformation_phone") RequestBody transformation_phone,
                               @Part("transformation_amount") RequestBody transformation_amount,
                               @Part MultipartBody.Part image
                               );

    @FormUrlEncoded
    @POST("Api/UpdateReservation/{id_reservation}")
    Call<ResponsModel> UpdateReservation(@Path("id_reservation") String id_reservation,
                                         @Field("new_transformation_date") String new_transformation_date,
                                         @Field("place_id") String place_id
                                         );

    @FormUrlEncoded
    @POST("Api/DeleteReservation/{id_reservation}")
    Call<ResponsModel> CancelReservation(@Path("id_reservation") String id_reservation,
                                         @Field("user_id") String user_id
    );

    @GET("Api/MyReservation/{user_id}")
    Call<List<PlacesModel>> getMyReservations(@Path("user_id") String user_id);


    @GET("Api/SearchCost/{user_id}/{type}")
    Call<List<PlacesModel>> search_by_cost(@Path("user_id") String user_id,
                                           @Path("type") String place_type);

    @FormUrlEncoded
    @POST("Api/SearchDate/{user_id}/{type}")
    Call<List<PlacesModel>> search_by_date(@Path("user_id") String user_id,
                                           @Path("type") String place_type,
                                           @Field("find_date") String find_date);

    @FormUrlEncoded
    @POST("Api/SearchNear/{user_id}/{type}")
    Call<List<PlacesModel>> search_nearby(@Path("user_id") String user_id,
                                           @Path("type") String place_type,
                                           @Field("user_google_lat") double user_google_lat,
                                          @Field("user_google_long") double user_google_long
                                          );

    @FormUrlEncoded
    @POST("AppUser/Profile/{user_id}")
    Call<UserModel> updateProfile(@Path("user_id") String user_id,
                                  @Field("user_phone") String user_phone,
                                  @Field("user_full_name") String user_full_name,
                                  @Field("user_email") String user_email

                                  );

    @FormUrlEncoded
    @POST("AppUser/UpdatePass/{user_id}")
    Call<UserModel> updatePassword(@Path("user_id") String user_id,
                                   @Field("user_old_pass") String user_old_pass,
                                   @Field("user_new_pass") String user_new_pass
                                   );

    @GET("Api/MyAlerts/{user_id}")
    Call<List<Notification_Model>> displayNotification(@Path("user_id") String user_id);

    @GET("Api/UnReadAlerts/{user_id}")
    Call<NotificationCountModel> getNotificationCount(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("Api/UnReadAlerts/{user_id}")
    Call<ResponsModel> readNotification(@Path("user_id") String user_id,@Field("read_all") String read_all);
}
