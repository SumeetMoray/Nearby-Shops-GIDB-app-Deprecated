package nbsidb.nearbyshops.org.RetrofitRESTContract;
import java.util.List;

import nbsidb.nearbyshops.org.Model.Image;
import nbsidb.nearbyshops.org.ModelRoles.Staff;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by sumeet on 12/3/16.
 */
public interface StaffService {

    @POST("/api/v1/Staff")
    Call<Staff> postStaff(@Header("Authorization") String headers,
                          @Body Staff staff);


    @PUT("/api/v1/Staff/{StaffID}")
    Call<ResponseBody> putStaff(@Header("Authorization") String headers,
                                @Path("StaffID") int id,
                                @Body Staff staff);


    @PUT("/api/v1/Staff/UpdateBySelf/{StaffID}")
    Call<ResponseBody> updateBySelfStaff(@Header("Authorization") String headers,
                                         @Path("StaffID") int id,
                                         @Body Staff staff);


    @DELETE("/api/v1/Staff/{StaffID}")
    Call<ResponseBody> deleteStaff(@Header("Authorization") String headers,
                                   @Path("StaffID") int staffID);


    @GET("/api/v1/Staff/Login")
    Call<Staff> getLogin(@Header("Authorization") String headers);



    @GET("/api/v1/Staff")
    Call<List<Staff>> getStaffList(@Header("Authorization") String headers,
                                   @Query("IsEnabled") Boolean isEnabled);


    @GET("/api/v1/Staff/CheckUsernameExists/{username}")
    Call<ResponseBody> checkUsernameExist(@Path("username") String username);






    // Image Calls

    @POST("/api/v1/Staff/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName


    @DELETE("/api/v1/Staff/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);







    //-------------------------------------------------------

//    @GET("/api/v1/Staff/LoginScreen")
//    Call<Staff> loginStaff(@Header("Authorization") String headers);

//    @POST("/api/v1/Staff")
//    Call<Staff> postStaff(@Body Staff staff);

//    @PUT("/api/v1/Staff/{id}")
//    Call<ResponseBody> putStaff(@Body Staff staff, @Path("id") int id);

//    @DELETE("/api/v1/Staff/{id}")
//    Call<ResponseBody> deleteStaff(@Path("id") int id);

}
