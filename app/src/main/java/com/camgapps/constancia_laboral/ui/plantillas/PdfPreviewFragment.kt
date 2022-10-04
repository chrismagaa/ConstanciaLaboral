package com.camgapps.constancia_laboral.ui.plantillas

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.camgapps.constancia_laboral.AdMobManager
import com.camgapps.constancia_laboral.MainActivity
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentPdfPreviewBinding
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import java.io.*
import kotlin.jvm.Throws


class PdfPreviewFragment : Fragment(), OnDrawListener, GuardarConstanciaDialogFragment.GuardarConstanciaDialogInterface {

    private var path: String? = null

    var nombreGuardarCarta = ""

    lateinit var adMobManager: AdMobManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            path = it.getString("path")
        }
    }

    private var _binding: FragmentPdfPreviewBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPdfPreviewBinding.inflate(inflater, container, false)
        val view  = binding.root
        setHasOptionsMenu(true)
        loadFile()
            return view
    }

    private fun loadFile() {
        binding.pdfPreView.fromFile(File(path)).
        spacing(0)
            .enableAnnotationRendering(false)
            .scrollHandle(null)
            .onDraw(this)
            .load()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_pdf_preview, menu)
    }
    private var absolutePath: String = ""
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_save -> {
                 absolutePath = requireContext().filesDir
                    .toString() + "/"+MainActivity.nombreCarpeta+"/"
                showDialogGuardar(absolutePath)
            }
            else ->{

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialogGuardar(absolutePath: String) {
        val dialog = GuardarConstanciaDialogFragment(absolutePath, File(path).nameWithoutExtension)
        dialog.show(childFragmentManager, "GuardarCartaDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLayerDrawn(
        canvas: Canvas?,
        pageWidth: Float,
        pageHeight: Float,
        displayedPage: Int
    ) {
        var paint = Paint()
        canvas?.drawLine(0F, 0F, pageWidth, 0F, paint)
        canvas?.drawLine(0F, pageHeight, pageWidth, pageHeight, paint)
        canvas?.drawLine(0F, 0F, 0F, pageHeight, paint)
        canvas?.drawLine(pageWidth, 0F, pageWidth, pageHeight, paint)
    }


    private fun guardarCarta() {
        Toast.makeText(requireContext(), nombreGuardarCarta, Toast.LENGTH_SHORT).show()
        val file = copy(
            File(path),
            File(
                requireContext().filesDir
                    .toString() + "/"+MainActivity.nombreCarpeta+"/" +nombreGuardarCarta+".pdf"
            )
        )
        Toast.makeText(requireContext(), file.path, Toast.LENGTH_SHORT).show()
        val bundle = bundleOf("path" to file.path)
        findNavController().navigate(R.id.action_pdfPreviewFragment_to_miConstanciaViewFragment, bundle)

    }


    @Throws(IOException::class)
    fun copy(src: File?, dst: File?): File {
        val inStream: InputStream = FileInputStream(src)
        try {
            val out: OutputStream = FileOutputStream(dst)
            try {
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (inStream.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
            } finally {
                out.close()
            }
        } finally {
            inStream.close()
        }
        return dst!!
    }

    override fun gurdarConstanciaResponsDialog(dialog: DialogFragment, namePdf: String) {
        nombreGuardarCarta = namePdf

        AdMobManager.showAd(requireActivity()){
            guardarCarta()
            Toast.makeText(requireContext(), "Carta guardada", Toast.LENGTH_SHORT).show()
        }
    }

}