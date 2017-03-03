package nbsidb.nearbyshops.org;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import org.apache.commons.validator.routines.UrlValidator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.ModelRoles.Admin;
import nbsidb.nearbyshops.org.ModelRoles.Staff;
import nbsidb.nearbyshops.org.RetrofitRestContractRoles.AdminServiceSimple;
import nbsidb.nearbyshops.org.RetrofitRestContractRoles.StaffService;
import nbsidb.nearbyshops.org.StaffHome.StaffHome;
import nbsidb.nearbyshops.org.Utility.UtilityGeneral;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

//    @Bind(R.id.serviceURLEditText) EditText serviceUrlEditText;
    @Bind(R.id.distributorIDEdittext) EditText username;
    @Bind(R.id.loginButton) Button loginButton;
    @Bind(R.id.password) EditText password;
//    @Bind(R.id.signUpButton) Button signUpButton;
    @Bind(R.id.role_admin) TextView roleAdmin;
    @Bind(R.id.role_staff) TextView roleStaff;

    @Inject Gson gson;


//    @Inject AdminServiceSimple adminService;
//    @Inject StaffService staffService;


    public LoginScreen() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        //loginButton = (Button) findViewById(R.id.loginButton);
        //signUpButton = (Button) findViewById(R.id.signUpButton);

        //loginButton.setOnClickListener(this);



        //serviceUrlEditText = (EditText) findViewById(R.id.serviceURLEditText);
        //username = (EditText) findViewById(R.id.distributorIDEdittext);

//        serviceUrlEditText.setText(UtilityGeneral.getServiceURL(getApplicationContext()));
        username.setText(UtilityLogin.getUsername(this));
        password.setText(UtilityLogin.getPassword(this));
        setRoleButtons();

//
//        serviceUrlEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                UtilityGeneral.saveServiceURL(s.toString(),LoginScreen.this);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                UtilityGeneral.saveServiceURL(s.toString(),LoginScreen.this);
//
//            }
//        });


        setupServiceURLEditText();

    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.loginButton:

                startActivity(new Intent(this,Home.class));
                break;
        }

    }


    @OnClick(R.id.loginButton)
    public void login()
    {
        int role = UtilityLogin.getRoleID(this);

        UtilityLogin.saveCredentials(this,username.getText().toString(),password.getText().toString());

        if(role == UtilityLogin.ROLE_ADMIN)
        {
            networkCallLoginAdmin();
        }
        else if(role == UtilityLogin.ROLE_STAFF)
        {

            networkCallLoginStaff();

        }
        else if(role == -1)
        {
            showSnackBar("Please select a Role !");
        }

    }


    void networkCallLoginStaff()
    {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        setProgressBar(true);

//        GsonBuilder gsonBuilder = new GsonBuilder();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        StaffService staffService = retrofit.create(StaffService.class);
        Call<Staff> staffCall = staffService.getLogin(UtilityLogin.baseEncoding(username,password));

        staffCall.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {

                Staff staff = response.body();

                if(staff !=null)
                {

                    UtilityLogin.saveStaff(staff,LoginScreen.this);
                    startActivity(new Intent(LoginScreen.this,StaffHome.class));
//                    showSnackBar("Launching Staff Home !");

                }

                if(response.code()==403 || response.code() ==401)
                {
                    showSnackBar("Unable to login. Username or password is incorrect !");
                }

                setProgressBar(false);
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {

                Log.d("login",String.valueOf(t.toString()));
                showSnackBar("Failed. Please try again !");
                setProgressBar(false);
            }

        });
    }




    void networkCallLoginAdmin()
    {

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();


        setProgressBar(true);

//        GsonBuilder gsonBuilder = new GsonBuilder();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();


        AdminServiceSimple adminServiceSimple = retrofit.create(AdminServiceSimple.class);
        Call<Admin> call = adminServiceSimple.getAdmin(UtilityLogin.baseEncoding(username,password));

        call.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {

                        if(response.code()==200 && response.body() !=null)
                        {
//                            showSnackBar("Successful !");
                            UtilityLogin.saveAdmin(response.body(),LoginScreen.this);
                            startActivity(new Intent(LoginScreen.this,Home.class));
                        }
                        else if(response.code()==401|| response.code()==403)
                        {
                            showSnackBar("Username or password is Incorrect !");
                        }
                        else
                        {
                            showSnackBar("Server Error Code : " + String.valueOf(response.code()));
                        }


                        setProgressBar(false);

            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {

                showSnackBar("Failed : ");

                setProgressBar(false);

            }
        });



//        Observable<Admin> call = adminService.getAdmin(UtilityLogin.baseEncoding(username,password));


//        call.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Admin>() {
//                    @Override
//                    public void onCompleted() {
//
//                        setProgressBar(false);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        if(e instanceof HttpException)
//                        {
//                            HttpException response = (HttpException) e;
//                            Log.d("login",String.valueOf(response.code()));
//
//                            if(response.code()==403 || response.code() ==401)
//                            {
//                                showSnackBar("Unable to login. Username or password is incorrect !");
//                            }
//                            else
//                            {
//                                showSnackBar("LoginScreen failed. Please try again !");
//                            }
//
//                        }
//
//                        setProgressBar(false);
//
//                    }
//
//                    @Override
//                    public void onNext(Admin admin) {
//
//                        if(admin !=null)
//                        {
//                            Gson gson = new Gson();
//                            Log.d("login", gson.toJson(admin));
//
//                            showSnackBar("Successful !");
//                            UtilityLogin.saveAdmin(admin,LoginScreen.this);
//                            startActivity(new Intent(LoginScreen.this,Home.class));
//
//                        }
//
//                        setProgressBar(false);
//
//                    }
//                });

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    void showSnackBar(String message)
    {
//        Snackbar.make(loginButton,message, Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }






    @OnClick(R.id.role_admin)
    void selectRoleAdmin()
    {
        clearSelection();
        roleAdmin.setTextColor(ContextCompat.getColor(this,R.color.white));
        roleAdmin.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonColorDark));

        UtilityLogin.setRoleID(this,UtilityLogin.ROLE_ADMIN);
    }


    @OnClick(R.id.role_staff)
    void selectRoleStaff()
    {
        clearSelection();
        roleStaff.setTextColor(ContextCompat.getColor(this,R.color.white));
        roleStaff.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonColorDark));

        UtilityLogin.setRoleID(this,UtilityLogin.ROLE_STAFF);
    }



    void clearSelection()
    {
        roleAdmin.setTextColor(ContextCompat.getColor(this,R.color.blueGrey800));
        roleStaff.setTextColor(ContextCompat.getColor(this,R.color.blueGrey800));

        roleAdmin.setBackgroundColor(ContextCompat.getColor(this,R.color.light_grey));
        roleStaff.setBackgroundColor(ContextCompat.getColor(this,R.color.light_grey));
    }




    void setRoleButtons() {

        if (UtilityLogin.getRoleID(this) == UtilityLogin.ROLE_ADMIN)
        {
            selectRoleAdmin();
        }
        else if(UtilityLogin.getRoleID(this)==UtilityLogin.ROLE_STAFF)
        {
            selectRoleStaff();
        }
    }





    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    void setProgressBar(boolean visible)
    {
        progressBar.setIndeterminate(true);

        if(visible)
        {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }

    }




    // Setup Status Light

    @Bind(R.id.text_input_service_url) TextInputLayout textInputServiceURL;
    @Bind(R.id.serviceURL) EditText serviceURL;

    UrlValidator urlValidator;

    void setupServiceURLEditText()
    {
        String[] schemes = {"http", "https"};

        urlValidator = new UrlValidator(schemes);

        serviceURL.setText(UtilityGeneral.getServiceURL(this));

        serviceURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (urlValidator.isValid(s.toString())) {
                    UtilityGeneral.saveServiceURL(s.toString(),LoginScreen.this);
                    textInputServiceURL.setError(null);
                    textInputServiceURL.setErrorEnabled(false);
//                    updateStatusLight();
                }
                else
                {
//                    serviceURL.setError("URL Invalid");
                    textInputServiceURL.setErrorEnabled(true);
                    textInputServiceURL.setError("Invalid URL");

//                    UtilityServiceConfig.saveServiceLightStatus(LoginScreen.this,STATUS_LIGHT_RED);
//                    setStatusLight();
                }

            }
        });


    }






}
