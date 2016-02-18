package in.vmc.mcubeconnect.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.vmc.mcubeconnect.R;
import in.vmc.mcubeconnect.utils.JSONParser;
import in.vmc.mcubeconnect.utils.OTPDialogFragment;
import in.vmc.mcubeconnect.utils.Utils;

public class SignupActivity extends AppCompatActivity implements OTPDialogFragment.OTPDialogListener, in.vmc.mcubeconnect.utils.TAG {
    private static final String TAG = "SignupActivity";
    private static SignupActivity inst;
    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_phone)
    EditText _phone;
    @InjectView(R.id.input_repassword)
    EditText _repassword;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    @InjectView(R.id.checkBox1)
    CheckBox terms;
    OTPDialogFragment otpDialogFragment = new OTPDialogFragment();
    AlertDialog.Builder alertDialog;
    @InjectView(R.id.rootLayout)
    LinearLayout mroot;
    String ResOtp, OTP1;
    String name, email, password, repassword, phone;
    private ProgressDialog progressDialog;
    private String errormsg;

    public static SignupActivity instance() {
        return inst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        alertDialog = new AlertDialog.Builder(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public void updateList(final String smsMessage) {
        //update otp Your one time passwod for Mconnect is: 356958
        OTP1 = smsMessage.substring(39);
        //  String OTP1=smsMessage.split(": ")[0];

        // Log.d("SMS", OTP1+" "+OTP);
        if (otpDialogFragment != null && otpDialogFragment.isVisible()) {
            otpDialogFragment.setOPT(OTP1);
        }


    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            View view = this.getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            // writeToLog("Software Keyboard was not shown");
        }
    }


    public void signup() {
        Log.d(TAG, "Signup");
        hideKeyboard();
        if (!validate()) {
            // onSignupFailed();
            //   Snackbar.make(mroot, errormsg, Snackbar.LENGTH_SHORT).show();
            return;
        } else {

            _signupButton.setEnabled(true);
            progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);


            name = _nameText.getText().toString();
            email = _emailText.getText().toString();
            password = _passwordText.getText().toString();
            repassword = _repassword.getText().toString();
            phone = _phone.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("A one time password will be sent to your number +91" + phone + " go back to change your mobile number or continue to activate .")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            GetOtp();

                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            _signupButton.setEnabled(true);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String repassword = _repassword.getText().toString();
        String phone = _phone.getText().toString();
        Drawable drawable = ContextCompat.getDrawable(SignupActivity.this, R.drawable.error);
        drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
        if (name.isEmpty() || name.length() < 4) {
            _nameText.setError("at least 4 characters", drawable);
            errormsg = "Name must contain at least 4 characters";
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (phone.isEmpty() || phone.length() < 10) {
            _phone.setError("at least 10 digit", drawable);
            errormsg = "Phone must contain at least 10 digit";
            valid = false;
        } else {
            _phone.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address", drawable);
            errormsg = "Enter a valid email address";
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (repassword.isEmpty() || repassword.length() < 4 || repassword.length() > 10) {
            _repassword.setError("between 4 and 10 alphanumeric characters", drawable);
            errormsg = "Password must be between 4 and 10 alphanumeric characters";
            valid = false;
        } else {
            _repassword.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters", drawable);
            errormsg = "Password must be between 4 and 10 alphanumeric characters";
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!repassword.equals(password)) {
            _repassword.setError("password mismatch", drawable);
            errormsg = "Password mismatch";
            valid = false;
        } else {
            _repassword.setError(null);
        }
        if (!terms.isChecked()) {
            Snackbar snack = Snackbar.make(mroot, "Accept terms and condition to proceed", Snackbar.LENGTH_SHORT)
                    .setAction(null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.accent));
            TextView tv = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
            valid = false;
        } else {
            _repassword.setError(null);
        }

        return valid;
    }

    private void showOTPDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        otpDialogFragment = new OTPDialogFragment();
        otpDialogFragment.setCancelable(false);
        otpDialogFragment.setDialogTitle("Enter OTP");
        otpDialogFragment.show(fragmentManager, "Input Dialog");
    }

    @Override
    public void onFinishInputDialog(String inputText) {
        hideKeyboard();
        if (ResOtp.equals(inputText)) {
            Register();


        } else {
            Toast.makeText(this, "Invalid OTP ",
                    Toast.LENGTH_SHORT).show();
            _signupButton.setEnabled(true);

        }

        progressDialog.dismiss();
    }

    public void GetOtp() {
        if (Utils.onlineStatus2(SignupActivity.this)) {
            new GetOtp(_phone.getText().toString()).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetOtp();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(SignupActivity.this, R.color.primary));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }

    public void Register() {
        if (Utils.onlineStatus2(SignupActivity.this)) {
            new Register().execute();

        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Register();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }


    class GetOtp extends AsyncTask<Void, Void, JSONObject> {
        String message = "No Response from server";
        String code = "N";
        String phone = "n";

        String msg;
        JSONObject response = null;

        public GetOtp(String phone) {
            this.phone = phone;
        }

        @Override
        protected void onPreExecute() {
            // showProgress("Login Please Wait.."); progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub


            try {
                response = JSONParser.getOTP(GENERATE_OTP_URL, phone);
                if (response != null) {
                    if (response.has(CODE)) {
                        code = response.getString(CODE);
                    }
                    if (response.has(OTP)) {
                        ResOtp = response.getString(OTP);
                    }
                    if (response.has(MESSAGE)) {
                        msg = response.getString(MESSAGE);
                    }
                }

                //  if(response.c)

            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (data != null) {
                Log.d("TEST", data.toString());
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (code.equals("202")) {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                _signupButton.setEnabled(true);
            } else {
                showOTPDialog();
            }

        }

    }

    class Register extends AsyncTask<Void, Void, JSONObject> {
        String code = "N";
        JSONObject response = null;
        private String msg;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Verfying OTP");
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.Register(REGISTER_URL, phone, name, email, password);
                code = response.getString(CODE);
                msg = response.getString(MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (data != null) {
                Log.d("TEST", data.toString());
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (code.equals("n")) {
                Snackbar.make(mroot, "No Response From Server", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Register();

                            }
                        }).
                        setActionTextColor(ContextCompat.getColor(SignupActivity.this, R.color.primary_dark)).show();
            }
            if (code.equals("202")) {
                Snackbar.make(mroot, msg, Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Register();

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(SignupActivity.this, R.color.accent)).show();
            } else {
                onSignupSuccess();
                _signupButton.setEnabled(true);
            }
        }

    }


}