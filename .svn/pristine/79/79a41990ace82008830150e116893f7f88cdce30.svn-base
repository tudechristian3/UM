package com.ultramega.shop.adapter.settings;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.notification.NotificationActivityDetails;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User-PC on 6/5/2017.
 */
public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Notification> mGridData = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView notifName;
        private final TextView nDate;
        private final TextView message;
        private final ImageView imagePic;

        public MyViewHolder(View itemView) {
            super(itemView);
            notifName = (TextView) itemView.findViewById(R.id.txv_action_notification_name);
            nDate = (TextView) itemView.findViewById(R.id.txv_action_notification_date);
            message = (TextView) itemView.findViewById(R.id.txv_action_notification_content);
            imagePic = (ImageView) itemView.findViewById(R.id.img_notification);
        }
    }

    public NotificationsRecyclerAdapter(Context context, List<Notification> mGridData) {
        this.mContext = context;
        this.mGridData = mGridData;
    }

    private Context getContext() {
        return mContext;
    }

    public void setNotificationsData(List<Notification> mGridData) {
        int startPos = this.mGridData.size() + 1;
        this.mGridData.clear();
        this.mGridData.addAll(mGridData);
        notifyItemRangeInserted(startPos, mGridData.size());

//        this.mGridData = mGridData;
//        notifyDataSetChanged();
    }

    public void clear() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyItemRangeRemoved(0, startPos);
    }

    @Override
    public NotificationsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_action_notification_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationsRecyclerAdapter.MyViewHolder holder, int position) {
        Notification notif = mGridData.get(position);
        holder.notifName.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", notif.getSender()));
        holder.nDate.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", CommonFunctions.convertDateNotification(notif.getDateTimeIN())));
        holder.message.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", notif.getMessage() + "..."));
        Glide.with(getContext())
                .load(notif.getSenderLogo())
                .apply(new RequestOptions()
                        .encodeFormat(Bitmap.CompressFormat.PNG)
                        .downsample(DownsampleStrategy.AT_LEAST)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.imagePic);

        if (notif.getReadStatus().equals("1")) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whitePrimary));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_E4E4E4));
        }

        holder.itemView.setTag(notif);
        holder.itemView.setOnClickListener(viewHistory);
    }

    private View.OnClickListener viewHistory = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Notification notification = (Notification) v.getTag();
            NotificationActivityDetails.start(getContext(), notification);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

}
