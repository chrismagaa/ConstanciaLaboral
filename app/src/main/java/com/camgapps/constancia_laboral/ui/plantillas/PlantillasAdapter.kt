package com.camgapps.constancia_laboral.ui.plantillas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentItemAdapterBinding
import java.util.ArrayList

class PlantillasAdapter(
    val onClick: (Plantilla, Int) -> Unit
): RecyclerView.Adapter<PlantillasAdapter.ViewHolder>() {

    var plantillas: List<Plantilla> = ArrayList()
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = FragmentItemAdapterBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.fragment_item_adapter, parent, false
                )
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = plantillas[position]
        holder.binding.imageViewPlantilla.setImageResource(item.img)
        holder.binding.textViewPlantilla.text = item.name

        if(item.isActive){
            holder.binding.ivCandado.visibility = View.GONE
        }

        holder.binding.cardViewPlantilla.setOnClickListener {
            onClick(item, position)
        }

    }

    override fun getItemCount(): Int = plantillas.size

    fun setData(list: List<Plantilla>) {
        this.plantillas = list
        notifyDataSetChanged()
    }


}