package com.hafiz.ftp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hafiz on 9/24/2015.
 */
public class ListCustomAdapter extends ArrayAdapter<String>{
    private String[] values;
    private final Context context;
    private ArrayList<String> selected = new ArrayList();
    private RemoteTabFragment fragment = null;
    private Boolean removeDelete = false;
    private Boolean[] deleteables;
    private HashMap fileMap;


    public ListCustomAdapter(Context context, String[] values, RemoteTabFragment fragment,  Boolean[] deleteables, HashMap fileMap) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.fragment = fragment;
        this.deleteables = deleteables;
        this.fileMap = fileMap;
    }

    public ListCustomAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.removeDelete = true;
    }

    public String[] getValues(){
        return this.values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_row, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.listText);
        textView.setText(values[position]);

        Button btn = (Button) rowView.findViewById(R.id.delete_button);
        if(fragment == null || !this.deleteables[position]){
            btn.setVisibility(View.INVISIBLE);
        } else {
            btn.setTag(position);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    btn.getTag(position);
                    String filename = values[position];
                    Log.d("filename", filename);

                    Bundle args = new Bundle();
                    args.putString("filename", filename);
                    fragment.startFtpTask("delete", fragment.addressBar.getText().toString(), args);
                }
            });
        }
//        CheckBox cb = (CheckBox) rowView.findViewById(R.id.listCheckbox);
//        cb.setTag(position);
//        Log.d("values", values.toString());
//
//
//        cb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                Integer position = (Integer) cb.getTag();
//                if (cb.isChecked()) {
//                    Log.d("values", values.toString());
//                    Log.d("position", position.toString());
//                    setSelected(values[position]);
//                } else {
//                    removeSelected(values[position]);
//                }
//
//                Toast.makeText(context, values[position], Toast.LENGTH_LONG).show();
//            }
//        });

        return rowView;
    }

    public void setSelected(String value) {
        selected.add(value);

    }

    public void removeSelected(String value) {
        selected.remove(selected.indexOf(value));
    }

    public ArrayList<String> getSelected() {
        return selected;
    }
}
