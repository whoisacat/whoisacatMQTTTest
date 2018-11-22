package ru.nag.shop.whoisacatmqtttest;
//https://www.hivemq.com/blog/mqtt-client-library-enyclopedia-paho-android-service хорошая статья, без нее не сделал бы

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Launcher extends AppCompatActivity{

    private static final String TOPIC = "topic";
    public SharedPreferences sharedPreferences;
    private static final String HAS_VISITED = "hasVisited";
    private static final boolean VISITED = true;

    private EditText mPostingTopic;
    private EditText mSubscribingTopic;
    private EditText mPostingText;
    private TextView mSubscribingText;
    private Button mPostingButton;
    private Button mSubscribingButton;
    private Button mUnsubscribeButton;
    private TextView mSubTopView;
    private EditText mDomainNameView;
    private EditText mPortView;
    private EditText mUserNameView;
    private EditText mPasswordView;
    private Button mGetSettingsButton;
    private EditText mClientIdView;
    private Button mMyButton;

    private MqttActions mMqttActions;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViewsById();
        resizeViews();
        setBackgroundColors();
        setHints();
        setTextColors();
        setOnClickListeners();
    }

    private void setOnClickListeners(){
        mPostingButton.setOnClickListener(v->{
            try{
                mMqttActions.publishTopic(mPostingTopic, mPostingText);
            } catch (NullPointerException npe){
                Toast.makeText(getApplicationContext(), "Нет соединения", Toast.LENGTH_LONG).show();
            }
        });

        mSubscribingButton.setOnClickListener(v->{
            if (mMqttActions != null && mMqttActions.hasConnectings()){
                String topic = mSubscribingTopic.getText().toString();
                mMqttActions.subscribeTopic(topic);
            }else Toast.makeText(getApplicationContext(), "Нет соединения", Toast.LENGTH_LONG).show();
        });

        mUnsubscribeButton.setOnClickListener(v->{
            if (mMqttActions != null && mMqttActions.hasConnectings()){
                final String topic = mSubscribingTopic.getText().toString();
                mMqttActions.unsubscribeTopic(topic);
            }else Toast.makeText(getApplicationContext(), "Нет соединения", Toast.LENGTH_LONG).show();
        });

        mGetSettingsButton.setOnClickListener(v->makeConnection());
        mMyButton.setOnClickListener(v->makeTestConnection());
        mMyButton.setOnLongClickListener(v -> unlightAndDisconnect());
    }

    private void findViewsById(){
        mPostingTopic = (EditText) findViewById(R.id.postTopicTextView);
        mPostingText = (EditText) findViewById(R.id.postBodyTextView);
        mPostingButton = (Button) findViewById(R.id.postButton);
        mSubscribingTopic = (EditText) findViewById(R.id.subTopicEditText);
        mSubscribingButton = (Button) findViewById(R.id.subButton);
        mSubscribingText = (TextView) findViewById(R.id.subBodyTextView);
        mUnsubscribeButton = (Button) findViewById(R.id.unSubButton);
        mSubTopView = (TextView) findViewById(R.id.subTopicTextView);
        mDomainNameView = (EditText) findViewById(R.id.domain_name);
        mPortView = (EditText) findViewById(R.id.port);
        mUserNameView = (EditText) findViewById(R.id.user_name);
        mPasswordView = (EditText) findViewById(R.id.password_view);
        mGetSettingsButton = (Button) findViewById(R.id.get_settings_button);
        mClientIdView = (EditText) findViewById(R.id.client_id);
        mMyButton = (Button) findViewById(R.id.my_settings);
    }

    private void setTextColors(){
        mPostingTopic.setTextColor(getResources().getColor(R.color.border_color));
        mSubscribingTopic.setTextColor(getResources().getColor(R.color.border_color));
        mPostingText.setTextColor(getResources().getColor(R.color.border_color));
        mSubscribingText.setTextColor(getResources().getColor(R.color.border_color));
        mSubTopView.setTextColor(getResources().getColor(R.color.border_color));
        mDomainNameView.setTextColor(getResources().getColor(R.color.border_color));
        mPortView.setTextColor(getResources().getColor(R.color.border_color));
        mUserNameView.setTextColor(getResources().getColor(R.color.border_color));
        mPasswordView.setTextColor(getResources().getColor(R.color.border_color));
        mClientIdView.setTextColor(getResources().getColor(R.color.border_color));
    }

    private void setHints(){
        mPostingTopic.setHint(getResources().getString(R.string.topic_for_posting));
        mSubscribingTopic.setHint(getResources().getString(R.string.text_for_subscription));
        mPostingText.setHint(getResources().getString(R.string.text_for_posting));
        mSubscribingText.setHint(getResources().getString(R.string.text_of_subscription));
        mSubTopView.setHint(getResources().getString(R.string.topic_of_subscription));
        mDomainNameView.setHint(getResources().getString(R.string.domain_hint));
        mPortView.setHint(getResources().getString(R.string.port_hint));
        mUserNameView.setHint(getResources().getString(R.string.user_hint));
        mPasswordView.setHint(getResources().getString(R.string.password_hint));
        mClientIdView.setHint(getResources().getString(R.string.client_id));
    }

    private void setBackgroundColors(){
        mPostingTopic.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mSubscribingTopic.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mPostingText.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mSubscribingText.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mSubTopView.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mDomainNameView.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mPortView.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mUserNameView.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mPasswordView.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
        mClientIdView.setBackgroundColor(getResources().getColor(R.color.num_picker_bck_gnd));
    }

    private void makeConnection(){
        mGetSettingsButton.setTextColor(Color.BLACK);
        String clintId = mClientIdView.getText().toString();//"7ec4ff81-889e-4aa6-bdd4-6b619579ea22"
        //clintId = MqttClient.generateClientId());

        String url = mDomainNameView.getText().toString();//ssl://m23.cloudmqtt.com
        String port = mPortView.getText().toString();//29975
        String user = mUserNameView.getText().toString();//"admin"
        char [] password = mPasswordView.getText().toString().toCharArray();//"admin".toCharArray()
        try{
            newMqttActionObject(clintId, url, port);
            try{
                mMqttActions.connecting(user, password);
                if (mMqttActions.hasConnectings())
                    mMqttActions.setCallback();
            }catch (IllegalArgumentException iae){
                mGetSettingsButton.setText("Enter settings");
                mGetSettingsButton.setTextColor(getResources().getColor(R.color.border_color));
            }
        } catch (NullPointerException npe){
            Toast.makeText(getApplicationContext(), "Или заполните поля с настройками или " +
                    "воспользуйтесь тестовыми настройками", Toast.LENGTH_LONG).show();
        }
    }

    private void newMqttActionObject(String clintId, String url, String port){
        mMqttActions = new MqttActions(getApplicationContext(), url.concat(":")
                                                                   .concat(port), clintId){
            @Override
            public void onMessageArrived(String subTopic, String subscribingText){
                updSubViews(subTopic, subscribingText);
            }
        };
    }

    private void updSubViews(String subTopic, String subscribingText){
        mSubTopView.setText(subTopic);
        mSubscribingText.setText(subscribingText);
    }

    private void makeTestConnection(){
        String clintId = "7ec4ff81-889e-4aa6-bdd4-6b619579ea22";
        String url = "ssl://m23.cloudmqtt.com";
        String port = "29975";
        String user = "admin";
        char [] password = "admin".toCharArray();
        //
        //MqttClient.generateClientId());
        newMqttActionObject(clintId, url, port);
        mMqttActions.connecting(user,password);
        mMqttActions.setCallback();
        mMyButton.setTextColor(getResources().getColor(R.color.border_color));
    }

    private Point getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mMqttActions.disconnecting();
    }

    private boolean unlightAndDisconnect(){
        mMyButton.setTextColor(Color.BLACK);
        return mMqttActions.disconnecting();
    }

    private void resizeViews(){
        Point size = getScreenSize();
        mSubscribingText.setWidth(size.x);
        mDomainNameView.setWidth(size.x);
        mPortView.setWidth(size.x);
        mUserNameView.setWidth(size.x);
        mClientIdView.setWidth(size.x);
        mPasswordView.setWidth(size.x);

        int x = ((int) (size.x * 0.5));
        mMyButton.setWidth(x);
        mGetSettingsButton.setWidth(x);

        x = (int) (size.x * 0.7);

        mSubscribingTopic.setWidth(x);
        mSubTopView.setWidth(x);
        mPostingText.setWidth(x);

        x = (int) (size.x * 0.3);

        mPostingButton.setWidth(x);
        mSubscribingButton.setWidth(x);
        mUnsubscribeButton.setWidth(x);
    }
}
