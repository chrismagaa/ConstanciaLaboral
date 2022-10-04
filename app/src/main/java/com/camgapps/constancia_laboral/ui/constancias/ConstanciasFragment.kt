package com.camgapps.constancia_laboral.ui.constancias

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camgapps.constancia_laboral.MainActivity
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentConstanciasBinding
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ConstanciasFragment : Fragment() {

    lateinit var adapter: ConstanciasAdapter

    var pdfs = ArrayList<File>()
    private var pdfNames = ArrayList<String>()
    private var pdfDates =  ArrayList<String>()
    private var misConstancias = ArrayList<MiConstancia>()

    private var _binding: FragmentConstanciasBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentConstanciasBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = ConstanciasAdapter(ArrayList(), requireContext(), findNavController())
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        setNameAndDates()
        changeDataAdapter()
        checarVacio()

        return view
    }

    private fun checarVacio() {
        if(misConstancias.isEmpty()){
            binding.imageViewNoFiles.visibility = View.VISIBLE
            binding.textViewNoFiles.visibility = View.VISIBLE
        }else{
            binding.imageViewNoFiles.visibility = View.GONE
            binding.textViewNoFiles.visibility = View.GONE
        }
    }




    private fun changeDataAdapter() {
        if(existeCarpeta()){
            adapter.setData(misConstancias)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setNameAndDates() {
        misConstancias.clear()
        if(existeCarpeta()){
            pdfs = getPdfFiles()
            pdfs.forEach { file ->
                pdfNames.add(file.name.replace(".pdf", ""))
                val df = SimpleDateFormat("dd-MM-yyyy")
                pdfDates.add(df.format(Date(file.lastModified())).toString())
                misConstancias.add(MiConstancia(file.name.replace(".pdf", ""),df.format(Date(file.lastModified())).toString(), file))
            }
        }
    }

    fun getPdfFiles(): ArrayList<File>{
        val arrayList = ArrayList<File>()
        val file = File(context?.filesDir.toString() +"/"+MainActivity.nombreCarpeta)
        file.listFiles()?.forEach { file ->
            if(file.name.endsWith(".pdf")){
                arrayList.add(file)
            }
        }
        return arrayList
    }


    fun existeCarpeta(): Boolean{
        val file = File(context?.filesDir.toString() +"/"+MainActivity.nombreCarpeta)
        if(!file.exists()){
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}