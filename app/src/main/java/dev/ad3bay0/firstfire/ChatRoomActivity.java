package dev.ad3bay0.firstfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatRoomActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private static final int SIGN_IN_REQUEST_CODE = 123;
    private FloatingActionButton fab;
    private ListView listOfMessages;
    private EditText input;
    private TextInputLayout inputTextLayout;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        fab  = findViewById(R.id.fab);

        input = findViewById(R.id.input);
        inputTextLayout = findViewById(R.id.inputLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputTextLayout.setError("");

                String chatMessage = input.getText().toString();

                if(!TextUtils.isEmpty(chatMessage)&&!(chatMessage.trim().length()==0)){

                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(chatMessage,auth.getCurrentUser().getDisplayName()));

                    input.setText("");


                }else{


                    inputTextLayout.setError("please enter a chat message before sending!");

                }
            }
        });



        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()==null){

            //start sign in/sign up activity

            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);


        }else{

            //user already signed in, therefore display welcome toast

            Toast.makeText(this,"Welcome "+auth.getCurrentUser().getDisplayName(),Toast.LENGTH_LONG).show();

            //loadchat room

            displayChatMessages();

        }
    }


    private void displayChatMessages(){
        listOfMessages = findViewById(R.id.list_of_messages);

        adapter =  new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //get reference to the vies of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                //format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));

            }
        };

        listOfMessages.setAdapter(adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE){


            if(resultCode == RESULT_OK){

                Toast.makeText(this,"Successfully signed in. Welcome!",Toast.LENGTH_LONG).show();

                displayChatMessages();


            }else{


                Toast.makeText(this,"We couldnt sign you in please try again!",Toast.LENGTH_LONG).show();

            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main2,menu);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() ==R.id.action_sign_out){


            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {


                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if(task.isSuccessful()){

                        Toast.makeText(ChatRoomActivity.this,"You have been successfully signed out.",Toast.LENGTH_LONG).show();

                        //close activity
                        finish();


                    }else{

                        Toast.makeText(ChatRoomActivity.this,"Cant sign out at the moment! Please try again!",Toast.LENGTH_LONG).show();
                    }
                }
            });


        }

        return true;
    }
}
