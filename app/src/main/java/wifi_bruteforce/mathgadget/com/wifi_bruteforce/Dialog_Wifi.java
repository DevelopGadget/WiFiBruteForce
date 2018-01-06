package wifi_bruteforce.mathgadget.com.wifi_bruteforce;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
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
    private boolean Archivo = false, Oculto = false;
    private WifiController Wifi = Inicio.getWifi();

    @SuppressLint("ValidFragment")
    public Dialog_Wifi(String titulo) {
        Titulo = titulo;
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
        Rb_Alfa.performClick();
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

    private void Fix() {
        if (Rb_Alfa.isChecked()) {
            Validar(EditCaracteres.getText().toString());
        } else if (Rb_Num.isChecked()) {
            Validar(EditNumerico.getText().toString());
        } else {
            if (Archivo) {
                    Alert_Dialog("Información", "Con Este Metodo Suele Ser Mas Exitoso Ya Que Se " +
                            "Creo Previo Un WordList Con Las Posibles Mas Acertadas Contraseñas",
                            R.mipmap.information, getActivity(), false, true, false, 0);
            } else {
                Alert_Dialog("ERROR!", "Verifique El Archivo Ingresado", R.mipmap.ic_launcher, getActivity(), false, false, true, 0);
            }
        }
    }

    private void Validar(String Numero) {
        try {
            if (Integer.parseInt(Numero) >= 8 && Integer.parseInt(Numero) <= 15) {
                Alert_Dialog("Información", "Con Este Metodo Prueba Diferentes Contraseñas " +
                        "Puede Que Afecte El Rendimiento ¿Quiere Continuar?", R.mipmap.information, getActivity(), true, false, false, Integer.parseInt(Numero));
                dismiss();
            } else {
                if (Numero.equals("") || Numero == null) {
                    Alert_Dialog("ERROR!", "Revise Los Datos Ingresados", R.mipmap.ic_launcher, getActivity(), false, false, true, 0);
                } else {
                    Alert_Dialog("ERROR!", "Debe Ingresar Un Valor Menor " +
                            "O Igual A 8 Y Menor  O Igual a 15", R.mipmap.ic_launcher, getActivity(), false, false, true, 0);
                }
            }
        } catch (Exception e) {
            Alert_Dialog("ERROR!", "Revise Los Datos Ingresados", R.mipmap.ic_launcher, getActivity(), false, false, true, 0);
        }
    }

    private void Alert_Dialog(String Titulo, String Mensaje, int Icono, final Context context, boolean Icono_Negative, final boolean Archivo, final boolean Error, final int Tamaño) {
        AlertDialog.Builder b = new AlertDialog.Builder(context)
                .setTitle(Titulo)
                .setIcon(Icono)
                .setCancelable(false)
                .setMessage(Mensaje)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!Error){
                            dismiss();
                            new Passwords(Tv_Opcion.getText().toString(), Archivo, Tamaño, Wifi).show(((AppCompatActivity) context).getFragmentManager(), "");
                        }
                    }
                });
        if (Icono_Negative) {
            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }
        b.show();
    }


}
