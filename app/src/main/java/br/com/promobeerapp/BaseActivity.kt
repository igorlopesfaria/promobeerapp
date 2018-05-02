package br.com.promobeerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.promobeerapp.model.User
import br.com.promobeerapp.model.dao.Preffs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

open class BaseActivity: AppCompatActivity() {

    lateinit var drawerToggle: ActionBarDrawerToggle

    var preffs: Preffs?  = null
    val TAG = "LocationProvider"
    var mFusedLocationClient: FusedLocationProviderClient? = null
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    var mLastLocation: Location? = null
    lateinit var user: User



    fun changeFragment(fragmentToChange: Fragment, hasBackPage: Boolean) {
        if (!hasBackPage) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container_fragment, fragmentToChange, fragmentToChange.javaClass.name)
                    .commit()
        } else {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container_fragment, fragmentToChange, fragmentToChange.javaClass.name)
                    .addToBackStack(null)
                    .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener(object : FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true) // show back button
                } else {
                    //show hamburger
                    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                }
            }
        })
    }


    fun verifyPermission():Boolean {

        var verified = false;
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
            verified=true
        }

        return verified
    }

    protected fun init(){
        preffs = Preffs(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     *
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLastLocation = task.result

                    } else {
                        Log.w(TAG, "getLastLocation:exception", task.exception)
                    }
                }
    }


    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    View.OnClickListener {
                        // Request permission
                    })

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }
    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {

        Toast.makeText(this, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null)
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        })
            }
        }
    }


}