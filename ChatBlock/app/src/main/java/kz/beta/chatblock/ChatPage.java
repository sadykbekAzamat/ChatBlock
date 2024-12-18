package kz.beta.chatblock;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatPage extends DialogFragment {

    View dialogView;
    LinearLayout content;
    TextView username_with_;
    EditText input;
    ImageView send;
    User with_;
    String current_chat_id;
    ProgressBar progress;

    QuickActions actions;
    private DatabaseReference databaseReference;
    public ChatPage(User with_) {
        this.with_ = with_;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.FullScreenServiceDialog);
        dialogView = getLayoutInflater().inflate(R.layout.page_chat, null);
        builder.setView(dialogView);
        actions = new QuickActions(requireContext());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        current_chat_id = BlockchainUtils.generateChatID(String.valueOf(actions.getId()), String.valueOf(with_.getId()));



        content = dialogView.findViewById(R.id.result_parent);
        username_with_ = dialogView.findViewById(R.id.username);
        input = dialogView.findViewById(R.id.input);
        send = dialogView.findViewById(R.id.send);
        progress = dialogView.findViewById(R.id.progress);

        username_with_.setText(with_.getUserID());
        send.setOnClickListener(v -> {
            String _m = input.getText().toString();
            if(!_m.isEmpty()){
                try {
                    Message message = new Message(QuickActions.NewTimeMilliId(), BlockchainUtils.encrypt(_m, current_chat_id), actions.getId());
                    sendMessage(message);
                    input.setText("");
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });

        InitPage();

        return builder.create();
    }
    void InitPage(){
        progress.setVisibility(View.VISIBLE);
        databaseReference.child("chats").child(current_chat_id).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        progress.setVisibility(View.GONE);
                        if(snapshot.exists()){
                            List<Message> messages = new ArrayList<>();
                            for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                Message message = messageSnapshot.getValue(Message.class);
                                if(message != null){
                                    messages.add(message);
                                }
                            }
                            if(!messages.isEmpty()){
                                DisplayChatLinks(messages);
                            }
                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        progress.setVisibility(View.GONE);
                    }
                });

    }


    void DisplayChatLinks(List<Message> messages){
        content.removeAllViews();
        for(Message current : messages){
            View itemView = getLayoutInflater().inflate(current.getSenderID() == actions.getId() ? R.layout.message_item : R.layout.message_item_with, content, false);
            TextView title = itemView.findViewById(R.id.content);
            try {
                title.setText(BlockchainUtils.decrypt(current.getContent(), current_chat_id));
            }catch (Exception e){

            }
            content.addView(itemView);
            content.addView(Divider());
        }
    }


    public void sendMessage(Message message) {
        databaseReference.child("chats").child(current_chat_id).child("messages").child(message.getMessageID()).setValue(message).addOnCompleteListener(task -> {

        });
    }



    View Divider(){
        View divider = new View(requireContext());
        divider.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 16));
        int color = Color.parseColor("#00ffffff");
        divider.setBackgroundColor(color);
        return  divider;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenServiceDialog);
    }
}
