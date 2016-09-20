package appsshoppy.com.whosnext.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by akshayacharya on 14/09/16.
 */
public class Util
{
    public static void showAlert(Context context, String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String getTime(int hourOfDay, int min){
        String AM_PM ;
        String time;
        if(hourOfDay < 12) {
            AM_PM = hourOfDay+":"+min+" AM";

        } else {
            AM_PM = (hourOfDay-12)+":"+min+" PM";
        }

        return  AM_PM;
    }
}
