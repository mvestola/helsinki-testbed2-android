package fi.testbed2.android.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.roboguice.annotations.RoboGuice;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.Task;
import fi.testbed2.android.ui.dialog.AlertDialogBuilder;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.SettingsService;

@SuppressLint("NonConstantResourceId")
@EActivity(R.layout.main)
@OptionsMenu(R.menu.main_menu)
@RoboGuice
public class MainActivity extends AbstractActivity implements AlertDialogBuilder.LocationPermissionDialogCloseHandler {

    public static final int PARSING_SUB_ACTIVITY = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.button_rain_temperature)
    View buttonRainTemperature;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.button_wind)
    View buttonWind;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.button_humidity)
    View buttonHumidity;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.button_air_pressure)
    View buttonAirPressure;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.button_dew_point)
    View buttonDewPoint;

    @Inject
    SettingsService settingsService;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NonConstantResourceId")
    @Click({R.id.button_rain_temperature, R.id.button_wind, R.id.button_humidity, R.id.button_air_pressure, R.id.button_dew_point})
    public void onMapTypeButtonClicked(View clickedView) {

        switch (clickedView.getId()) {
            case R.id.button_rain_temperature:
                settingsService.setMapType("radar");
                break;
            case R.id.button_wind:
                settingsService.setMapType("wind");
                break;
            case R.id.button_humidity:
                settingsService.setMapType("relativehumidity");
                break;
            case R.id.button_air_pressure:
                settingsService.setMapType("pressure");
                break;
            case R.id.button_dew_point:
                settingsService.setMapType("dewpoint");
                break;
        }

        checkPermissionsAndStartMainParsingActivity();
    }

    private void startMainParsingActivity() {
        Logger.debug("startMainParsingActivity");
        ParsingActivity_.intent(this).startForResult(PARSING_SUB_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PARSING_SUB_ACTIVITY) {
            handleParsingResult(resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleParsingResult(int resultCode, Intent data) {

        switch (resultCode) {
            case Activity.RESULT_CANCELED:
                showShortMessage(this.getString(R.string.notice_cancelled));
                break;
            case MainApplication.RESULT_ERROR:
                String errorMsg = this.getString(R.string.error_message_detailed,
                        data.getStringExtra(Task.ERROR_MSG_CODE));
                showErrorDialog(errorMsg);
                break;
        }

    }

    private void checkPermissionsAndStartMainParsingActivity() {
        String locationProvider = settingsService.getLocationProvider();
        if (settingsService.showUserLocation() && (LocationService.LOCATION_PROVIDER_NETWORK.equals(locationProvider) || LocationService.LOCATION_PROVIDER_GPS.equals(locationProvider))) {
            checkAndRequestLocationPermission(locationProvider);
        } else {
            Logger.debug("No need to request location permission");
            startMainParsingActivity();
        }
    }

    private String getPermissionToRequest(String locationProvider) {
        if (LocationService.LOCATION_PROVIDER_GPS.equals(locationProvider)) {
            return Manifest.permission.ACCESS_FINE_LOCATION;
        } else {
            return Manifest.permission.ACCESS_COARSE_LOCATION;
        }
    }

    private void checkAndRequestLocationPermission(String locationProvider) {
        String permission = getPermissionToRequest(locationProvider);
        Logger.debug("Checking location permission");
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                dialogBuilder.getLocationPermissionDialog(permission, this).show();
            } else {
                requestLocationPermission(permission);
            }
        } else {
            Logger.debug("Location permission already granted");
            startMainParsingActivity();
        }
    }

    private void requestLocationPermission(String permission) {
        Logger.debug("Requesting location permission: " + permission);
        ActivityCompat.requestPermissions(this, new String[]{permission}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onCloseLocationPermissionDialog(String permission) {
        requestLocationPermission(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logger.debug("User granted location permission");
            }
            startMainParsingActivity();
        }
    }

    @Override
    public void onRefreshFromMenuSelected() {
        checkPermissionsAndStartMainParsingActivity();
    }

}