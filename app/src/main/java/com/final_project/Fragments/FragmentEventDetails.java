package com.final_project.Fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.final_project.R;

public class FragmentEventDetails extends Fragment {
    private TextView eventName, eventLocation, eventTime, eventPrice, eventDescription;
    private String eStartTime, eEndTime, eImageUrls;
    private RequestQueue requestQueue;
    private ConstraintLayout imageLayout;
    private LinearLayout timeLayout, imagesContainer, eventPriceLayout;

    public FragmentEventDetails() {
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    private void getEventImage(String url) {
        requestQueue.add(imageRequestBuilder(url));
    }

    private void addImageToScroll(Bitmap imgBitmap) {
        ImageView img = new ImageView(getActivity());
        img.setLayoutParams(new LinearLayout.LayoutParams(getScreenWidth(), LinearLayout.LayoutParams.MATCH_PARENT));
        img.setImageBitmap(imgBitmap);
        imagesContainer.addView(img);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_details, container, false);

        if (getActivity() != null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        eventName = v.findViewById(R.id.event_detail_name);
        eventLocation = v.findViewById(R.id.event_detail_location);
        eventTime = v.findViewById(R.id.event_detail_time);
        timeLayout = v.findViewById(R.id.event_detail_time_layout);
        eventPrice = v.findViewById(R.id.event_detail_price);
        eventPriceLayout = v.findViewById(R.id.event_detail_price_layout);
        eventDescription = v.findViewById(R.id.event_detail_description);
        imageLayout = v.findViewById(R.id.event_details_image_layout);
        imagesContainer = v.findViewById(R.id.event_detail_images_container);

        if (getArguments() != null) {
            try {
                Bundle b = getArguments();
                eventName.setText(b.getString("EVENT_ITEM_NAME"));
                eventLocation.setText(Html.fromHtml(b.getString("EVENT_ITEM_PLACE_NAME")));

                String priceString = b.getString("EVENT_ITEM_PRICE");
                if (priceString != null && priceString.length() > 0) {
                    eventPrice.setText(Html.fromHtml(b.getString("EVENT_ITEM_PRICE")));
                } else {
                    eventPriceLayout.setVisibility(View.GONE);
                }

                eImageUrls = b.getString("EVENT_ITEM_IMAGE_URLS");
                if (eImageUrls != null && eImageUrls.length() > 10) {
                    // TODO any comma separated values in the URL WILL brake this. It is not a problem for a project with these requirements(single place filter etc.). But if developed further it might become one.
                    String allUrls = eImageUrls.substring(1, eImageUrls.length() - 1);
                    String[] urls = allUrls.split(",");
                    for (String url : urls) {
                        Log.d(" FETCHING ULR", url);
                        getEventImage(url);
                    }

                } else {
                    imageLayout.setVisibility(View.GONE);
                }

                eventDescription.setText(Html.fromHtml(b.getString("EVENT_ITEM_DESCRIPTION")));
                eventDescription.setMovementMethod(LinkMovementMethod.getInstance()); // Make links open in a browser
                eStartTime = b.getString("EVENT_ITEM_START_TIME");
                eEndTime = b.getString("EVENT_ITEM_END_TIME");
                String timeString = "";
                if (eStartTime.length() > 0) {
                    if (eEndTime != null && eEndTime.length() > 0) {
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
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }

        }
        return v;

    }

    private ImageRequest imageRequestBuilder(final String url) {
        return new ImageRequest
                (url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null) {
                            try {
                                addImageToScroll(bitmap);
                            } catch (Error e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, 1920, 1080, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Fetching image failed", url);
                    }
                });
    }
}
