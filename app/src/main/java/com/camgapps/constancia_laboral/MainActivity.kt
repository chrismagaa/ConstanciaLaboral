package com.camgapps.constancia_laboral

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.camgapps.constancia_laboral.ui.ValorarDialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.io.File

class MainActivity : AppCompatActivity(),ValorarDialogFragment.ValorarDialogInterface {

    companion object {
        const val nombreCarpeta = "MisConstancias"
        const val SETTING_PROYECTO = "settings"
        const val CONTEO_PARA_VALORAR_APP = "conteo_valorar"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if(!existeCarpeta()){
            CreateFolder()
        }

        val adRequest = AdRequest.Builder().build()
        val adViewEditar = this.findViewById<AdView>(R.id.adViewEditar)
        adViewEditar.loadAd(adRequest)

        valorarAppStatus()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        var tvAppName = navView.getHeaderView(0).findViewById<TextView>(R.id.textViewAppName)
        tvAppName.text =  "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME}"
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_plantillas, R.id.nav_constancias, R.id.nav_feedback), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun valorarAppStatus() {
        val shareP = this.getSharedPreferences(SETTING_PROYECTO, Context.MODE_PRIVATE)
        var conteoValorarApp = shareP.getInt(CONTEO_PARA_VALORAR_APP, 0)

        if(conteoValorarApp==3){
            var dialogValorar = ValorarDialogFragment()
            dialogValorar.show(supportFragmentManager, "ValorarDialogFragment")
            with(shareP!!.edit()){
                putInt(CONTEO_PARA_VALORAR_APP, conteoValorarApp+1)
                commit()
            }
        }else if(conteoValorarApp<3){
            with(shareP!!.edit()){
                putInt(CONTEO_PARA_VALORAR_APP, conteoValorarApp+1)
                commit()
            }
        }
    }

    fun existeCarpeta(): Boolean{
        val file = File(this.filesDir.toString() +"/"+nombreCarpeta)
        if(!file.exists()){
            return false
        }
        return true
    }

    private fun CreateFolder() {
        val filer = File(this.filesDir.toString() +"/"+ nombreCarpeta)
        if(!filer.exists()){
            filer.mkdirs()
            if(filer.isDirectory){
                val shareP = this?.getSharedPreferences("settings_file", Context.MODE_PRIVATE)
                with(shareP!!.edit()){
                    putString("dir_pdfs", filer.absolutePath)
                    commit()
                }
            }else{
                Log.d("MainActivity", "Error al crear carpeta")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun valorarApp() {
        var url = "https://play.google.com/store/apps/details?id=com.camgapps.constancia_laboral"
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

    override fun valorar(dialog: DialogFragment) {
        valorarApp()
        dialog.dismiss()
    }

    override fun mandarRetro(dialog: DialogFragment) {
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_feedback)
        dialog.dismiss()
    }
}