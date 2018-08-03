package com.techease.cpasolutions.Interfaces;

import com.techease.cpasolutions.Retro.FileUploadResponseModel;

import java.io.File;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Adamnoor on 8/2/2018.
 */

public interface ApiInterface {

    @Multipart
    @POST("uploadFile")
  Call<FileUploadResponseModel> uploadFile(
            @Part("userid") RequestBody userid,
            @Part("folderid") RequestBody folderid,
            @Part("sendto") RequestBody sendTo,
            @Part MultipartBody.Part photo,
            @Part("file") RequestBody file,
            @Part("note") RequestBody note

    );
}
