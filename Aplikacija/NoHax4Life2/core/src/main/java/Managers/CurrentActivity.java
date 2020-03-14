package Managers;

import androidx.appcompat.app.AppCompatActivity;

public class CurrentActivity {
    /**
     * Staticka varijabla zadnje prosljedene aktivnosti.
     */
    private static AppCompatActivity activity;

    /**
     * Getter funkcija za dohvacanje zadnje prosljedene aktivnosti.
     * @return AppCompatActivity
     */
    public static AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * Setter funkcija za postavljanje zadnje prosljedene aktivnosti.
     * @param aktivnost AppCompatActivity
     */
    public static void setActivity(AppCompatActivity aktivnost) {
        activity = aktivnost;
    }
}
