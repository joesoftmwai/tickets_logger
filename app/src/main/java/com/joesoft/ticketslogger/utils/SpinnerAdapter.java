package com.joesoft.ticketslogger.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.joesoft.ticketslogger.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    Context mContext;
    String[] mSpinnerText, mSpinnerImageStrings;
    int[] mSpinnerImageIntegers;
    String selectedText = "";

    public SpinnerAdapter(@NonNull Context context, String[] spinnerText, String[] spinnerImageStrings) {
        super(context, R.layout.spinner_image_and_text);
        mContext = context;
        mSpinnerText = spinnerText;
        mSpinnerImageStrings = spinnerImageStrings;
        selectedText = spinnerText[0];
    }

    public SpinnerAdapter(@NonNull Context context, String[] spinnerText, int[] spinnerImageIntegers) {
        super(context, R.layout.spinner_image_and_text);
        mContext = context;
        mSpinnerText = spinnerText;
        mSpinnerImageIntegers = spinnerImageIntegers;
        selectedText = spinnerText[0];
    }

    @Override
    public int getCount() {
        return mSpinnerText.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_image_and_text, parent, false);

            viewHolder.mSpinnerImage = convertView.findViewById(R.id.spinner_img_image);
            viewHolder.mSpinnerText = convertView.findViewById(R.id.spinner_txt_text);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_camera_24dp)
                .error(R.drawable.ic_camera_24dp)
                .centerInside()
                .override(50, 50);

        if (mSpinnerImageIntegers != null) {
            Glide.with(mContext)
                    .setDefaultRequestOptions(options)
                    .load(mSpinnerImageIntegers[position])
                    .into(viewHolder.mSpinnerImage);
        } else if(mSpinnerImageStrings != null) {
            Glide.with(mContext)
                    .setDefaultRequestOptions(options)
                    .load(mSpinnerImageStrings[position])
                    .into(viewHolder.mSpinnerImage);
        }



        viewHolder.mSpinnerText.setText(mSpinnerText[position]);

        return convertView;
    }

    @Override
    public int getPosition(@Nullable String item) {
        for(int i = 0; i < mSpinnerImageStrings.length; i++){
            if(mSpinnerText[i].equals(item)){
                return i;
            }
        }
        return super.getPosition(item);
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mSpinnerText[position];
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView mSpinnerImage;
        TextView mSpinnerText;
    }
}

