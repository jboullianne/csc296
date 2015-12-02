/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    private static final String TAG = "CAMERA_FRAGMENT";

    private RecyclerView mRecyclerView;
    private CameraAdapter mAdapter;


    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_camera);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        List<Uri> pictures = getPictureList();
        mAdapter = new CameraAdapter(pictures);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    //onResume we need to update the UI since the dataset has changed
    @Override
    public void onResume(){
        super.onResume();

        List<Uri> pictures = getPictureList();
        Log.d(TAG, pictures.toString());
        if(mAdapter == null) {
            mAdapter = new CameraAdapter(pictures);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setImageList(pictures);
        }
    }

    //Returns Picture List From File System
    public LinkedList<Uri> getPictureList(){
        LinkedList<Uri> list = new LinkedList<>();

        File pictureDirectory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for(int i = 0; i<pictureDirectory.listFiles().length; i++){
            list.add(Uri.fromFile(pictureDirectory.listFiles()[i]));
        }

        return list;
    }

}
