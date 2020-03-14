package Managers;

import androidx.appcompat.app.AppCompatActivity;

public class CurrentActivity {
    /**
     * Statička varijabla zadnje prosljeđene aktivnosti.
     */
    private static AppCompatActivity activity;

    /**
     * Getter funkcija za dohvaćanje zadnje prosljeđene aktivnosti.
     * @return AppCompatActivity
     */
    public static AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * Setter funkcija za postavljanje zadnje prosljeđene aktivnosti.
     * @param aktivnost AppCompatActivity
     */
    public static void setActivity(AppCompatActivity aktivnost) {
        activity = aktivnost;
    }
}
