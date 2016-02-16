package in.vmc.mcubeconnect.utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import in.vmc.mcubeconnect.R;

/**
 * Created by mukesh on 17/11/15.
 */
public class OTPDialogFragment extends DialogFragment {
    static String DialogboxTitle;
    EditText txtname;
    Button btnDone, btnCancel;

    //---empty constructor required
    public OTPDialogFragment() {

    }

    //---set the title of the dialog window
    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public void setOPT(String otp) {
        if (txtname != null) {
            txtname.setText(otp);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.otp_dialog, container);

        //---get the EditText and Button views
        txtname = (EditText) view.findViewById(R.id.txtOTP);
        txtname.setInputType(InputType.TYPE_CLASS_NUMBER);
        btnDone = (Button) view.findViewById(R.id.btnDone);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        //---event handler for the button
        btnDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //---gets the calling activity
                OTPDialogListener activity = (OTPDialogListener) getActivity();
                activity.onFinishInputDialog(txtname.getText().toString());

                //---dismiss the alert
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //---gets the calling activity


                //---dismiss the alert
                dismiss();
            }
        });

        //---show the keyboard automatically
        txtname.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
        getDialog().setTitle(DialogboxTitle);
       /* txtname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnDone.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 4) {
                    txtname.setText(s.toString());
                    btnDone.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });*/

        return view;
    }

    public interface OTPDialogListener {
        void onFinishInputDialog(String inputText);
    }
}