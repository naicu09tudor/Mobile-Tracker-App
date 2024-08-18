package edu.utcluj.track;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private EditText editTextIPAddress, editTextPortNumber;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextIPAddress = findViewById(R.id.server_ip);
        editTextPortNumber = findViewById(R.id.server_port);
        buttonSave = findViewById(R.id.save_settings_button);

        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ip = editTextIPAddress.getText().toString();
                String portString = editTextPortNumber.getText().toString();

                if (ip.isEmpty() || portString.isEmpty()) {
                    Toast.makeText(SettingsActivity.this, "Trebuie să introduceți IP-ul și portul serverului!", Toast.LENGTH_LONG).show();
                    return;
                }

                int port;
                try {
                    port = Integer.parseInt(portString);
                } catch (NumberFormatException e) {
                    Toast.makeText(SettingsActivity.this, "Portul trebuie să fie un număr!", Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ServerIP", ip);
                editor.putInt("ServerPort", port);
                editor.apply();

                Toast.makeText(SettingsActivity.this, "Setări salvate", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        loadSettings();
    }

    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String ip = prefs.getString("ServerIP", "");
        int port = prefs.getInt("ServerPort", -1); // -1 sau orice altă valoare care semnifică "nedefinit"

        if (!ip.isEmpty()) {
            editTextIPAddress.setText(ip);
        }
        if (port != -1) {
            editTextPortNumber.setText(String.valueOf(port));
        }
    }
}
