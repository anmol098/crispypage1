package c.crispypage.activity;

/**
 * Created by Anmol Pratap Singh on 07-01-2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import c.crispypage.R;

public class MyArrayAdapter extends ArrayAdapter < MyDataModel > {

    List < MyDataModel > modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public MyArrayAdapter(Context context, List < MyDataModel > objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public MyDataModel getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        MyDataModel item = getItem(position);

        vh.textViewId.setText(item.getId());
        vh.textViewName.setText(item.getName());
        vh.textViewTime.setText(item.getTime());
        vh.textViewEmail.setText(item.getEmail());
        vh.textViewDelivery.setText(item.getDelivery());



        return vh.rootView;
    }



    private static class ViewHolder {
        public final RelativeLayout rootView;

        public final TextView textViewId;
        public final TextView textViewName;
        public final TextView textViewEmail;
        public final TextView textViewTime;
        public final TextView textViewDelivery;


        private ViewHolder(RelativeLayout rootView, TextView textViewName, TextView textViewId, TextView textViewDelivery, TextView textViewTime,TextView textViewEmail) {
            this.rootView = rootView;
            this.textViewId = textViewId;
            this.textViewName = textViewName;
            this.textViewDelivery = textViewDelivery;
            this.textViewEmail = textViewEmail;
            this.textViewTime = textViewTime;

        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView textViewId = (TextView) rootView.findViewById(R.id.textViewId);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewName);
            TextView textViewEmail = (TextView) rootView.findViewById(R.id.email);
            TextView textViewTime = (TextView) rootView.findViewById(R.id.time);
            TextView textViewDelivery = (TextView) rootView.findViewById(R.id.delivery);

            return new ViewHolder(rootView, textViewName, textViewId, textViewTime,textViewDelivery,textViewEmail);
        }
    }
}