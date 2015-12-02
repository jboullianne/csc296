/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by JeanMarc on 11/1/15.
 */
public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {

    public static final String KEY_FULL_IMAGE = "csc296.assignment08.FULL_IMAGE";

    private static final String TAG = "CAMERA_ADAPTER";
    private List<Uri> mImageList; //Contains References to all of the image files

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImage;
        public Uri imageFile;
        public ViewHolder(View v) {
            super(v);
            mImage = (ImageView) v.findViewById(R.id.image_view_camera);
        }
    }

    public CameraAdapter(List<Uri> imageList) {
        mImageList = imageList;
    }

    @Override
    public CameraAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_image, parent, false);

        final ViewHolder holder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "IMAGE PRESSED");

                Intent intent = new Intent(v.getContext(), FullScreenImageView.class);
                intent.putExtra(KEY_FULL_IMAGE, holder.imageFile);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mImage.setImageBitmap(BitmapFactory.decodeFile(mImageList.get(position).getPath()));
        holder.mImage.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.imageFile = mImageList.get(position);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public void setImageList(List<Uri> mImageList) {
        this.mImageList = mImageList;
    }

}
