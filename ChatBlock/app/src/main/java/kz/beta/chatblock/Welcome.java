package kz.beta.chatblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);



        QuickActions _action = new QuickActions(Welcome.this);
        if(_action.getId() == 0){
            _action.saveId();
        }

        if(_action.getAuth() != null && _action.getAuth().length() > 3){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        ImageView next = findViewById(R.id.next);
        EditText input = findViewById(R.id.input);
        next.setVisibility(View.GONE);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                next.setVisibility(count > 3 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        next.setOnClickListener(v -> {
            String p_ = input.getText().toString();
            if(p_.length() > 3){
                _action.saveAuth(p_);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}