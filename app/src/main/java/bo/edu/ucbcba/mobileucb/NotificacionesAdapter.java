package bo.edu.ucbcba.mobileucb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Richard on 10/4/15.
 */
public class NotificacionesAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<cls_notificaciones> items;

    public NotificacionesAdapter(Activity activity, ArrayList<cls_notificaciones> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).get_id_notificacion();
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi=contentView;

        if(contentView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_item_layout, null);
        }

        cls_notificaciones item = items.get(position);

        ImageView image = (ImageView) vi.findViewById(R.id.imagen);
        int imageResource = activity.getResources().getIdentifier(item.get_rutaImagen(), null, activity.getPackageName());
        image.setImageDrawable(activity.getResources().getDrawable(imageResource));

        TextView nombre = (TextView) vi.findViewById(R.id.nombre);
        nombre.setText(item.get_aviso());

        TextView tipo = (TextView) vi.findViewById(R.id.tipo);
        tipo.setText(item.get_desc_tipo_notificacion());

        return vi;
    }
}