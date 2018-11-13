package com.semicolon.rests.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.semicolon.rests.R;
import com.semicolon.rests.activities.HomeActivity;
import com.semicolon.rests.models.NotificationCountModel;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.preferences.Preferences;
import com.semicolon.rests.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class MyFirBaseMessagingService extends FirebaseMessagingService {
    Preferences preferences = Preferences.getInstance();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> map = remoteMessage.getData();
        for (String key:map.keySet())
        {
            Log.e("Key:",key+"\n"+"Value :"+map.get(key));
        }
        ManageNotification(map,getUser(),getSession(),remoteMessage.getSentTime());

    }

    private void ManageNotification(final Map<String, String> map, UserModel user, String session, long not_time) {

        if (session!=null|| !TextUtils.isEmpty(session))
        {
            if (session.equals(Tags.session_login))
            {
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);


                String current_id = user.getUser_id();
                String to_id = map.get("to_user_id");

                if (current_id.equals(to_id))
                {
                    new Handler(Looper.getMainLooper())
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    CreateNotification(map.get("main_title"),map.get("message_content"),map.get("image"),manager,builder);
                                }
                            },100);
                }
            }
        }

    }

    private void CreateNotification(final String title, final String msg, String from_image, final NotificationManager manager, final NotificationCompat.Builder builder) {

        Log.e("title",title);
        Log.e("msg",msg);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            Log.e("nnnnnnnn","nnnnnnnnnnn");

            final int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";
            CharSequence name ="my_channel_name";
            String notPath = "android.resource://"+getPackageName()+"/"+ R.raw.not;
            int importance = NotificationManager.IMPORTANCE_HIGH;

            Intent intent = new Intent(MyFirBaseMessagingService.this, HomeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            mChannel.setShowBadge(true);
            mChannel.setSound(Uri.parse(notPath),new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build());

            final Notification.Builder notification = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.logo)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent);
            final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);


            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    notification.setLargeIcon(bitmap);
                    mNotificationManager.notify(notifyID , notification.build());
                    EventBus.getDefault().post(new NotificationCountModel());
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(this).load(Tags.IMAGE_PATH+from_image).into(target);

        }else
        {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    builder.setSmallIcon(R.drawable.logo);
                    Intent intent = new Intent(MyFirBaseMessagingService.this, HomeActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MyFirBaseMessagingService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentTitle(title);
                    String notPath = "android.resource://"+getPackageName()+"/"+ R.raw.not;
                    builder.setSound(Uri.parse(notPath));
                    builder.setContentIntent(pendingIntent);
                    builder.setContentText(msg);

                    manager.notify(0,builder.build());

                    EventBus.getDefault().post(new NotificationCountModel());

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(this).load(Tags.IMAGE_PATH+from_image).into(target);

        }

    }


    private UserModel getUser()
    {
        UserModel userData = preferences.getUserData(this);
        return userData;
    }

    private String getSession()
    {
        String session = preferences.getSession(this);
        return session;
    }
}
