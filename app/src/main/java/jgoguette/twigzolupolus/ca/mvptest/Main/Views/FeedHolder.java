package jgoguette.twigzolupolus.ca.mvptest.Main.Views;

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

import jgoguette.twigzolupolus.ca.mvptest.R;

/**
 * Created by jerry on 2016-11-14.
 */

public class FeedHolder extends RecyclerView.ViewHolder {
    private View mView;

    public FeedHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.tvData);
        field.setText(name);
    }

    public void setStatus(String status) {
        TextView field = (TextView) mView.findViewById(R.id.txtStatusMsg);
        field.setText(status);
    }

    public void setTimeStamp(Long timeStamp) {
        TextView field = (TextView) mView.findViewById(R.id.timestamp);
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                timeStamp,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        field.setText(timeAgo);
    }

    public void setProfilePic(final Context context, String firebaseId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(firebaseId
                + context.getString(R.string.profilePicPath) + ".jpg");

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Pass it to Picasso to download, show in ImageView and caching
                ImageView profilePic = (ImageView) mView.findViewById(R.id.profilePic);
                Picasso.with(context).load(uri.toString()).fit().into(profilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
