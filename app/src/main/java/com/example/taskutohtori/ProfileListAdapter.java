package com.example.taskutohtori;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class ProfileListAdapter extends ArrayAdapter<Profile> {

    private Context context;
    private int resource;

    public ProfileListAdapter(@NonNull Context context, int resource, @NonNull List<Profile> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).name;
        int age = getItem(position).age;
        boolean male = getItem(position).male;
        String sex = getItem(position).getSexString();
        boolean active = getItem(position).active;

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
        TextView ageText = (TextView) convertView.findViewById(R.id.ageText);
        TextView sexText = (TextView) convertView.findViewById(R.id.sexText);
        ImageView activeIV = (ImageView) convertView.findViewById(R.id.activeIV);

        nameText.setText(name);
        ageText.setText("Ikä:  " + age);
        sexText.setText("Sukupuoli:  " + sex);

        int activeID;
        if(active) {
            activeID = R.drawable.active_true;
        } else {
            activeID = R.drawable.active_false;
        }

        activeIV.setImageResource(activeID);

        return convertView;
    }
}
