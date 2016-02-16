package in.vmc.mcubeconnect.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vmc.mcubeconnect.R;
import in.vmc.mcubeconnect.model.UserData;
import in.vmc.mcubeconnect.utils.DatePickerFragment;
import in.vmc.mcubeconnect.utils.JSONParser;
import in.vmc.mcubeconnect.utils.TAG;
import in.vmc.mcubeconnect.utils.Utils;

public class UpdateProfile extends AppCompatActivity implements TAG {
    @InjectView(R.id.tvdob)
    TextView _tvDob;
    @InjectView(R.id.tvusername)
    TextView _tvUsername;
    @InjectView(R.id.tvuseremail)
    TextView _tvUseremail;
    @InjectView(R.id.tvname)
    TextView _tvName;
    @InjectView(R.id.tvemail)
    TextView _tvEmail;
    @InjectView(R.id.tvPassword)
    TextView _tvPassword;
    @InjectView(R.id.profile_image)
    CircleImageView _userImage;

    @InjectView(R.id.etname)
    EditText _etName;

    @InjectView(R.id.etPassword)
    EditText _etPassword;
    @InjectView(R.id.etemail)
    EditText _etEmail;

    @InjectView(R.id.myRadioGroup)
    RadioGroup _gender;

    @InjectView(R.id.ivName)
    ImageView _ivName;
    @InjectView(R.id.ivEmail)
    ImageView _ivEmail;

    @InjectView(R.id.btshow)
    Button _btshow;

    @InjectView(R.id.btnUpdate)
    Button _btUpdate;
    @InjectView(R.id.datepic)
    ImageView _dateTimePick;
    @InjectView(R.id.mroot)
    RelativeLayout mroot;

    @InjectView(R.id.male)
    RadioButton _rbMale;
    @InjectView(R.id.female)
    RadioButton _rbFemale;


    String dob = "9-3-2016";
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            dob = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(year);
            //   Toast.makeText(UpdateProfile.this, DOB, Toast.LENGTH_LONG).show();
            _tvDob.setText(dob);

        }
    };
    private String username,
            email,
            authkey, profileImage,
            password;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    private String gender;
    private ProgressDialog progressDialog;
    private UserData mUserdata;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        authkey = Utils.getFromPrefs(UpdateProfile.this, AUTHKEY, "n");
        username = Utils.getFromPrefs(UpdateProfile.this, USERNAME, "n");
        email = Utils.getFromPrefs(UpdateProfile.this, EMAIL, "n");
        // profileImage = Utils.getFromPrefs(UpdateProfile.this, PRO_IMAGE, "n");
        dob = "";

        GetUpdate();


        _userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showFileChooser();


            }
        });


        _tvUsername.setText(username);
        _tvName.setText(username);

        _tvEmail.setText(email);
        _tvUseremail.setText(email);

        _dateTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        _btshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (_etPassword.getVisibility() == View.VISIBLE) {
                    password = _etPassword.getText().toString();
                    _etPassword.setVisibility(View.GONE);
                    _btshow.setText("Show");
                    //  _tvPassword.setText(password);
                    _tvPassword.setVisibility(View.VISIBLE);
                } else if (_tvPassword.getVisibility() == View.VISIBLE) {
                    _tvPassword.setVisibility(View.GONE);
                    _etPassword.setText(password);
                    _etPassword.setVisibility(View.VISIBLE);
                    _btshow.setText("Hide");
                }

            }
        });


        _ivName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (_etName.getVisibility() == View.VISIBLE) {
                    username = _etName.getText().toString();
                    _etName.setVisibility(View.GONE);
                    _tvName.setText(username);
                    _tvName.setVisibility(View.VISIBLE);
                } else if (_tvName.getVisibility() == View.VISIBLE) {
                    _tvName.setVisibility(View.GONE);
                    _etName.setText(username);
                    _etName.setVisibility(View.VISIBLE);
                }

            }
        });
        _ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (_etEmail.getVisibility() == View.VISIBLE) {
                    email = _etEmail.getText().toString();
                    _etEmail.setVisibility(View.GONE);
                    _tvEmail.setText(email);
                    _tvEmail.setVisibility(View.VISIBLE);
                } else if (_tvEmail.getVisibility() == View.VISIBLE) {
                    _tvEmail.setVisibility(View.GONE);
                    _etEmail.setText(email);
                    _etEmail.setVisibility(View.VISIBLE);
                }

            }
        });

        _btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_etEmail.getVisibility() == View.VISIBLE) {
                    email = _etEmail.getText().toString();
                } else {
                    email = _tvEmail.getText().toString();
                }
                if (_etName.getVisibility() == View.VISIBLE) {
                    username = _etName.getText().toString();
                } else {
                    username = _tvName.getText().toString();
                }


                if (_etPassword.getVisibility() == View.VISIBLE) {
                    password = _etPassword.getText().toString();
                } else {
                    /// password = _tvPassword.getText().toString();
                }
                if (_rbMale.isChecked()) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }


                DoUpdate();

            }
        });


    }

    public void UpdateView(UserData userData) {
        if (userData != null) {
            username = userData.getUsername();
            email = userData.getEmail();
            password = userData.getPassword();
            dob = userData.getDob();
            _tvDob.setText(userData.getDob());
            profileImage = userData.getImage();
            if (!userData.getImage().equals("")) {
                _userImage.setImageBitmap(decodeBase64(userData.getImage()));
            }

            if (userData.getGender().equals("Male")) {
                _rbMale.setChecked(true);
            } else {
                _rbFemale.setChecked(true);
            }

        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap encoded = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                encoded.compress(Bitmap.CompressFormat.PNG, 100, out);
                bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                _userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    public void DoUpdate() {

        if (Utils.onlineStatus2(UpdateProfile.this)) {
            new DoUpdateProfile().execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DoUpdate();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(UpdateProfile.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    public void GetUpdate() {

        if (Utils.onlineStatus2(UpdateProfile.this)) {
            new GetProfileDetail().execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetUpdate();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(UpdateProfile.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    class DoUpdateProfile extends AsyncTask<Void, Void, String> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        String image1 = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UpdateProfile.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating Profile...");
            progressDialog.show();
            if (bitmap != null) {
                image1 = getStringImage(bitmap);
            }
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.UpdateUserProfile(UPDATE_PROFILE, authkey, username, email, dob, gender, password, image1);
                Log.d("UPDATE", response.toString());


                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);

                }

            } catch (Exception e) {

            }
            return code;
        }

        @Override
        protected void onPostExecute(String data) {
            Log.d("UPDATE", username + " " + email + " " + dob + " " + gender + " " + password + " " + image1);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (data != null) {

                if (data.equals("400")) {
                    Toast.makeText(UpdateProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    if (image1.length() > 10) {
                        Utils.saveToPrefs(UpdateProfile.this, IMAGE_1, image1);
                    }
                } else {
                    Toast.makeText(UpdateProfile.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                }
            }


        }


    }

    class GetProfileDetail extends AsyncTask<Void, Void, UserData> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        String image1 = "";
        UserData userData;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UpdateProfile.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating Profile...");
            progressDialog.show();
            if (bitmap != null) {
                image1 = getStringImage(bitmap);
            }
            super.onPreExecute();
        }


        @Override
        protected UserData doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.GetProfileDetail(GET_PROFILE, authkey);
                Log.d("UPDATE1", response.toString());
                userData = new UserData();


                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);

                }

                if (response.has(DATA)) {
                    JSONObject data = response.getJSONObject(DATA);


                    if (data.has(EMAIL)) {
                        userData.setEmail(data.getString(EMAIL));

                    }

                    if (data.has(USERNAME)) {
                        userData.setUsername(data.getString(USERNAME));

                    }

                    if (data.has(PASSWORD)) {
                        userData.setPassword(data.getString(PASSWORD));

                    }

                    if (data.has(DOB)) {
                        userData.setDob(data.getString(DOB));

                    }
                    if (data.has(IMAGE_1)) {
                        userData.setImage(data.getString(IMAGE_1));

                    }
                    if (data.has(GENDER)) {
                        userData.setGender(data.getString(GENDER));
                    }


                }
            } catch (Exception e) {

            }
            return userData;
        }

        @Override
        protected void onPostExecute(UserData data) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (data != null) {

                if (code.equals("400")) {
                    UpdateView(data);
                } else {
                    Toast.makeText(UpdateProfile.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                }
            }


        }


    }

}
