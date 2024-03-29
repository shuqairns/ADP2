package com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nazirshuqair.lastpick.R;
import com.android.nazirshuqair.lastpick.textViewHelper.AutoResizeTextView;
import com.squareup.picasso.Picasso;

/**
 * Created by nazirshuqair on 12/13/14.
 */
public class FeaturedFragment extends Fragment {

    public static final String TAG = "FeaturedFragment.TAG";
    private static final String ARG_NAME = "FeaturedFragment.ARG_NAME";
    private static final String ARG_PHONE = "FeaturedFragment.ARG_PHONE";
    private static final String ARG_ADDRESS = "FeaturedFragment.ARG_ADDRESS";
    private static final String ARG_IMGURL = "FeaturedFragment.ARG_IMGURL";

    ImageView fImage;
    AutoResizeTextView fName;
    TextView fPhone;
    TextView fAddress;
    Button restore;
    Button details;

    public interface RestoreClickListener{
        public void restore();
        public void toDetails();
    }

    private RestoreClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof RestoreClickListener) {
            mListener = (RestoreClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    public static FeaturedFragment newInstance(String _name, String _phone, String _address, String _imgUrl) {
        FeaturedFragment frag = new FeaturedFragment();

        Bundle args = new Bundle();
        args.putString(ARG_NAME, _name);
        args.putString(ARG_PHONE, _phone);
        args.putString(ARG_ADDRESS, _address);
        args.putString(ARG_IMGURL, _imgUrl);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.featured_fragment, _container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_NAME)) {
            updateDisplay(args.getString(ARG_NAME),
                    args.getString(ARG_PHONE),
                    args.getString(ARG_ADDRESS),
                    args.getString(ARG_IMGURL));

            details = (Button) getView().findViewById(R.id.featured_details);

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.toDetails();
                }
            });

            restore = (Button) getView().findViewById(R.id.restore);


            restore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.restore();
                }
            });
        }
    }

    public void updateDisplay(String _name, String _phone, String _address, String _imgUrl){

        fImage = (ImageView) getView().findViewById(R.id.featuredImage);
        fName = (AutoResizeTextView) getView().findViewById(R.id.featuredName);
        fPhone = (TextView) getView().findViewById(R.id.featuredPhone);
        fAddress = (TextView) getView().findViewById(R.id.featuredAddress);

        fName.setText(_name);
        fName.resizeText();
        fPhone.setText(_phone);
        fAddress.setText(_address);
        Picasso.with(getActivity()).load(_imgUrl).into(fImage);

    }


}
