package nbsidb.nearbyshops.org.ItemSpecValue.EditItemSpecValue;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.DaggerComponentBuilder;
import nbsidb.nearbyshops.org.Model.Image;
import nbsidb.nearbyshops.org.ModelItemSpecification.ItemSpecificationName;
import nbsidb.nearbyshops.org.ModelItemSpecification.ItemSpecificationValue;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemSpecNameService;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemSpecValueService;
import nbsidb.nearbyshops.org.Utility.UtilityGeneral;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class EditItemSpecValueFragment extends Fragment{

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


//    public static final String ITEM_ID_INTENT_KEY = "item_id_intent_key";

    @Inject
    ItemSpecValueService itemSpecNameService;


    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    // bind views
    @Bind(R.id.uploadImage) ImageView resultView;


    @Bind(R.id.itemSpecNameID) EditText itemSpecNameID;
    @Bind(R.id.title_spec_name) EditText title;
    @Bind(R.id.description) EditText description;


    @Bind(R.id.saveButton) Button buttonUpdateItem;


    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    public static final String ITEM_SPEC_NAME_INTENT_KEY = "ITEM_SPEC_NAME_INTENT_KEY";


    int current_mode = MODE_ADD;

    boolean isDestroyed = false;


    ItemSpecificationValue itemSpecName = new ItemSpecificationValue();


    public EditItemSpecValueFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.content_edit_item_spec_value_fragment, container, false);

        ButterKnife.bind(this,rootView);

        if(savedInstanceState==null)
        {

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

            if(current_mode == MODE_UPDATE)
            {
                itemSpecName = UtilityItemSpecValue.getItemSpecName(getContext());

                if(itemSpecName !=null) {
                    bindDataToViews();
                }

            }
            else if (current_mode == MODE_ADD)
            {

//                if(getActivity().getActionBar()!=null)
//                {
//                    getActivity().getActionBar().setTitle("Add Item Image");
//                }


                if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
                {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Item Specification Value");
                }

//                item.setItemCategoryID(itemCategory.getItemCategoryID());
//                System.out.println("Item Category ID : " + item.getItemCategoryID());
            }



            showLogMessage("Inside OnCreateView - Saved Instance State !");
        }




        updateIDFieldVisibility();


        if(itemSpecName !=null) {
            loadImage(itemSpecName.getImageFilename());
        }


        showLogMessage("Inside On Create View !");

        return rootView;
    }



    void updateIDFieldVisibility()
    {

        if(current_mode==MODE_ADD)
        {
            buttonUpdateItem.setText("Add Item Image");
            itemSpecNameID.setVisibility(View.GONE);
        }
        else if(current_mode== MODE_UPDATE)
        {
            itemSpecNameID.setVisibility(View.VISIBLE);
            buttonUpdateItem.setText("Save");


            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Item Specification Value");
            }
        }
    }




    public static final String TAG_LOG = "TAG_LOG";

    void showLogMessage(String message)
    {
        Log.i(TAG_LOG,message);
        System.out.println(message);
    }




    void loadImage(String filename) {

//        String iamgepath = UtilityGeneral.getServiceURL(getContext()) + "/api/v1/ItemImage/five_hundred_" + imagePath + ".jpg";


        String imagePath = UtilityGeneral.getServiceURL(getActivity()) + "/api/v1/ItemSpecificationValue/Image/five_hundred_"
                + filename + ".jpg";

        System.out.println(imagePath);

        Picasso.with(getContext())
                .load(imagePath)
                .into(resultView);
    }




    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(!validateData())
        {
//            showToastMessage("Please correct form data before save !");
            return;
        }

        if(current_mode == MODE_ADD)
        {
            itemSpecName = new ItemSpecificationValue();
            addAccount();
        }
        else if(current_mode == MODE_UPDATE)
        {
            update();
        }
    }


    boolean validateData()
    {
        boolean isValid = true;

        if(title.getText().toString().length()==0)
        {
            title.setError("Title cannot be empty !");
            title.requestFocus();
            isValid= false;
        }



        return isValid;
    }




    void addAccount()
    {
        if(isImageChanged)
        {
            if(!isImageRemoved)
            {
                // upload image with add
                uploadPickedImage(false);
            }


            // reset the flags
            isImageChanged = false;
            isImageRemoved = false;

        }
        else
        {
            // post request
            retrofitPOSTRequest();
        }

    }


    void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(itemSpecName.getImageFilename());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {

                itemSpecName.setImageFilename(null);
                retrofitPUTRequest();

            }else
            {

                uploadPickedImage(true);
            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }else {

            retrofitPUTRequest();
        }
    }



    void bindDataToViews()
    {
        if(itemSpecName !=null) {


            itemSpecNameID.setText(String.valueOf(itemSpecName.getId()));
            title.setText(itemSpecName.getTitle());
            description.setText(itemSpecName.getDescription());
        }
    }





    void getDataFromViews()
    {
        if(itemSpecName ==null)
        {
            if(current_mode == MODE_ADD)
            {
//                itemImage = new ItemImage();
            }
            else
            {
                return;
            }
        }


        itemSpecName.setTitle(title.getText().toString());
        itemSpecName.setDescription(description.getText().toString());
    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }

    public void retrofitPUTRequest()
    {

        getDataFromViews();


        Call<ResponseBody> call = itemSpecNameService.updateItemSpec(
                UtilityLogin.getAuthorizationHeaders(getContext()),
                itemSpecName,itemSpecName.getId()
        );



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                    UtilityItemSpecValue.saveItemSpecValue(itemSpecName,getContext());
                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Update Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }



                showToastMessage("Update failed !");
            }
        });

    }





    void retrofitPOSTRequest()
    {
        getDataFromViews();
//        itemSpecName.setId(getActivity().getIntent().getIntExtra(ITEM_ID_INTENT_KEY,0));

        itemSpecName.setItemSpecNameID(getActivity().getIntent().getIntExtra(ITEM_SPEC_NAME_INTENT_KEY,0));


        Call<ItemSpecificationValue> call = itemSpecNameService.saveItemSpecValue(UtilityLogin.getAuthorizationHeaders(getContext()), itemSpecName);

        call.enqueue(new Callback<ItemSpecificationValue>() {
            @Override
            public void onResponse(Call<ItemSpecificationValue> call, Response<ItemSpecificationValue> response) {


                if(isDestroyed)
                {
                    return;
                }



                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    current_mode = MODE_UPDATE;
                    updateIDFieldVisibility();
                    itemSpecName = response.body();
                    bindDataToViews();

                    UtilityItemSpecValue.saveItemSpecValue(itemSpecName,getContext());

                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Add failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ItemSpecificationValue> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }



                showToastMessage("Add failed !");
            }
        });

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }





    /*
        Utility Methods
     */




    void showToastMessage(String message)
    {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }




    @Bind(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        isImageChanged = true;
        isImageRemoved = true;
    }



    public static void clearCache(Context context)
    {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }



    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

//        ImageCropUtility.showFileChooser(()getActivity());



        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }



        clearCache(getContext());

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);



        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {

                startCropActivity(result.getData(),getContext());
            }

        }


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(null);
            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }
    }



    // upload image after being picked up
    void startCropActivity(Uri sourceUri, Context context) {



        final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";

        Uri destinationUri = Uri.fromFile(new File(getContext().getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));

        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);

//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        options.setCompressionQuality(100);

        options.setToolbarColor(ContextCompat.getColor(getContext(),R.color.blueGrey800));
        options.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);


        // this function takes the file from the source URI and saves in into the destination URI location.
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(context,this);

        //.withMaxResultSize(400,300)
        //.withMaxResultSize(500, 400)
        //.withAspectRatio(16, 9)
    }





    /*

    // Code for Uploading Image

     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showToastMessage("Permission Granted !");
                    pickShopImage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {


                    showToastMessage("Permission Denied for Read External Storage . ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }





    public void uploadPickedImage(final boolean isModeEdit)
    {

        Log.d("applog", "onClickUploadImage");


        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }


        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");


        // Marker

        RequestBody requestBodyBinary = null;

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            byte[] buf;
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;

            requestBodyBinary = RequestBody.create(MediaType.parse("application/octet-stream"), buf);

        } catch (Exception e) {
            e.printStackTrace();
        }



        Call<Image> imageCall = itemSpecNameService.uploadImage(UtilityLogin.getAuthorizationHeaders(getContext()),
                requestBodyBinary);


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {


                if(isDestroyed)
                {
                    return;
                }



                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    itemSpecName.setImageFilename(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    itemSpecName.setImageFilename(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    itemSpecName.setImageFilename(null);

                }

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }


            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }



                showToastMessage("Image Upload failed !");
                itemSpecName.setImageFilename(null);


                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }
            }
        });

    }



    void deleteImage(String filename)
    {
        Call<ResponseBody> call = itemSpecNameService.deleteImage(
                UtilityLogin.getAuthorizationHeaders(getContext()),
                filename);



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }




                if(response.code()==200)
                    {
                        showToastMessage("Image Removed !");
                    }
                    else
                    {
//                        showToastMessage("Image Delete failed");
                    }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }
//                showToastMessage("Image Delete failed");
            }
        });
    }


}
