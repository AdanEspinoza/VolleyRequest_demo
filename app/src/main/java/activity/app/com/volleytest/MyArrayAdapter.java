package activity.app.com.volleytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class MyArrayAdapter extends ArrayAdapter<Contacto> {
    Context context;
    ArrayList<Contacto> contactos;

    public MyArrayAdapter(Context context,int resource,ArrayList<Contacto> strings) {
        super(context, resource, strings);
        this.context = context;
        contactos = strings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row, parent, false);

        TextView tvid = (TextView) row.findViewById(R.id.tvid);
        TextView tvname = (TextView) row.findViewById(R.id.tvname);
        TextView tvlast = (TextView) row.findViewById(R.id.tvlast);
        TextView tvgender = (TextView) row.findViewById(R.id.tvgender);
        TextView tvweight = (TextView) row.findViewById(R.id.tvweight);
        TextView tvheight = (TextView) row.findViewById(R.id.tvheight);
        TextView tvbirthday = (TextView) row.findViewById(R.id.tvbirthday);

        tvid.setText(contactos.get(position).getId());
        tvname.setText(contactos.get(position).getFirstName());
        tvlast.setText(contactos.get(position).getLastName());
        tvgender.setText(contactos.get(position).getGender());
        tvweight.setText(contactos.get(position).getWeight());
        tvheight.setText(contactos.get(position).getHeight());
        tvbirthday.setText(contactos.get(position).getBirthday());

        return row;
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                notifyDataSetChanged();
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();
//                constraint = constraint.toString().toLowerCase(Locale.getDefault());
//                contactos.clear();
//                if(constraint.length() == 0){
//
//                    contactos.addAll(arrayList);
//
//                } else{
//
//                    for(Contacto c: arrayList){
//
//                        if(c.getName().toLowerCase(Locale.getDefault()).contains(constraint)
//                                ||bean.getLastName().toLowerCase(Locale.getDefault()).contains(constraint)) {
//                            contactBeans.add(bean);
//
//                        }
//                    }
//
//                }
//
//                results.count = contactBeans.size();
//                results.values = contactBeans;
//                return results;
//            }
//        };
//    }
}
