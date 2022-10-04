package com.camgapps.constancia_laboral.ui.constancias

import android.content.Context
import android.content.Intent
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.camgapps.constancia_laboral.MyPrintDocumentAdapter
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentItermConstanciaBinding
import java.io.File
import java.lang.Exception

class ConstanciasAdapter(
    var constancias: ArrayList<MiConstancia>,
    var ctx: Context,
    var nav: NavController
): RecyclerView.Adapter<ConstanciasAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = FragmentItermConstanciaBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_iterm_constancia, parent, false)

        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!constancias.isNullOrEmpty()){
            val constancia = constancias[position]

            holder.binding.textViewNombrePdf.text = constancia.name
            holder.binding.textViewFecha.text = constancia.date

            val popupMenu = PopupMenu(ctx, holder.binding.imageViewOptions)
            popupMenu.inflate(R.menu.popup_menu_pdf)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menu_share -> {

                        val shareintent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ctx, "com.camgapps.constancia_laboral"+".fileprovider", constancia.file))
                            type = "application/pdf"
                        }
                        ContextCompat.startActivity(ctx, shareintent, null)

                    }
                    R.id.menu_imprimir -> {
                        doPrint(constancia.file)
                    }
                    R.id.menu_delete -> {
                        constancia.file.delete()
                        constancias.remove(constancia)
                        notifyItemRemoved(position)
                    }

                }
                true
            }

            holder.binding.cardViewMiCarta.setOnClickListener {
                val bundle = bundleOf("path" to constancia.file.absolutePath)
                nav.navigate(R.id.action_nav_constancias_to_miConstanciaViewFragment, bundle)
            }

            holder.binding.imageViewOptions.setOnClickListener {
                try {
                    val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                    popup.isAccessible = true
                    val menu = popup.get(popupMenu)
                    menu.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(menu, true)
                }catch (e: Exception){
                    e.printStackTrace()
                }finally {
                    popupMenu.show()
                }

            }

        }
    }

    fun setData(constanciaList: ArrayList<MiConstancia>){
        constancias = constanciaList
        notifyDataSetChanged()
    }

    private fun doPrint(file: File) {
        ctx.also { context ->
            // Get a PrintManager instance
            val printManager = context!!.getSystemService(Context.PRINT_SERVICE) as PrintManager
            // Set job name, which will be displayed in the print queue
            val jobName = "${context.getString(R.string.app_name)} Documento"
            // Start a print job, passing in a PrintDocumentAdapter implementation
            // to handle the generation of a print document
            printManager.print(jobName, MyPrintDocumentAdapter(file), null)
        }
    }

    override fun getItemCount(): Int = constancias.size
}