package jgoguette.twigzolupolus.ca.ycgshifts.Main.Views;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-14.
 *
 * FeedHolder
 */

public class FeedHolder extends RecyclerView.ViewHolder {
    private View view;

    public FeedHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) view.findViewById(R.id.tvData);
        field.setText(name);
    }

    public void setStatus(String status) {
        TextView field = (TextView) view.findViewById(R.id.txtStatusMsg);
        field.setText(status);
    }

    public void setTimeStamp(Long timeStamp) {
        TextView field = (TextView) view.findViewById(R.id.timestamp);
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                timeStamp,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        field.setText(timeAgo);
    }

    public void setProfilePic(final Context context, final String firebaseId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(firebaseId
                + context.getString(R.string.profilePicPath));

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Pass it to Picasso to download, show in ImageView and caching
                ImageView profilePic = (ImageView) view.findViewById(R.id.profilePic);
                Picasso.with(context).load(uri.toString()).fit().into(profilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                ImageView profilePic = (ImageView) view.findViewById(R.id.profilePic);
                profilePic.setImageResource(R.drawable.no_picture);
            }
        });
    }
}
