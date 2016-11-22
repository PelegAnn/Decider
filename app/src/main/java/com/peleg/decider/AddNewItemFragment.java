package com.peleg.decider;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peleg.decider.com.peleg.decider.db.DbBitmapUtility;
import com.peleg.decider.com.peleg.decider.db.ItemsDBHelper;
import com.peleg.decider.com.peleg.decider.db.ItemsReaderContract;

import java.util.ArrayList;
import java.util.List;

import static com.peleg.decider.com.peleg.decider.db.DbBitmapUtility.getBytes;

/**
 * Created by Annie on 11/4/16.
 */
public class AddNewItemFragment extends Fragment {

    private EditText mItemName;
    private RatingBar mRBRank;
    ViewSwitcher simpleViewSwitcher;
    private Button mImage, mDone, mCancel;
    private ImageView mImageView;
    private View view;
    private Bitmap imageBitmap;
    public static AddNewItemFragment newInstance() {
        Bundle args = new Bundle();
        AddNewItemFragment fragment = new AddNewItemFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view =  inflater.inflate(R.layout.dialogfragment_addnewitem, container, false);

        mItemName = (EditText) view.findViewById(R.id.item_name);
        mRBRank = (RatingBar) view.findViewById(R.id.item_ratingBar) ;
        mImageView = (ImageView) view.findViewById(R.id.item_thumb_image);
        mImage = (Button) view.findViewById(R.id.item_image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getActivity().getPackageManager();
                if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA))
                    // dispatchTakePictureIntent();
                test();
            }
        });
        mDone = (Button) view.findViewById(R.id.saveButton);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemName == null || mRBRank == null) {
                    Snackbar.make(view, "Please fill in the required fields", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    ItemOption item;
                    if (imageBitmap != null)
                        item = new ItemOption(mItemName.getText().toString(), mRBRank.getRating(),imageBitmap);
                    else {
                        item = new ItemOption(mItemName.getText().toString(), mRBRank.getRating());
                    }
                    instertItemToDB(item);

                }
            }
        });

        simpleViewSwitcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher); // get the reference of ViewSwitcher
        View nextView=simpleViewSwitcher.getNextView(); // get next view to be displayed


        return view;

    }

    private void instertItemToDB(ItemOption item) {
        WriteToDBTask writeToDB = new WriteToDBTask();
        writeToDB.execute(item);

    }


    static final int REQUEST_IMAGE_CAPTURE = 1888;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void test() {
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.MEDIA_IGNORE_FILENAME, ".nomedia");

            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.add_new));

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);
    }
//
//    private void addToDB(ItemOption item) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("item");
//
//        myRef.setValue(item.getName(),item.getRank());
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("AddNew", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.e("AddNew", "Failed to read value.", error.toException());
//            }
//        });
//    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            simpleViewSwitcher.showNext();
            mImageView.setImageBitmap(imageBitmap);
        }
    }


    private class WriteToDBTask extends AsyncTask<ItemOption, Void, Long> {

        @Override
        protected Long doInBackground(ItemOption... items) {
            ItemsDBHelper mDbHelper = new ItemsDBHelper(getContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_NAME, items[0].getName());
            values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_RANK, items[0].getRank());
            values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_IMAGE, getBytes(items[0].getImage()));

            // Insert the new row, returning the primary key value of the new row
            return db.insert(ItemsReaderContract.ItemEntry.TABLE_NAME, null, values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Snackbar.make(view, "Cancelled! try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Log.e("TAG",String.valueOf(aLong));
            Snackbar.make(view, "Item no. "+String.valueOf(aLong)+" successfully saved! ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            //TODO doneAddingNewItem();
        }
    }

}
