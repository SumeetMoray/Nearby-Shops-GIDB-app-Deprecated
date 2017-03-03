package nbsidb.nearbyshops.org.StaffHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.DaggerComponentBuilder;
import nbsidb.nearbyshops.org.ModelRoles.Staff;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.RetrofitRestContractRoles.StaffService;
import nbsidb.nearbyshops.org.StaffHome.EditStaffSelf.EditStaffSelf;
import nbsidb.nearbyshops.org.StaffHome.EditStaffSelf.EditStaffSelfFragment;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffHome extends AppCompatActivity {

    @Bind(R.id.notice) TextView notice;
    @Inject
    StaffService staffService;

    public StaffHome() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkAccountActivation();
    }


    void checkAccountActivation()
    {
        // if account is deactivated notify User

        Staff staff = UtilityLogin.getStaff(this);

        if(staff!=null && !staff.getEnabled())
        {
            notice.setVisibility(View.VISIBLE);
        }
        else
        {
            notice.setVisibility(View.GONE);
        }

    }




    @OnClick(R.id.edit_profile)
    void editProfileClick()
    {
        Intent intent = new Intent(this, EditStaffSelf.class);
        intent.putExtra(EditStaffSelfFragment.EDIT_MODE_INTENT_KEY, EditStaffSelfFragment.MODE_UPDATE);
        startActivity(intent);
    }



    @OnClick(R.id.dashboard)
    void dashboardClick()
    {
        Call<Staff> call = staffService.getLogin(UtilityLogin.getAuthorizationHeaders(this));

        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {

                if(response.code()==200)
                {
                    // permitted
                    if(response.body().getEnabled())
                    {
                        UtilityLogin.saveStaff(response.body(),StaffHome.this);
                        startActivity(new Intent(StaffHome.this,StaffDashboard.class));
                    }
                    else
                    {
                        showToastMessage("Not Permitted. Your Account is Disabled !");
                    }
                }
                else if(response.code() == 401 || response.code() ==403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Server Error Code : " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {

                showToastMessage("Network Failed. Check your Internet Connection !");
            }
        });

    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
