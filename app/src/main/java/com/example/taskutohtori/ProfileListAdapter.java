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

/**
 * Custom ArrayAdapter for profileList in ProfilesActivity.
 * @author Jarno Tiainen
 * @version 1.0
 */

public class ProfileListAdapter extends ArrayAdapter<Profile> {

    private Context context;
    private int resource;

    /**
     * A constructor for ProfileListAdapter
     * @param context the context used to create the adapter
     * @param resource the layout used by the adapter
     * @param objects the class of objects the adapter gets its data from
     */
    public ProfileListAdapter(@NonNull Context context, int resource, @NonNull List<Profile> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    /**
     * Used to generate views for a ListView
     * @param position position in a ListView the view is being created for
     * @param convertView recycled view from the list
     * @param parent the listView the views are being created for
     * @return view to be shown on the list
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).name;
        int age = getItem(position).age;
        String sex = getItem(position).getSexString();
        boolean active = getItem(position).active;

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView nameText = convertView.findViewById(R.id.nameText);
        TextView ageText = convertView.findViewById(R.id.ageText);
        TextView sexText = convertView.findViewById(R.id.sexText);
        ImageView activeIV = convertView.findViewById(R.id.activeIV);

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
