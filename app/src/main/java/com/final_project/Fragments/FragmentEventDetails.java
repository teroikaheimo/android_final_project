package com.final_project.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.final_project.R;

public class FragmentEventDetails extends Fragment {
    private ImageView imageView;
    private TextView eventName, eventLocation, eventTime, eventPrice, eventDescription;
    private String eStartTime, eEndTime, eImageUrls;
    private OnFragmentInteractionListener mListener;
    private RequestQueue requestQueue;
    private ConstraintLayout imageLayout;
    private LinearLayout timeLayout;

    public FragmentEventDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_details, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());


        imageView = v.findViewById(R.id.event_details_image);
        eventName = v.findViewById(R.id.event_detail_name);
        eventLocation = v.findViewById(R.id.event_detail_location);
        eventTime = v.findViewById(R.id.event_detail_time);
        timeLayout = v.findViewById(R.id.event_detail_time_layout);
        eventPrice = v.findViewById(R.id.event_detail_price);
        eventDescription = v.findViewById(R.id.event_detail_description);
        imageLayout = v.findViewById(R.id.event_details_image_layout);

        if (getArguments() != null) {
            Bundle b = getArguments();
            eventName.setText(b.getString("EVENT_ITEM_NAME"));
            eventLocation.setText(Html.fromHtml(b.getString("EVENT_ITEM_PLACE_NAME")));
            eventPrice.setText(Html.fromHtml(b.getString("EVENT_ITEM_PRICE")));

            eImageUrls = b.getString("EVENT_ITEM_IMAGE_URLS");
            Log.d("the IMAGE URL", eImageUrls.substring(1, eImageUrls.length() - 1));
            if (eImageUrls.length() > 5) {
                getEventImage(eImageUrls.substring(1, eImageUrls.length() - 1));
            } else {
                imageLayout.setVisibility(View.GONE);
            }

            eventDescription.setText(Html.fromHtml(b.getString("EVENT_ITEM_DESCRIPTION")));
            eStartTime = b.getString("EVENT_ITEM_START_TIME");
            eEndTime = b.getString("EVENT_ITEM_END_TIME");
            String timeString = "";
            if (eStartTime.length() > 0) {
                if (eEndTime.length() > 0) {
                    timeString = eStartTime + " - " + eEndTime;
                } else {
                    timeString = eStartTime;
                }
            }
            if (timeString.length() > 0) {
                eventTime.setText(timeString);
            } else {
                timeLayout.setVisibility(View.GONE);
            }
        }
        return v;

    }

    private void getEventImage(String url) {
        requestQueue.add(imageRequestBuilder(url));

    }

    private ImageRequest imageRequestBuilder(final String url) {
        final Toast loading = Toast.makeText(getActivity(), "Loading data..", Toast.LENGTH_LONG);
        return new ImageRequest
                (url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            try {
                                imageView.setImageBitmap(bitmap);
                            } catch (Error e) {
                                e.printStackTrace();
                            }
                            loading.cancel();
                        }
                    }
                }, 1920, 1080, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Fetching image failed.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onViewAllImagesClick();
    }
}
