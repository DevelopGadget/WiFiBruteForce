package wifi_bruteforce.mathgadget.com.wifi_bruteforce;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Gamer on 03/01/2018.
 */

@SuppressLint("ValidFragment")
public class Passwords extends DialogFragment {

    private View view;
    private TextView Tv_Pass;
    private WifiController Wifi;
    char[] Elem;
    private boolean Leer_Archivo;

    @SuppressLint("ValidFragment")
    public Passwords(String Elem, boolean Leer_Archivo) {
        this.Wifi = Inicio.getWifi();
        this.Elem = Elem.toCharArray();
        this.Leer_Archivo = Leer_Archivo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.passwords, null);
        Tv_Pass = view.findViewById(R.id.Tv_Pass);
        builder.setView(view)
                .setTitle("Wifi Brute-Force: Probando Contraseñas")
                .setIcon(R.drawable.wifi);
        return builder.create();
    }

    private void WordList(char[] elem, String act, int Tamaño) {
        Wifi.Connect("00986307503763", Wifi.WifiInfo.get(Wifi.Position).getsNombre());
        Wifi.Reconnect();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Wifi.isOnline()) {
                    Log.i("Online", "Conectado");
                    Toast.makeText(getActivity(), "Conectado", Toast.LENGTH_SHORT).show();
                    Wifi.Remove();
                    try {
                        dismiss();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }, 10000);
       /* if(Tamaño == 0){

        }else{
            for (int i = 0; i < elem.length; i++) {
                WordList(elem, act + elem[i], Tamaño - 1);
            }
        }
        */
    }

}
