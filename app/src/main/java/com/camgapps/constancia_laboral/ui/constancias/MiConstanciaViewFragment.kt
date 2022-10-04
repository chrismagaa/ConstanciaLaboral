package com.camgapps.constancia_laboral.ui.constancias

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.print.PrintManager
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.camgapps.constancia_laboral.*
import com.camgapps.constancia_laboral.databinding.FragmentMiConstanciaViewBinding
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import java.io.File


class MiConstanciaViewFragment : Fragment(), OnDrawListener {

    private var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            path = it.getString("path")
        }
    }


    private var _binding: FragmentMiConstanciaViewBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMiConstanciaViewBinding.inflate(inflater, container, false)
        val view = binding.root

        setHasOptionsMenu(true)

        viewPdf()


        return view
    }

    private fun viewPdf() {
        binding.miPdfView.fromFile(File(path))
                .enableSwipe(true)
                .enableAnnotationRendering(true)
                .scrollHandle(DefaultScrollHandle(requireContext()))
                .onDraw(this)
                .load()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_mi_constancia, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_share -> {
                AdMobManager.showAd(requireActivity()){
                    path?.let { requireContext().shareFilePDF(it) }
                }
                return true
            }
            R.id.action_print -> {
                doPrint()
                return true
            }

            else -> return false
        }

        return super.onOptionsItemSelected(item)

    }

    private fun doPrint() {
        this.also { context ->
            // Get a PrintManager instance
            val printManager = requireContext().getSystemService(Context.PRINT_SERVICE) as PrintManager
            // Set job name, which will be displayed in the print queue
            val jobName = "${context.getString(R.string.app_name)} Documento"
            // Start a print job, passing in a PrintDocumentAdapter implementation
            // to handle the generation of a print document
            printManager.print(jobName, MyPrintDocumentAdapter(File(path)), null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLayerDrawn(
            canvas: Canvas,
            pageWidth: Float,
            pageHeight: Float,
            displayedPage: Int
    ) {
        var paint = Paint()
        canvas.drawLine(0F, 0F, pageWidth, 0F, paint)
        canvas.drawLine(0F, pageHeight, pageWidth, pageHeight, paint)
        canvas.drawLine(0F, 0F, 0F, pageHeight, paint)
        canvas.drawLine(pageWidth, 0F, pageWidth, pageHeight, paint)
    }


}