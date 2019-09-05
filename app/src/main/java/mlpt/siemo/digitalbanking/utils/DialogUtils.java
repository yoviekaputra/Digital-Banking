package mlpt.siemo.digitalbanking.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import mlpt.siemo.digitalbanking.R;

/***
 * yovi.putra
 */
public class DialogUtils {

    public static void showAlertDialog(Context context, String content) {
        showPositiveDialog(context, context.getString(R.string.warning), content, null);
    }

    public static void showPositiveDialog(Context context, String title, String content, Dialog.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton(context.getString(R.string.ok), onClickListener);
        show(context,builder);
    }

    public static void showConfirmationDialog(Context context, String content, DialogInterface.OnClickListener positiveClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.confirmation));
        builder.setMessage(content);
        builder.setPositiveButton(context.getString(R.string.yes), positiveClick);
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        show(context,builder);
    }

    public static void showFailedRequestDialog(Context context, String content, DialogInterface.OnClickListener positiveClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle((R.string.warning));
        builder.setMessage(content);
        builder.setPositiveButton(context.getString(R.string.ok), positiveClick);
        show(context,builder);
    }

    public static Dialog create(Context context, @LayoutRes int layout) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
            w.setGravity(Gravity.CENTER);
            w.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }
        return dialog;
    }

    /***
     * @param context
     * @param builder
     */
    private static void show(Context context, AlertDialog.Builder builder){
        if(builder != null){
            Activity activity = (Activity) context;
            if(!activity.isFinishing()){
                builder.show();
            }
        }
    }
}
