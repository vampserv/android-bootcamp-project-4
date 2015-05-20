package com.codepath.apps.twitterclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.TimelineActivity;

public class TweetComposerDialog extends DialogFragment {

    static Context mContext;
    static TimelineActivity.TweetComposerDialogFragmentListener mListener;

    public TweetComposerDialog() { }

    public static TweetComposerDialog newInstance(Context context, TimelineActivity.TweetComposerDialogFragmentListener listener) {
        TweetComposerDialog dialog = new TweetComposerDialog();
        mContext = context;
        mListener = listener;
        Bundle args = new Bundle();
//        args.putParcelable("SearchFilter", searchFilter);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_composer, container);

        // set dialog title
//        getDialog().setTitle(R.string.advanced_search_label);

        // get SearchFilter model
//        searchFilter = getArguments().getParcelable("SearchFilter");

        // initialize views
//        spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
//        spImageColor = (Spinner) view.findViewById(R.id.spImageColor);
//        spImageType = (Spinner) view.findViewById(R.id.spImageType);
//        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
//
//        btSave = (Button) view.findViewById(R.id.btSave);
//        btSave.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                saveSettings();
//            }
//        });
//
//        btCancel = (Button) view.findViewById(R.id.btCancel);
//        btCancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                cancelSettings();
//            }
//        });

//        ArrayAdapter<CharSequence> aImageSize = ArrayAdapter.createFromResource(mContext, R.array.image_size_spinner_array, android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> aImageColor = ArrayAdapter.createFromResource(mContext, R.array.image_color_spinner_array, android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> aImageType = ArrayAdapter.createFromResource(mContext, R.array.image_type_spinner_array, android.R.layout.simple_spinner_item);
//
//        aImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        aImageColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        aImageType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spImageSize.setAdapter(aImageSize);
//        spImageColor.setAdapter(aImageColor);
//        spImageType.setAdapter(aImageType);
//
//        // set defaults
//        if(searchFilter.imgsz != null && !searchFilter.imgsz.isEmpty()) {
//            int imageSizePos = aImageSize.getPosition(searchFilter.imgsz);
//            spImageSize.setSelection(imageSizePos);
//        }
//
//        if(searchFilter.imgcolor != null && !searchFilter.imgcolor.isEmpty()) {
//            int imageColorPos = aImageColor.getPosition(searchFilter.imgcolor);
//            spImageColor.setSelection(imageColorPos);
//        }
//
//        if(searchFilter.imgtype != null && !searchFilter.imgtype.isEmpty()) {
//            int imageTypePos = aImageType.getPosition(searchFilter.imgtype);
//            spImageType.setSelection(imageTypePos);
//        }
//
//        if(searchFilter.as_sitesearch != null && !searchFilter.as_sitesearch.isEmpty()) {
//            etSiteFilter.setText(searchFilter.as_sitesearch);
//        }
//
//        spImageSize.requestFocus();

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    public void saveSettings() {
//        searchFilter.imgsz = spImageSize.getSelectedItem().toString();
//        searchFilter.imgcolor = spImageColor.getSelectedItem().toString();
//        searchFilter.imgtype = spImageType.getSelectedItem().toString();
//        searchFilter.as_sitesearch = etSiteFilter.getText().toString();
//
//        mListener.updateSettings(searchFilter);
        this.dismiss();
    }

    public void cancelSettings() {
        this.dismiss();
    }
}
