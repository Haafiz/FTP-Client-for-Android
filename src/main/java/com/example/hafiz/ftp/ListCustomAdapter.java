package com.example.hafiz.ftp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Hafiz on 9/24/2015.
 */
public class ListCustomAdapter extends ArrayAdapter<String>{
    private String[] values;
    private final Context context;
    private ArrayList<String> selected;


    public ListCustomAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    public String[] getValues(){
        return this.values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_row, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.listText);
        CheckBox cb = (CheckBox) rowView.findViewById(R.id.listCheckbox);
        textView.setText(values[position]);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if(cb.isChecked()) {
                    setSelected(position, values[position]);
                } else {
                    removeSelected(position);
                }

                Toast.makeText(context, values[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }

    public void setSelected(int index, String value) {
        selected.add(index, value);
    }

    public void removeSelected(int index) {
        selected.remove(index);
    }

    public ArrayList<String> getSelected() {
        return selected;
    }
}
