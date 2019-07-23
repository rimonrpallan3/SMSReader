package com.glen.smsreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.glen.smsreader.adapter.SmsAdapter;
import com.glen.smsreader.appConfig.Constances;
import com.glen.smsreader.model.Sms;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.glen.smsreader.appConfig.Constances.RC_READ_SMS;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    Button btnSubmit;
    EditText etPhoneNo;
    LinearLayout llEmptyView;

    List<Sms> smsArrayList = new ArrayList<>();
    List<Sms> smsDeletedArrayList = new ArrayList<>();
    RecyclerView rvSmsList;
    SmsAdapter smsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubmit = findViewById(R.id.btnSubmit);
        etPhoneNo = findViewById(R.id.etPhoneNo);
        rvSmsList = findViewById(R.id.rvSmsList);
        rvSmsList = findViewById(R.id.rvSmsList);
        llEmptyView = findViewById(R.id.llEmptyView);
        getSMSTask();
    }

    public List<Sms> getFilteredList(List<Sms> smsArrayList){

        for(int i=0;i<smsArrayList.size();i++){
            Sms sms = smsArrayList.get(i);
            if(Constances.isValidPhoneNumber(sms.getAddress())){
                System.out.println("Yes this is a valid no ");
            }else {
                if(sms.getMsg().contains("OTP")){
                    //hideItem(position);
                }else {
                    smsDeletedArrayList.add(sms);
                }

            }
        }
        return smsDeletedArrayList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this, "Please enable Sms Read Permission manually ",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }



    public void btnRetry(View v){
        getSMSTask();
    }

    private boolean hasSMSReadPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_SMS);
    }

    @AfterPermissionGranted(RC_READ_SMS)
    public void getSMSTask() {
        if (hasSMSReadPermission()) {
            // Have permission, do the thing!
            smsArrayList = getAllSms();
            smsDeletedArrayList = getFilteredList(smsArrayList);
            smsAdapter = new SmsAdapter(smsDeletedArrayList);
            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(getParent(), RecyclerView.VERTICAL, false);
            rvSmsList.setLayoutManager(linearLayoutManager);
            rvSmsList.setAdapter(smsAdapter);
            if (smsAdapter.getItemCount() == 0){
                llEmptyView.setVisibility(View.VISIBLE);
                rvSmsList.setVisibility(View.GONE);
            }
            Toast.makeText(this, "TODO: Has SMS Read Permission", Toast.LENGTH_LONG).show();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_sms_prom),
                    RC_READ_SMS,
                    Manifest.permission.READ_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public List<Sms> getAllSms() {
        llEmptyView.setVisibility(View.GONE);
        rvSmsList.setVisibility(View.VISIBLE);
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        startManagingCursor(c);
        int totalSMS = c.getCount();

        System.out.println("totalSMS : "+totalSMS);
        if(c!=null) {
            if (c.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {

                    objSms = new Sms();
                    objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(c.getString(c
                            .getColumnIndexOrThrow("address")));
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSms.setReadState(c.getString(c.getColumnIndex("read")));
                    objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objSms.setFolderName("inbox");
                    } else {
                        objSms.setFolderName("sent");
                    }

                    lstSms.add(objSms);
                    c.moveToNext();
                }
            } else {
                llEmptyView.setVisibility(View.VISIBLE);
                rvSmsList.setVisibility(View.GONE);
                Toast.makeText(
                        this, "Their is no Message to read",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }else {

        }
        c.close();
        c = null;

        return lstSms;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        llEmptyView.setVisibility(View.VISIBLE);
        rvSmsList.setVisibility(View.GONE);
    }
}
