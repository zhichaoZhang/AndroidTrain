package com.zzc.androidtrain.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zzc.androidtrain.R;

import java.util.List;

/**
 * Created by zczhang on 16/8/6.
 */
public class GridViewAdapter extends BaseAdapter {
    private static final String TAG = "GridViewAdapter";
    private List<String> mContent;
    private Context mContext;

    public GridViewAdapter(Context context) {
        this.mContext = context;
    }

    public void setContent(List<String> mContent) {
        this.mContent = mContent;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContent == null ? 0 : mContent.size();
    }

    @Override
    public Object getItem(int position) {
        return mContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.e(TAG, "getView0: " + "position = " + position + "; convertView = " + convertView );
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.viewholder_gride_view, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.tvText.setText(mContent.get(position));
        viewHolder.simpleDraweeView.setImageURI("http://near.m1img.com/op_upload/115/14703053173.png");

        return convertView;
    }

    private static class ViewHolder {
        TextView tvText;
        SimpleDraweeView simpleDraweeView;

        public ViewHolder(View view) {
            tvText = (TextView) view.findViewById(R.id.tv_text);
            simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_avatar);
        }
    }
}
