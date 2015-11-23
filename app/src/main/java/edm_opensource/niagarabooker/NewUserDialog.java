package edm_opensource.niagarabooker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by mattiaspernhult on 2015-11-19.
 */
public class NewUserDialog extends DialogFragment {

    private CustomClickListener listener;

    public void setListener(CustomClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.new_user, null);

        builder.setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText username = (EditText) view.findViewById(R.id.username);
                        EditText password = (EditText) view.findViewById(R.id.password);
                        if (listener != null) {
                            Toast.makeText(getActivity(), R.string.credentials_updated, Toast.LENGTH_LONG).show();
                            listener.onClick(true, username.getText().toString().trim(), password.getText().toString().trim());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewUserDialog.this.getDialog().cancel();
                        Toast.makeText(getActivity(), R.string.credentials_not_updated, Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
    }
}
