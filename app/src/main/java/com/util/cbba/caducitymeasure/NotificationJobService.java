package com.util.cbba.caducitymeasure;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobService;
import com.util.cbba.caducitymeasure.persistence.ItemRepository;
import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.Calendar;
import java.util.List;

public class NotificationJobService extends JobService {

    private static final String TAG = NotificationJobService.class.getSimpleName();
    private NotificationManager mNotifyManager;
    private ItemRepository itemRepository;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    private void createNotificationChannel() {
        // Define notification manager object.
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "Job Service notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifications from Job Service");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        createNotificationChannel();
        Intent intent= new Intent(this, MainActivity.class);
        final PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, PRIMARY_CHANNEL_ID);
        itemRepository = new ItemRepository(getApplication());
        Observer<Integer> obsResult = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer > 0) {
                    builder.setContentTitle(getApplicationContext().getString(R.string.notificacion_title))
                            .setContentText(getApplicationContext().getString(R.string.notificacion_msg, integer.toString()))
                            .setContentIntent(contentPendingIntent)
                            .setSmallIcon(R.drawable.ic_job_running)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                            .setAutoCancel(true);
                    mNotifyManager.notify(0, builder.build());
                } else {
                    Log.d(TAG, "Nothing to expire right now");
                }
            }
        };

        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        Calendar to = Calendar.getInstance();
        to.add(Calendar.DATE, 3);
        to.set(Calendar.HOUR_OF_DAY, 23);
        to.set(Calendar.MINUTE, 59);
        itemRepository.getAllItemsByExpirationNextNDaysPending(from.getTime(), to.getTime()).observeForever(obsResult);
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }
}
