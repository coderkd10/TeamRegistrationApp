package com.ashish.cop290assign0;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public final class PagerFragment extends Fragment {
    private String name,entryCode;
    private Bitmap image;
    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
//        try {
//            byte[] b = Base64.decode(img, Base64.DEFAULT);
//            fragment.image = BitmapFactory.decodeByteArray(b, 0, b.length);
//        }catch(Exception e){
//            fragment.image = null;
//        }
        return fragment;
    }
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
//            mContent = savedInstanceState.getString(KEY_CONTENT);
//        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        View child = inflater.inflate(
                R.layout.student_info_layout, null);
        layout.addView(child);

        setOnClickListeners(layout);

        return layout;
    }

    private void setOnClickListeners(final LinearLayout layout){

        layout.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryCode = ((TextView)layout.findViewById(R.id.entryCode)).getText().toString();
                String name = ((TextView)layout.findViewById(R.id.name)).getText().toString();

                layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
            }
        });
        layout.findViewById(R.id.edit_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.findViewById(R.id.display_layout).setVisibility(View.GONE);
                layout.findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(KEY_CONTENT, mContent);
    }
    private int dp2Pix(float i){
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
    }
}
