package com.hafiz.ftp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Hafiz on 9/24/2015.
 */
public class ListCustomAdapter extends ArrayAdapter<String>{
    private String[] values;
    private final Context context;
    private ArrayList<String> selected = new ArrayList();


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
        textView.setText(values[position]);

        CheckBox cb = (CheckBox) rowView.findViewById(R.id.listCheckbox);
        cb.setTag(position);
        Log.d("values", values.toString());


        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Integer position = (Integer) cb.getTag();
                if (cb.isChecked()) {
                    Log.d("values", values.toString());
                    Log.d("position", position.toString());
                    setSelected(values[position]);
                } else {
                    removeSelected(values[position]);
                }

                Toast.makeText(context, values[position], Toast.LENGTH_LONG).show();
            }
        });

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
