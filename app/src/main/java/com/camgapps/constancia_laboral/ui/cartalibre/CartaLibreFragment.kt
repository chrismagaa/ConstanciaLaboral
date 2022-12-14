package com.camgapps.constancia_laboral.ui.cartalibre

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.camgapps.constancia_laboral.PlantillasConscantcias.PlantillaLibre
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentCartaLibreBinding
import com.camgapps.constancia_laboral.text
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class CartaLibreFragment : Fragment() {

    lateinit var setListener: DatePickerDialog.OnDateSetListener

    val df = SimpleDateFormat("d/MMMM/yyyy", Locale.getDefault())
    private var day by Delegates.notNull<Int>()
    private var month by Delegates.notNull<Int>()
    private var year by Delegates.notNull<Int>()

    private var _binding: FragmentCartaLibreBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartaLibreBinding.inflate(inflater, container, false)
        val view = binding.root

        val calendarHoy = Calendar.getInstance()
        day = calendarHoy.get(Calendar.DAY_OF_MONTH)
        month = calendarHoy.get(Calendar.MONTH)
        year = calendarHoy.get(Calendar.YEAR)

        initListeners()

        setHasOptionsMenu(true)

        return view
    }


    private fun initListeners() {

        binding.ivFecha.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(), android.R.style.Theme_Material_Dialog,
                setListener, year, month, day
            )
            datePicker.show()
        }

        setListener = DatePickerDialog.OnDateSetListener { _, yea, mon, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(yea, mon, dayOfMonth)
            binding.inputFecha.editText!!.setText(df.format(cal.time))
        }


        binding.btnNext.setOnClickListener {
            crearPreview()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit_plantilla, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_check -> {
                crearPreview()
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        const val EXTRA_PATH_PDF_VIEW = "extra_path_pdf_view"
    }
    private fun crearPreview() {
        val outputDir = requireContext().cacheDir

        var outputFile: File? = null

        outputFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf", outputDir)
        outputFile.deleteOnExit()

        val plantilla = PlantillaLibre(
            binding.inputCiudad.text(),
            binding.inputFecha.text(),
            binding.inputCuerpo.text(),
            binding.inputFirma.text()
        )
        plantilla.createPdf(outputFile)

        val bundle = bundleOf("path" to outputFile!!.absolutePath)
        findNavController().navigate(
           R.id.action_cartaLibreFragment_to_pdfPreviewFragment,
            bundle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}