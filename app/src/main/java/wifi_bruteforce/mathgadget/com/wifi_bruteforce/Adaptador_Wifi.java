package wifi_bruteforce.mathgadget.com.wifi_bruteforce;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class Adaptador_Wifi extends RecyclerView.Adapter<Adaptador_Wifi.ViewHolderDatos> implements View.OnClickListener{

    ArrayList<Wifi_List_Model> Lista;
    private View.OnClickListener Listener;

    public Adaptador_Wifi(ArrayList<Wifi_List_Model> lista) {
        Lista = lista;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_wifi_list,null,false);
        vista.setOnClickListener(this);
        return new ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.asignarDatos(Lista.get(position).getsNombre(), Lista.get(position).getsMac(), Lista.get(position).getsLevel(),
                Lista.get(position).getsSoporte());
    }

    @Override
    public int getItemCount() {
        return Lista.size();
    }

    public void setOnClickListener(View.OnClickListener Listener){
        this.Listener = Listener;
    }

    @Override
    public void onClick(View view) {
        if(Listener != null){
            Listener.onClick(view);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView Nombre,Mac,Canal,Vendor;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            Nombre = itemView.findViewById(R.id.Tv_Nombre);
            Canal = itemView.findViewById(R.id.Tv_Canal);
            Mac = itemView.findViewById(R.id.Tv_Mac);
            Vendor = itemView.findViewById(R.id.Tv_Vendor);
        }

        public void asignarDatos(String Nombre, String Mac, String Canal, String Vendor) {
            this.Nombre.setText(Nombre);
            this.Mac.setText("Mac: "+Mac);
            this.Canal.setText("Nivel: "+Canal+" dbm");
            this.Vendor.setText("Tipo: "+Vendor);
        }
    }
    public void addAll(ArrayList<Wifi_List_Model> Lista){
        this.Lista.addAll(Lista);
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear(){
        Lista.clear();
        notifyDataSetChanged();
    }

}
