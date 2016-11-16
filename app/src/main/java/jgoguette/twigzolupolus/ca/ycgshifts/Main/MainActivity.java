package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jgoguette.twigzolupolus.ca.ycgshifts.Login.LoginActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.FeedsFragment;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.MessageFragment;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadMessageFragment;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTradeFragment;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ScheduleFragment;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendBlastFragment;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SettingsFragment;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Services.NotificationService;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

public class MainActivity extends AppCompatActivity
        implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int MY_READ_EXTERNAL_STORAGE = 1;
    private Uri mFileUri = null;
    private Uri outputFileUri;

    public Toolbar toolbar;
    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;

    // User Profile
    public User user;

    MainPresenter presenter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User();

        presenter = new MainPresenterImpl(this);
        presenter.getProfilePic();
        presenter.getUserProfile();
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        } else {
            drawer.openDrawer(GravityCompat.START);
            Toast.makeText(this, getString(R.string.Exit_Text), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_logout) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();

            navigateToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpNav();
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggle.syncState();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Check if this Activity was launched by clicking on an upload notification
        if (intent.hasExtra(NotificationService.NOTIFICATION_FOUND)) {
            openMessage(intent);
        }
    }

    private void openMessage(Intent intent) {
        if(intent.hasExtra(NotificationService.NOTIFICATION_FOUND)) {
            if (intent.getBooleanExtra("Grouped", true)) {
                if (intent.hasExtra("Keys")) {
                    ArrayList<String> keys = intent.getStringArrayListExtra("Keys");
                    if (!keys.isEmpty()) {
                        presenter.removeNotifications(keys);
                    }
                }

                navigateToMessages();
            } else {
                Message message = (Message) intent.getSerializableExtra("Message");
                presenter.removeNotification(message.getKey());
                if (message.getType().equals(message
                        .convertTypeToString(Message.Type.SHIFT_SWAP_NOTIF))) {

                    navigateToReadShiftTradeMessage(message);
                } else {
                    navigateToReadMessage(message);
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNav() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };

        drawer.addDrawerListener(toggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navigateToHome();
        } else if (id == R.id.nav_inbox) {
            navigateToMessages();
        } else if (id == R.id.nav_send_blast) {
            navigateToSendBlast();
        } else if (id == R.id.nav_schedule) {
            navigateToSchedule();
        } else if (id == R.id.nav_manage) {
            navigateToSettings();
        }  else if (id == R.id.nav_logout) {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProfileInformation() {
        TextView nav_name = (TextView) findViewById(R.id.nav_name);
        nav_name.setText(user.getName());

        TextView nav_email = (TextView)findViewById(R.id.nav_email);
        nav_email.setText(user.getEmail());

        ImageView profilePic = (ImageView)findViewById(R.id.imageView);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assume thisActivity is the current activity
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    openImageIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_READ_EXTERNAL_STORAGE);
                }
            }
        });
    }

    @Override
    public void setProfilePic(Uri uri) {
        ImageView profilePic = (ImageView) findViewById(R.id.imageView);
        Picasso.with(MainActivity.this)
                .load(uri)
                .fit()
                .into(profilePic);
    }

    @Override public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.fetch_user_progressDialog_message));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    @Override public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onProfileSuccessfullyRetrieved(User user) {
        setUser(user);
        setProfileInformation();
        navigateToHome();

        onNewIntent(getIntent());
        startAlarm();
    }

    @Override
    public void navigateToHome() {
        navigationView.setCheckedItem(R.id.nav_home);
        // Initial Fragment
        FeedsFragment fragment = new FeedsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).commit();
    }

    @Override
    public void navigateToMessages() {
        MessageFragment fragment = new MessageFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).commit();
    }

    @Override
    public void navigateToReadMessage(Message message) {
        ReadMessageFragment fragment =
                ReadMessageFragment.newInstance(message);
        FragmentManager manager =
                getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).addToBackStack(null).commit();
    }

    @Override
    public void navigateToReadShiftTradeMessage(Message message) {
        ReadShiftTradeFragment fragment =
                ReadShiftTradeFragment.newInstance(message);
        FragmentManager manager =
                getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).addToBackStack(null).commit();
    }

    @Override
    public void navigateToSendBlast() {
        SendBlastFragment fragment = new SendBlastFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).commit();
    }

    @Override
    public void navigateToSchedule() {
        ScheduleFragment fragment = new ScheduleFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).commit();
    }

    @Override
    public void navigateToSettings() {
        SettingsFragment fragment = new SettingsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).commit();
    }

    @Override
    public void logOut() {
        cancelAlarm();
        cancelNotifications();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        navigateToLogin();
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void startAlarm() {
        presenter.startNotificationService();
    }

    public void cancelAlarm() {
        presenter.stopNotificationService();
    }

    public void cancelNotifications() {
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context
                .NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }

    private void openImageIntent() {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "photos" + File.separator);
        root.mkdirs();

        final String fname = user.getFirebaseId() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Choose file storage location, must be listed in res/xml/file_paths.xml
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
        File file = new File(dir, user.getFirebaseId() + ".jpg");
        try {
            // Create directory if it does not exist.
            if (!dir.exists()) {
                dir.mkdir();
            }
            boolean created = file.createNewFile();
            Log.d(TAG, "file.createNewFile:" + file.getAbsolutePath() + ":" + created);
        } catch (IOException e) {
            Log.e(TAG, "file.createNewFile" + file.getAbsolutePath() + ":FAILED", e);
        }

        // Create content:// URI for file, required since Android N
        // See: https://developer.android.com/reference/android/support/v4/content/FileProvider.html
        mFileUri = FileProvider.getUriForFile(this,
                "com.ycgshifts.fileprovider", file);

        Intent pickIntent =
                new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(pickIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            final boolean isCamera;
            if (data == null) {
                isCamera = true;
            } else {
                final String action = data.getAction();
                if (action == null) {
                    isCamera = false;
                } else {
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }

            Uri selectedImageUri;
            if (isCamera) {
                selectedImageUri = outputFileUri;
            } else {
                selectedImageUri = data == null ? null : data.getData();
            }

            if (selectedImageUri != null) {
                setProfilePic(selectedImageUri);
                uploadProfilePic(selectedImageUri);
            } else {
                Log.w(TAG, "File URI is null");
            }
        }
    }

    private void uploadProfilePic(Uri fileUri) {
        presenter.uploadProfilePic(mFileUri,fileUri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
