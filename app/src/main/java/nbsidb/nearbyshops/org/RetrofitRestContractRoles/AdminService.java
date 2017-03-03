package nbsidb.nearbyshops.org.RetrofitRestContractRoles;

import nbsidb.nearbyshops.org.Model.Image;
import nbsidb.nearbyshops.org.ModelRoles.Admin;
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
import rx.Observable;

/**
 * Created by sumeet on 12/3/16.
 */
public interface AdminService {

    @GET("/api/v1/Admin/Login")
    Observable<Admin> getAdmin(@Header("Authorization") String headers);


    @PUT("/api/v1/Admin")
    Call<ResponseBody> putAdmin(@Header("Authorization") String headers,
                               @Body Admin admin);



    @GET("/api/v1/Staff/CheckUsernameExists/{username}")
    Call<ResponseBody> checkUsernameExist(@Path("username") String username);



    // Image Calls

    @POST("/api/v1/Admin/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName


    @DELETE("/api/v1/Admin/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);


}
