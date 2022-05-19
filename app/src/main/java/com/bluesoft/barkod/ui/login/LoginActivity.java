package com.bluesoft.barkod.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;

import com.bluesoft.barkod.HomeActivity;
import com.bluesoft.barkod.R;
import com.bluesoft.barkod.SettingsActivity;
import com.bluesoft.barkod.network.ApiClient;
import com.bluesoft.barkod.network.RetrofitInterface;
import com.bluesoft.barkod.request.UserRequest;
import com.bluesoft.barkod.response.UserResponse;
import com.bluesoft.barkod.util.LocalStorage;

import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    RetrofitInterface apiService;
    ApiClient apiClient;
    private Button loginButton;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        loadLocalStorage(usernameEditText, passwordEditText);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validateInput(usernameEditText.getText().toString(), passwordEditText.getText().toString())) {
                        loginButton.setEnabled(false);
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        loginProcess(usernameEditText.getText().toString(), passwordEditText.getText().toString(), v);
                    }
                } catch (Exception e) {
                    loginButton.setEnabled(true);
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private boolean validateInput(String name, String password) {
        if(name==null || name.isEmpty()){
            Toast.makeText(getApplicationContext(),"Kullanıcı adını giriniz", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password==null  || password.isEmpty()){
            Toast.makeText(getApplicationContext(),"Lütfen Şifrenizi giriniz", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadLocalStorage(EditText usernameEditText, EditText passwordEditText) {
        UserRequest userRquest = LocalStorage.getUserRequest(getApplicationContext());
        if(userRquest!=null){
        usernameEditText.setText(userRquest.getUsername());
        passwordEditText.setText(userRquest.getPassword());
    }}

    private void loginProcess(String email, String password,View v) {
        apiClient = new ApiClient(LocalStorage.getServiceUrl(getApplicationContext()));
        apiService = apiClient.getApiService();
            UserRequest user=new UserRequest();
            user.setPassword(password);
            user.setUsername(email);

            Call<UserResponse> call = apiService.login(user);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if(response.isSuccessful() && response.code()==200) {
                        Toast.makeText(getApplicationContext(),"Hoş Geldiniz", Toast.LENGTH_SHORT).show();
                        LocalStorage.saveLoginUserData(getApplicationContext(),response.body(),user);
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(),"Başarısız İşlem", Toast.LENGTH_SHORT).show();
                        loginButton.setEnabled(true);
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    LocalStorage.logout(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Başarısız İşlem "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            });

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        
        inflater.inflate(R.menu.settings_menu, menu);
        MenuItem item = menu.findItem(R.id.action_logout);
        item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}