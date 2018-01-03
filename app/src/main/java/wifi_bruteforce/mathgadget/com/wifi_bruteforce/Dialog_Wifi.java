package wifi_bruteforce.mathgadget.com.wifi_bruteforce;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;

import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static com.nbsp.materialfilepicker.ui.FilePickerActivity.RESULT_FILE_PATH;

@SuppressLint("ValidFragment")
public class Dialog_Wifi extends DialogFragment implements View.OnClickListener {

    private EditText EditCaracteres, EditNumerico;
    private TextView Tv_Opcion;
    private ImageButton Btn_Archivo;
    private String Titulo, Path = "", Array = "";
    private View view;
    private RadioButton Rb_Alfa, Rb_Num, Rb_Archvio;
    private WifiController Wifi;
    private boolean Archivo = false;

    @SuppressLint("ValidFragment")
    public Dialog_Wifi(String titulo) {
        Titulo = titulo;
        this.Wifi = Inicio.getWifi();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.wifi_seleccion, null);
        Instancias();
        builder.setView(view)
                .setTitle("Wifi Brute-Force: " + Titulo)
                .setIcon(R.drawable.wifi);
        return builder.create();
    }

    private void Instancias() {
        EditCaracteres = (EditText) view.findViewById(R.id.Edit_Alfa);
        EditNumerico = (EditText) view.findViewById(R.id.Edit_Num);
        Tv_Opcion = (TextView) view.findViewById(R.id.Tv_Opcion);
        Rb_Alfa = (RadioButton) view.findViewById(R.id.Rb_Alfa);
        Rb_Num = (RadioButton) view.findViewById(R.id.Rb_Num);
        Rb_Archvio = (RadioButton) view.findViewById(R.id.Rb_Archivo);
        Btn_Archivo = (ImageButton) view.findViewById(R.id.Btn_Find);
        view.findViewById(R.id.Btn_Cancel).setOnClickListener(this);
        view.findViewById(R.id.Btn_Fix).setOnClickListener(this);
        Rb_Alfa.setOnClickListener(this);
        Rb_Num.setOnClickListener(this);
        Rb_Archvio.setOnClickListener(this);
        Btn_Archivo.setOnClickListener(this);
        Rb_Alfa.setChecked(true);
        Btn_Archivo.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Rb_Alfa:
                vista(true, false, false);
                EditCaracteres.requestFocus();
                Tv_Opcion.setText(Alfa());
                Array = (String) Tv_Opcion.getText();
                break;
            case R.id.Rb_Archivo:
                vista(false, false, true);
                Tv_Opcion.setText("Cargue el archivo txt");
                Array = (String) Tv_Opcion.getText();
                EditCaracteres.clearFocus();
                EditNumerico.clearFocus();
                break;
            case R.id.Rb_Num:
                vista(false, true, false);
                EditNumerico.requestFocus();
                Tv_Opcion.setText("0123456789");
                Array = (String) Tv_Opcion.getText();
                break;
            case R.id.Btn_Find:
                File();
                break;
            case R.id.Btn_Cancel:
                this.dismiss();
                break;
            case R.id.Btn_Fix:
                Fix();
                //WordList("01".toCharArray(), "", 2);
                Archivo = false;
                break;
        }
    }

    private void vista(boolean Alfa, boolean Num, boolean Archivo) {
        EditCaracteres.setEnabled(Alfa);
        EditCaracteres.setText(null);
        EditNumerico.setEnabled(Num);
        EditNumerico.setText(null);
        Btn_Archivo.setEnabled(Archivo);
        Archivo = false;
    }

    private String Alfa() {
        String Alfa = "";
        for (int i = 0; i < 26; i++) {
            Alfa += (char) ('A' + i);
        }
        Alfa += Alfa.toLowerCase() + "0123456789";
        return Alfa;
    }

    private void File() {
        new MaterialFilePicker()
                .withFragment(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.txt$")) // Filtering files and directories by file name using regexp
                .withHiddenFiles(true) // Show hidden files and folders
                .withTitle("Seleccione un archivo txt")
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Path = data.getStringExtra(RESULT_FILE_PATH);
            Tv_Opcion.setText(Path);
            Archivo = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void Fix() {
        if (Integer.parseInt(Wifi.WifiInfo.get(Wifi.Position).getsLevel()) >= -95) {
            if ((!EditCaracteres.getText().toString().equals("") && EditCaracteres.getText().toString() != null)) {
                Validar(EditCaracteres.getText().toString());
            } else if (!EditNumerico.getText().toString().equals("") && EditNumerico.getText().toString() != null) {
                Validar(EditNumerico.getText().toString());
            } else {

            }
        } else {
            Inicio.Alert_Dialog("ERROR!", "La Intensidad De La Señal Es Baja", R.mipmap.ic_launcher, getActivity(), false);
            Toast.makeText(getActivity(), "Refresque La Lista", Toast.LENGTH_LONG).show();
            dismiss();
        }
    }

    private boolean Validar(String Numero) {
        try {
            if (Integer.parseInt(Numero) >= 8 && Integer.parseInt(Numero) <= 15) {
                if (Inicio.Alert_Dialog("Información", "Con Este Metodo Prueba Diferentes Contraseñas " +
                        "Puede Que Afecte El Rendimiento ¿Quiere Continuar?", R.mipmap.information, getActivity(), true)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (Numero.equals("") || Numero == null) {
                    Inicio.Alert_Dialog("ERROR!", "Revise Los Datos Ingresados", R.mipmap.ic_launcher, getActivity(), false);
                } else {
                    Inicio.Alert_Dialog("ERROR!", "Debe Ingresar Un Valor Menor O Igual A 8 Y Menor  O Igual a 15", R.mipmap.ic_launcher, getActivity(), false);
                }
                return false;
            }
        } catch (Exception e) {
            Inicio.Alert_Dialog("ERROR!", "Revise Los Datos Ingresados", R.mipmap.ic_launcher, getActivity(), false);
            return false;
        }
    }
}
