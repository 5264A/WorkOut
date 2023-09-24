package com.example.womenssafety

import android.Manifest
import android.Manifest.permission.*
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    val permission= arrayOf(
        Manifest.permission.CAMERA,
        READ_CONTACTS,
        ACCESS_FINE_LOCATION,
        )
    val permissioncode=78
    //lateinit var binding:ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askforpermission()
        //binding=ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        val bottombar=findViewById<BottomNavigationView>(R.id.bottom_bar)
        if (isAllPermissionsGranted()) {
            if (isLocationEnabled(this)) {
                setUpLocationListener()
            } else {
                showGPSNotEnabledDialog(this)
            }
        } else {
            askforpermission()
        }

        bottombar.setOnItemSelectedListener {menuitem->
            when (menuitem.itemId) {
                R.id.nav_home -> {
                 inflateHomeFragement()
                }
                R.id.nav_guard -> {
                inflateGuardFragment()
                }
                R.id.nav_profile -> {
                 inflateProfileFragement()
                }
                R.id.nav_dashboard -> {
                   inflateMapsFragement()
                }
            }
            true

        }

        bottombar.selectedItemId=R.id.nav_home
        val db = Firebase.firestore
        val currentUser = FirebaseAuth.getInstance().currentUser
        val name = currentUser?.displayName.toString()
        val mail = currentUser?.email.toString()
        val phoneNumber = currentUser?.phoneNumber.toString()
        val imageUrl = currentUser?.photoUrl.toString()

        val user = hashMapOf(
            "firstName" to name,
            "MiddleNmae" to mail,
            "lastName" to phoneNumber,

        )

        db.collection("users").document(mail).set(user).addOnSuccessListener {

        }.addOnFailureListener {

        }

    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = com.google.android.gms.location.LocationRequest().setInterval(2000)
           


        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        Log.d("Location89", "onLocationResult: latitude ${location.latitude}")
                        Log.d("Location89", "onLocationResult: longitude ${location.longitude}")


                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val mail = currentUser?.email.toString()

                        val db = Firebase.firestore

                        val locationData = mutableMapOf<String,Any>(
                            "lat" to location.latitude.toString(),
                            "long" to location.longitude.toString(),
                        )


                        db.collection("users").document(mail).update(locationData)
                            .addOnSuccessListener {

                            }.addOnFailureListener {

                            }


                    }
                }
            },
            Looper.myLooper()
        )
    }


    fun isLocationEnabled(context: MainActivity): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Function to show the "enable GPS" Dialog box
     */
    fun showGPSNotEnabledDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Enable GPS")
            .setMessage("required_for_this_app")
            .setCancelable(false)
            .setPositiveButton("enable_now") { _, _ ->
                context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    private fun isAllPermissionsGranted(): Boolean {
        for (item in permission) {
            if (ContextCompat
                    .checkSelfPermission(
                        this,
                        item
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun askforpermission() {
       ActivityCompat.requestPermissions(this,permission,permissioncode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==permissioncode){
            if(allPermissionGranted()){
                //setUpLocationListener()
            }
            else{
                Toast.makeText(this,"Give Permission For Camera",Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun opencamera() {
        val intent= Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun allPermissionGranted(): Boolean {
        for (item in permission) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    item
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }

        return true
    }


    private fun inflateHomeFragement() {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,HomeFragment())
        transaction.commit()

    }
    private fun inflateMapsFragement() {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,MapsFragment())
        transaction.commit()

    }


    private fun inflateGuardFragment() {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,GuardFragment.newInstance())
        transaction.commit()

    }
    private fun inflateDashboradFragement() {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, HomeFragment())
        transaction.commit()
    }

    private fun inflateProfileFragement() {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,ProfileFragment.newInstance())
        transaction.commit()
    }






}

