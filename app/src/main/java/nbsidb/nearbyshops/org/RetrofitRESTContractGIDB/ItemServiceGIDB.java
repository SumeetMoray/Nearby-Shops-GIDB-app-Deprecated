package nbsidb.nearbyshops.org.RetrofitRESTContractGIDB;


import java.util.List;

import nbsidb.nearbyshops.org.Model.Image;
import nbsidb.nearbyshops.org.Model.Item;
import nbsidb.nearbyshops.org.ModelEndpoint.ItemEndPoint;
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
 * Created by sumeet on 3/4/16.
 */
public interface ItemServiceGIDB
{

    @POST("/api/v1/Item")
    Call<Item> insertItem(@Header("Authorization") String headers,
                          @Body Item item);



    @PUT("/api/v1/Item/ChangeParent/{id}")
    Call<ResponseBody> changeParent(@Header("Authorization") String headers,
                                    @Body Item item,
                                    @Path("id") int id);


    @PUT ("/api/v1/Item/ChangeParent")
    Call<ResponseBody> changeParentBulk(@Header("Authorization") String headers,
                                        @Body List<Item> itemsList);



    @PUT("/api/v1/Item/{id}")
    Call<ResponseBody> updateItem(@Header("Authorization") String headers,
                                  @Body Item item,
                                  @Path("id") int id);

    @PUT("/api/v1/Item/")
    Call<ResponseBody> updateItemBulk(@Header("Authorization") String headers,
                                      @Body List<Item> itemList);

    @DELETE("/api/v1/Item/{id}")
    Call<ResponseBody> deleteItem(@Header("Authorization") String headers,
                                  @Path("id") int id);


    @GET("/api/v1/Item")
    Call<ItemEndPoint> getItems(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("ShopID") Integer shopID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @GET("/api/v1/Item/OuterJoin")
    Call<ItemEndPoint> getItemsOuterJoin(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );



    @GET("/api/v1/Item/OuterJoin")
    Call<ItemEndPoint> getItemsOuterJoin(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("IsDetached") Boolean parentIsNull,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );



    @GET("/api/v1/Item/{id}")
    Call<Item> getItem(@Path("id") int ItemID);



    // Image Calls

    @POST("/api/v1/Item/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);


    @DELETE("/api/v1/Item/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);




}
