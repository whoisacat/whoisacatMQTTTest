package ru.nag.shop.whoisacatmqtttest;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

abstract class MqttActions{

    private MqttAndroidClient mClient;
    private Context mContext;

    public MqttActions(Context context, String servURL, String clientId) throws NullPointerException{
        if (servURL.equals("") || clientId.equals("")){
            throw new NullPointerException();
        }
        mContext = context;
        mClient = new MqttAndroidClient(mContext, servURL,
                        clientId);
    }

    public void connecting(String user, char[] password){
        connect(user, password);
    }

    private void connect(String userName, char[] userPassword){
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            options.setUserName(userName);
            options.setPassword(userPassword);
            IMqttToken token = mClient.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(mContext, "Соединение установлено", Toast.LENGTH_LONG)
                         .show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(mContext, "Соединение не установлено", Toast.LENGTH_LONG)
                         .show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d("TAG", " \n говно catch");
        }
    }

    public void publishTopic(EditText mPostingTopic, EditText mPostingText){
        if (mClient.isConnected()){
            String topic = mPostingTopic.getText().toString();
            String payload = mPostingText.getText().toString();
            byte[] encodedPayload;
            try {
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                mClient.publish(topic, message);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        }else mPostingTopic.setText("There is no connection");
    }

    public void setCallback(){
        mClient.setCallback(new MqttCallback(){
            @Override
            public void connectionLost(Throwable cause){
                Toast.makeText(mContext, "Соединение потеряно", Toast.LENGTH_LONG)
                     .show();
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception{
                String str = message.toString();
                onMessageArrived(topic,str);
                Toast.makeText(mContext, str, Toast.LENGTH_LONG)
                     .show();
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token){
                Toast.makeText(mContext, "Успешная доставка", Toast.LENGTH_LONG)
                     .show();
            }
        });
    }

    public abstract void onMessageArrived(String subTopic, String subscribingText);

    public void unsubscribeTopic(String topic){
        try {
            IMqttToken unsubToken = mClient.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(mContext, "Отписано", Toast.LENGTH_LONG)
                         .show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                        Throwable exception) {
                    Toast.makeText(mContext, "Не тписано", Toast.LENGTH_LONG)
                         .show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeTopic(String topic){
        int qos = 1;
        try {
            IMqttToken subToken = mClient.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(mContext, "Подписано", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken,
                        Throwable exception) {
                    Toast.makeText(mContext, "Не подписано", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean disconnecting(){
        if (mClient.isConnected()){
            try {
                IMqttToken disconToken = mClient.disconnect();
                disconToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                    }
                    @Override
                    public void onFailure(IMqttToken asyncActionToken,Throwable exception) {
                    }
                });
                return true;
            } catch (MqttException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean hasConnectings(){
        return mClient.isConnected();
    }
}
