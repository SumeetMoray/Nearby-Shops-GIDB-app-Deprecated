package nbsidb.nearbyshops.org.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nbsidb.nearbyshops.org.MyApplication;
import nbsidb.nearbyshops.org.RetrofitRESTContract.AdminService;
import nbsidb.nearbyshops.org.RetrofitRESTContract.AdminServiceSimple;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemCategoryService;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemImageService;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemService;
import nbsidb.nearbyshops.org.RetrofitRESTContract.StaffService;
import nbsidb.nearbyshops.org.RetrofitRESTContractGIDB.ItemCategoryServiceGIDB;
import nbsidb.nearbyshops.org.RetrofitRESTContractGIDB.ItemServiceGIDB;
import nbsidb.nearbyshops.org.Utility.UtilityGeneral;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 14/5/16.
 */

        /*
        retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        */

@Module
public class NetModule {

    String serviceURL;

    // Constructor needs one parameter to instantiate.
    public NetModule() {

    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    /*
    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    */

    @Provides
    @Singleton
    Gson provideGson() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        // Cache cache

        // cache is commented out ... you can add cache by putting it back in the builder options
        //.cache(cache)

        return new OkHttpClient()
                .newBuilder()
                .build();
    }


    //    @Singleton

    @Provides @Named("normal")
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        //        .client(okHttpClient)

        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));


        return retrofit;
    }




    @Provides @Named("gidb")
    Retrofit provideRetrofitGIDB(Gson gson, OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL_GIDB(MyApplication.getAppContext()))
                .build();

        //        .client(okHttpClient)

        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL_GIDB(MyApplication.getAppContext()));


        return retrofit;
    }



    @Provides @Named("reactive")
    Retrofit provideRetrofitReactive(Gson gson, OkHttpClient okHttpClient) {


//        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();

//        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        //        .client(okHttpClient)

        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));


        return retrofit;
    }


    @Provides
    ItemCategoryService provideItemCategoryService(@Named("normal")Retrofit retrofit)
    {

        return retrofit.create(ItemCategoryService.class);
    }


    @Provides
    ItemService provideItemService(@Named("normal")Retrofit retrofit)
    {

        return retrofit.create(ItemService.class);
    }


    @Provides
    ItemImageService provideItemImageService(@Named("normal")Retrofit retrofit)
    {

        return retrofit.create(ItemImageService.class);
    }



    @Provides
    AdminService provideAdminService(@Named("reactive")Retrofit retrofit)
    {
        return retrofit.create(AdminService.class);
    }


    @Provides
    AdminServiceSimple provideAdminServiceSimple(@Named("normal")Retrofit retrofit)
    {
        return retrofit.create(AdminServiceSimple.class);
    }


    @Provides
    StaffService provideStaffService(@Named("normal")Retrofit retrofit)
    {
        return retrofit.create(StaffService.class);
    }



    @Provides
    ItemCategoryServiceGIDB provideItemCategoryServiceGIDB(@Named("gidb")Retrofit retrofit)
    {
        return retrofit.create(ItemCategoryServiceGIDB.class);
    }

    @Provides
    ItemServiceGIDB provideItemServiceGIDB(@Named("gidb")Retrofit retrofit)
    {
        return retrofit.create(ItemServiceGIDB.class);
    }




}
