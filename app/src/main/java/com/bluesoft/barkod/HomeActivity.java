package com.bluesoft.barkod;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bluesoft.barkod.network.ApiClient;
import com.bluesoft.barkod.network.RetrofitInterface;
import com.bluesoft.barkod.request.CommonActionRequest;
import com.bluesoft.barkod.request.MobilActionRequest;
import com.bluesoft.barkod.response.ActionResponse;
import com.bluesoft.barkod.response.MobilActionResponse;
import com.bluesoft.barkod.response.SanalBarkodResponse;
import com.bluesoft.barkod.ui.login.LoginActivity;
import com.bluesoft.barkod.util.LocalStorage;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity  {

    RetrofitInterface apiService;
    ApiClient apiClient;
    private TextView formatTxt;
    private EditText  contentTxt,devreAdi,duzey,spoll;
    String token;
    private Button sendRestButton;
    private Button btnScanNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        token="Bearer" + " " + LocalStorage.getUserToken(getApplicationContext());
        apiClient = new ApiClient(LocalStorage.getServiceUrl(getApplicationContext()));
        apiService = apiClient.getApiService();
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = findViewById(R.id.scan_content);
        sendRestButton = findViewById(R.id.btn_send_action);
        btnScanNow = findViewById(R.id.btn_scan_now);
        devreAdi = findViewById(R.id.scan_devre_adi);
        duzey = findViewById(R.id.scan_duzey);
        spoll = findViewById(R.id.scan_spoll);


        //contentTxt.setText("101462");

        contentTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if( actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){

                    getSanalBarkodDetay(String.valueOf(contentTxt.getText()));
                    return true;
                }
                return false;
            }
        });




       // Toast.makeText(getApplicationContext(),LocalStorage.getUserId(getApplicationContext()), Toast.LENGTH_SHORT).show();

        sendRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validateInput(contentTxt.getText().toString())) {
                        sendRestButton.setEnabled(false);
                        sendAction();
                        sendRestButton.setEnabled(true);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    sendRestButton.setEnabled(true);
                }
            }
        });

        btnScanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                     scanNow();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean validateInput(String barkod) {
        if(barkod==null || barkod.isEmpty()){
            Toast.makeText(getApplicationContext(),"Barkod Okutunuz", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void sendAction(){

        if(String.valueOf(contentTxt.getText())!=null && !String.valueOf(contentTxt.getText()).isEmpty()){
            getActionBarkod(String.valueOf(contentTxt.getText()));
        }
        sendRestButton.setEnabled(true);
    }

     public void sendActionUser(ActionResponse response){
        if(response.getActionList()!=null && response.getActionList().size()>0 ){
            // setup the alert builder
            final int[] selectedHavuz = {0};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Gönder");
            builder.setSingleChoiceItems(response.getActionList().toArray(new String[response.getActionList().size()]), 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedHavuz[0] =which;
                }
            });
            // add OK and Cancel buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendRestCall(response.getActionList().get(selectedHavuz[0]));
                }
            });
            builder.setNegativeButton("Cancel", null);
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }else {
            Toast.makeText(getApplicationContext(),response.getMessage(), Toast.LENGTH_SHORT).show();
        }
}


    public void logout(){
        new AlertDialog.Builder(this)
                .setTitle("Çıkış")
                .setMessage("Uygulamadan çıkış yapacakmısınız ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        LocalStorage.logout(getApplicationContext());
                        Intent intenti = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intenti);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void scanNow(){
        // add fragment
        devreAdi.setText("");
        duzey.setText("");
        spoll.setText("");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureCamera.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        integrator.setOrientationLocked(false);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            if(scanningResult.getContents()!=null){


                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Okunan Barkod");
                builder.setMessage(scanningResult.getContents());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Tekrar Tara", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                scanNow();
                            }})
                        .setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getSanalBarkodDetay(scanningResult.getContents());
                                contentTxt.setText(""+scanningResult.getContents());
                            }});

                AlertDialog dialog=builder.create();
                dialog.show();
            }else{
                Toast.makeText(getApplicationContext(),"Lütfen Barkodu Okutunuz.", Toast.LENGTH_SHORT).show();
            }

        }else{
           super.onActivityResult(requestCode, resultCode, intent);
        }
    }




    public void sendRestCall(String havuz){

        if( String.valueOf(contentTxt.getText())!=null && !String.valueOf(contentTxt.getText()).isEmpty()){

        MobilActionRequest request=new MobilActionRequest();
        request.setAction(havuz);
        request.setSanalBarkod(String.valueOf(contentTxt.getText()));
        request.setPersonelId(LocalStorage.getUserId(getApplicationContext()));

            Call<MobilActionResponse> callAction = apiService.toUserIslemAction(request,token);
            callAction.enqueue(new Callback<MobilActionResponse>() {
            @Override
            public void onResponse(Call<MobilActionResponse> callAction, Response<MobilActionResponse> callActionResponse) {
                if(callActionResponse.isSuccessful() && callActionResponse.code()==200) {
                    cleanScanData();
                    Toast.makeText(getApplicationContext(),"İşlem başarı ile sona erdi.", Toast.LENGTH_SHORT).show();
                }else if(callActionResponse.code()==401) {
                    Toast.makeText(getApplicationContext(), "Kullanıcı Adı ve Şifresi yanlış", Toast.LENGTH_SHORT).show();
                } else if(callActionResponse.code()==404){
                    Toast.makeText(getApplicationContext(),"Server ile bağlantı kurulamadı", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Bağlantı Sorunu aşılamadı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MobilActionResponse> callAction, Throwable t) {
                Toast.makeText(getApplicationContext(), "Başarısız İşlem "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }else{
            Toast.makeText(getApplicationContext(),"Lütfen Barkodu Okutunuz.", Toast.LENGTH_SHORT).show();
        }

    }



    private void cleanScanData(){
        contentTxt.setText(null);
    }

    public void getSanalBarkodDetay(String sanalBarkodNo){

        Call<SanalBarkodResponse> callgetSanalBarkodDetay = apiService.getSanalBarkodDetay(new Long(sanalBarkodNo),token);
        callgetSanalBarkodDetay.enqueue(new Callback<SanalBarkodResponse>() {
            @Override
            public void onResponse(Call<SanalBarkodResponse> callCommonAction, Response<SanalBarkodResponse> callActionResponse) {
                if(callActionResponse.isSuccessful() && callActionResponse.code()==200) {
                    if(callActionResponse.body()!=null &&  callActionResponse.body().getSpoolNo()!=null){
                         devreAdi.setText(callActionResponse.body().getDevreAdi());
                         duzey.setText(callActionResponse.body().getDuzey());
                         spoll.setText(callActionResponse.body().getSpoolNo());
                    }else{
                        devreAdi.setText("Bulunamadı");
                        duzey.setText("Bulunamadı");
                        spoll.setText("Bulunamadı");
                    }
                }
            }
            @Override
            public void onFailure(Call<SanalBarkodResponse> callCommonAction, Throwable t) {
                devreAdi.setText("Bulunamadı");
                duzey.setText("Bulunamadı");
                spoll.setText("Bulunamadı");
            }
        });


    }

    public void getActionBarkod(String sanalBarkodNo){
        if(String.valueOf(sanalBarkodNo)!=null && !String.valueOf(sanalBarkodNo).isEmpty()){

            CommonActionRequest request=new CommonActionRequest();
            request.setSanalBarkodNo(sanalBarkodNo);
            request.setPersonelId(LocalStorage.getUserId(getApplicationContext()));

            Call<ActionResponse> callCommonAction = apiService.getCommonAction(request,token);
            callCommonAction.enqueue(new Callback<ActionResponse>() {
                @Override
                public void onResponse(Call<ActionResponse> callCommonAction, Response<ActionResponse> callCommonActionResponse) {
                    if(callCommonActionResponse.isSuccessful() && callCommonActionResponse.code()==200) {
                        if(callCommonActionResponse.body().getCode()==1){
                            sendActionUser(callCommonActionResponse.body());
                        }else{
                            Toast.makeText(getApplicationContext(), callCommonActionResponse.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Hatalı Barkod", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ActionResponse> callCommonAction, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Hatalı Barkod", Toast.LENGTH_SHORT).show();
                }
            });


        }else{
            Toast.makeText(getApplicationContext(),"Lütfen Barkodu Okutunuz.", Toast.LENGTH_SHORT).show();
        }

    }

}