package dsh.com.ejournal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import dsh.com.ejournal.stuff.Session;

public class LoginActivity extends AppCompatActivity {
    EditText    loginField;
    EditText    passwordField;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginField = findViewById(R.id.loginField);
        passwordField = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.prBar);
    }

    class AsyncLogin extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            Session session = new Session();
            boolean success = session.login(strings[0], strings[1]);

            if(!success){
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, R.string.wrongLoginOrPassword, Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, R.string.loginSuccess, Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }
    }

    public void login(View v) {
        if(loginField.getText().toString().equals("")){Toast.makeText(this, R.string.loginFieldEmpty, Toast.LENGTH_SHORT).show();return;}
        if(passwordField.getText().toString().equals("")){Toast.makeText(this, R.string.passwordFieldEmpty, Toast.LENGTH_SHORT).show();return;}

        boolean connection = Session.checkNetConnection(this);
        if (!connection) {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncLogin loginTask = new AsyncLogin();
        loginTask.execute(loginField.getText().toString(), passwordField.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
    }
}
