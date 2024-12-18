package kz.beta.chatblock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    QuickActions actions;
    private DatabaseReference databaseReference;
    ProgressBar progress;
    LinearLayout result_parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        actions = new QuickActions(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        result_parent = findViewById(R.id.result_parent);
        progress = findViewById(R.id.progress);
        TextView me = findViewById(R.id.me);
        me.setText("@"+actions.getAuth());
        addMeToList();
        GetList();
    }
    void addMeToList(){
        String myName = actions.getAuth();

        databaseReference.child("Users").child(actions.getId() + "").setValue(myName).addOnCompleteListener(task -> {
            System.out.println("Username is sent: " + myName);
        });
    }
    void GetList(){
        progress.setVisibility(View.VISIBLE);
        databaseReference.child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progress.setVisibility(View.GONE);
                        List<User> appUsers = new ArrayList<>();

                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                            String someUser = messageSnapshot.getValue(String.class);
                            String someUserId = messageSnapshot.getKey();
                            if(someUser != null && someUserId != null){
                                int ID = Integer.parseInt(someUserId);
                                if(ID != actions.getId()){
                                    User nUser = new User(someUser, ID);
                                    appUsers.add(nUser);
                                }
                            }
                        }
                        DisplayChatLinks(appUsers);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    void DisplayChatLinks(List<User> appUsers){
        result_parent.removeAllViews();
        for(User current : appUsers){
            View itemView = getLayoutInflater().inflate(R.layout.chat_item, result_parent, false);
            TextView title = itemView.findViewById(R.id.name);
            title.setText(current.getUserID());
            itemView.setOnClickListener(v -> {
                ChatPage chat = new ChatPage(current);
                chat.show(getSupportFragmentManager(), current.getId() + "_chat");
            });
            itemView.setOnLongClickListener(v -> {
                Toast.makeText(this, "User: " +current.getUserID()+ "\nID: " + current.getId(), Toast.LENGTH_SHORT).show();
                return true;
            });
            result_parent.addView(itemView);
            result_parent.addView(Divider());
        }
    }

    View Divider(){
        View divider = new View(this);
        divider.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 16));
        int color = Color.parseColor("#00ffffff");
        divider.setBackgroundColor(color);
        return  divider;
    }
}