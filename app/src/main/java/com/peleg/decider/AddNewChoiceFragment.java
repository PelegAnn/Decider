package com.peleg.decider;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ViewSwitcher;

import com.peleg.decider.com.peleg.decider.db.DbBitmapUtility;
import com.peleg.decider.com.peleg.decider.db.ItemsDBHelper;
import com.peleg.decider.com.peleg.decider.db.ItemsReaderContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.peleg.decider.com.peleg.decider.db.DbBitmapUtility.getBytes;

/**
 * Created by Annie on 11/4/16.
 */
public class AddNewChoiceFragment extends Fragment {

    public static final int REQUEST_IMAGE_CAPTURE = 1888;
    private static String mCurrentPhotoPath;

    private EditText mItemName;
    private RatingBar mRBRank;
    ViewSwitcher simpleViewSwitcher;
    private Button mImage, mDone, mCancel;
    private ImageView mImageView;
    private View view;
    private ImageView nextView;


    public static AddNewChoiceFragment newInstance() {
        Bundle args = new Bundle();
        AddNewChoiceFragment fragment = new AddNewChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view =  inflater.inflate(R.layout.dialogfragment_addnewitem, container, false);

        simpleViewSwitcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher); // get the reference of ViewSwitcher

        mItemName = (EditText) view.findViewById(R.id.item_name);
        mRBRank = (RatingBar) view.findViewById(R.id.item_ratingBar) ;
        // second view
        mImageView = (ImageView) view.findViewById(R.id.item_thumb_image);
        // first view
        mImage = (Button) view.findViewById(R.id.item_image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getActivity().getPackageManager();
                if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA))
                    dispatchTakePictureIntent();
                //test();
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
                    Choice item;
                    if (mCurrentPhotoPath != null) {
                        item = new Choice(mItemName.getText().toString(), mRBRank.getRating(), mCurrentPhotoPath);
                        mCurrentPhotoPath = null;
                    } else {
                        item = new Choice(mItemName.getText().toString(), mRBRank.getRating());
                    }
                    insertItemToDB(item);

                }
            }
        });


        //mImageView= (ImageView) simpleViewSwitcher.getNextView(); // get next view to be displayed


        return view;

    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void insertItemToDB(Choice item) {
        WriteToDBTask writeToDB = new WriteToDBTask();
        writeToDB.execute(item);

    }


    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {

            Picasso.with(getActivity()).load(new File(mCurrentPhotoPath))
                    .fit().centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mImageView);

            if (simpleViewSwitcher.getCurrentView() != mImageView){
                simpleViewSwitcher.showPrevious();
            } else {
                simpleViewSwitcher.showNext();
            }

            galleryAddPic();
        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.peleg.decider.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
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

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            simpleViewSwitcher.showNext();
//            mImageView.setImageBitmap(imageBitmap);
            handleBigCameraPhoto();
        }
    }


    private class WriteToDBTask extends AsyncTask<Choice, Void, Long> {

        @Override
        protected Long doInBackground(Choice... items) {
            ItemsDBHelper mDbHelper = new ItemsDBHelper(getContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_NAME, items[0].getName());
            values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_RANK, items[0].getRank());
            values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_IMAGE, items[0].getImagePath());

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

            //TODO doneAddNewItem();
        }
    }

}
