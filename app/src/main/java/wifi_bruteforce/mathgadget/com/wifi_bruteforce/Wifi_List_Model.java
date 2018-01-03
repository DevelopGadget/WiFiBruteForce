package wifi_bruteforce.mathgadget.com.wifi_bruteforce;

/**
 * Created by Fernan Programer on 23/12/2017.
 */

public class Wifi_List_Model {

    private String sNombre;
    private String sMac;
    private String sLevel;
    private String sSoporte;

    public Wifi_List_Model(String sNombre, String sMac, String sLevel, String sSoporte) {
        this.sNombre = sNombre;
        this.sMac = sMac;
        this.sLevel = sLevel;
        this.sSoporte = sSoporte;
    }

    public String getsNombre() {
        return sNombre;
    }

    public String getsMac() {
        return sMac;
    }

    public String getsLevel() {
        return sLevel;
    }

    public String getsSoporte() {
        return sSoporte;
    }
}
