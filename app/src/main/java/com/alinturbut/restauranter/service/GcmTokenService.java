//package com.alinturbut.restauranter.service;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.util.Log;
//
//import com.alinturbut.restauranter.R;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.iid.InstanceID;
//
//import java.io.IOException;
//
//
///**
// * An {@link IntentService} subclass for handling asynchronous task requests in
// * a service on a separate handler thread.
// * <p/>
// * TODO: Customize class - update intent actions and extra parameters.
// */
//public class GcmTokenService extends IntentService {
//    //actions
//    public static final String ACTION_GET_TOKEN = "com.alinturbut.restauranter.service.action.get.token.action";
//    public static final String TOKEN_PUBLISH_INTENT = "com.alinturbut.restauranter.service.token.publish";
//
//    //parameters
//    public static final String TOKEN_PARAM = "com.alinturbut.restauranter.service.param.token";
//
//    public GcmTokenService() {
//        super("GcmTokenService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_GET_TOKEN.equals(action)) {
//                getTokenAndPublish();
//            }
//        }
//    }
//
//    private void getTokenAndPublish() {
//        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
//        String token = "";
//        try {
//            token = instanceID.getToken(getString(R.string.gcm_restauranter_id), GoogleCloudMessaging
//                    .INSTANCE_ID_SCOPE, null);
//        } catch (IOException e) {
//            Log.e("GcmTokenService", "Unable to get Google Cloud Messaging token");
//        }
//
//        if(token.length() > 1) {
//            publishToken(token);
//        }
//    }
//
//    private void publishToken(String gcmToken) {
//        Intent intent = new Intent(TOKEN_PUBLISH_INTENT);
//        intent.putExtra(TOKEN_PARAM, gcmToken);
//        sendBroadcast(intent);
//    }
//
//}
