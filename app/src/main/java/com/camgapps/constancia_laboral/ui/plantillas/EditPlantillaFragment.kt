package com.camgapps.constancia_laboral.ui.plantillas

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.camgapps.constancia_laboral.AdMobManager
import com.camgapps.constancia_laboral.PlantillasConscantcias.*
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentEditPlantillaBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.ads.AdRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class EditPlantillaFragment : Fragment() {

    private var _binding: FragmentEditPlantillaBinding? = null
    val binding get() = _binding!!
    var position = 0


    val listSizeLetters  = arrayOf("12", "13", "14","16", "17","18", "19")
    val selectionSizeLetter = 3


    lateinit var setListener: DatePickerDialog.OnDateSetListener
    lateinit var setListenerInicio: DatePickerDialog.OnDateSetListener
    lateinit var setListenerSalida: DatePickerDialog.OnDateSetListener

    lateinit var visibilityEt: VisibilityViews

    private var year by Delegates.notNull<Int>()
    private var month by Delegates.notNull<Int>()
    private var day by Delegates.notNull<Int>()

    val df = SimpleDateFormat("d 'de' MMMM 'del' yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("position")
        }
    }

    override fun onStart() {
        super.onStart()
        AdMobManager.loadAd(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPlantillaBinding.inflate(inflater, container, false)
        val view = binding.root

        setVisiEditTexts()

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)


        binding.textInputFecha.editText!!.setText(df.format(calendar.time))

        initEvents()

        val spinner: Spinner = binding.spinnerSizeLetter
        ArrayAdapter( requireContext(), android.R.layout.simple_spinner_item, listSizeLetters).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.setSelection(selectionSizeLetter)

        setHasOptionsMenu(true)

        return view
    }

    var mUri: Uri? = null
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                mUri = fileUri
                binding.textViewLogotipo.text = fileUri.lastPathSegment
                // imgProfile.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {

            }
        }
    private fun initEvents() {
        binding.buttonLogotipo.setOnClickListener {
            ImagePicker.with(requireActivity())
                .galleryOnly()
                .cropSquare()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }


        binding.imageViewFechaHoy.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Material_Dialog,
                setListener,
                year,
                month,
                day
            )
            datePicker.show()
        }
        setListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val d = Calendar.getInstance()
            d.set(year, month, dayOfMonth)

            binding.textInputFecha.editText!!.setText(df.format(d.time))
        }

        binding.imageViewFechaInicio.setOnClickListener {
            val datePickerInicio = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Material_Dialog,
                setListenerInicio,
                year,
                month,
                day
            )
            datePickerInicio.show()
        }
        setListenerInicio = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val d = Calendar.getInstance()
            d.set(year, month, dayOfMonth)
            binding.textInputFechaInicio.editText!!.setText(df.format(d.time))
        }

        binding.imageViewFechaSalida.setOnClickListener {
            val datePickerSalida = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Material_Dialog,
                setListenerSalida,
                year,
                month,
                day
            )
            datePickerSalida.show()
        }
        setListenerSalida = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val d = Calendar.getInstance()
            d.set(year, month, dayOfMonth)
            binding.textInputFechaSalida.editText!!.setText(df.format(d.time))
        }

        binding.buttonSiguiente.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setVisiEditTexts() {
        when (position) {
            0 -> {
                visibilityEt = PlantillaUno.getVisibility()
                establecerLaVisibilidad()
            }
            1 -> {
                visibilityEt = PlantillaDos.getVisibility()
                establecerLaVisibilidad()
            }
            2 -> {
                visibilityEt = PlantillaTres.getVisibility()
                establecerLaVisibilidad()
            }
            3 -> {
                visibilityEt = PlantillaCuatro.getVisibility()
                establecerLaVisibilidad()
            }
            4 -> {
                visibilityEt = PlantillaCinco.getVisibility()
                establecerLaVisibilidad()
            }
        }
    }

    private fun establecerLaVisibilidad() {
        binding.textInputCiudad.visibility = setVisibility(visibilityEt.vCiudad)
        binding.textInputFecha.visibility = setVisibility(visibilityEt.vFecha)
        binding.textInputFechaInicio.visibility = setVisibility(visibilityEt.vFechaInicio)
        binding.imageViewFechaInicio.visibility = setVisibility(visibilityEt.vFechaInicio)
        binding.textInputFechaSalida.visibility = setVisibility(visibilityEt.vFechaSalida)
        binding.imageViewFechaSalida.visibility = setVisibility(visibilityEt.vFechaSalida)
        binding.textInputPuesto.visibility = setVisibility(visibilityEt.vPuesto)
        binding.radioGroupSexo.visibility = setVisibility(visibilityEt.vSexo)
        binding.textInputEmpresa.visibility = setVisibility(visibilityEt.vNombreEmpresa)
        binding.textInputTrabajador.visibility = setVisibility(visibilityEt.vNombreTrabajador)
        binding.textInputNombreFirma.visibility = setVisibility(visibilityEt.vNombreFirma)
        binding.textInputDni.visibility = setVisibility(visibilityEt.vDni)
        binding.textInputSueldo.visibility = setVisibility(visibilityEt.vSueldo)
        binding.textInputNumeroEmisor.visibility = setVisibility(visibilityEt.vNumeroEmisor)
        binding.buttonLogotipo.visibility = setVisibility(visibilityEt.vLogotipo)
        binding.textViewLogotipo.visibility = setVisibility(visibilityEt.vLogotipo)
    }

    fun setVisibility(isVisible: Boolean): Int {
        if (isVisible) return View.VISIBLE
        return View.GONE
    }

    private fun crearPreview() {
        var lugar = binding.etCiudad.text.toString()
        var fecha = binding.etFecha.text.toString()
        var nombreTrabajador = binding.etTrabajador.text.toString()
        var nombreFirma = binding.etNombreFirma.text.toString()
        var fechaInicio = binding.etFechaInicio.text.toString()
        var fechaSalida = binding.etFechaSalida.text.toString()
        var puesto = binding.etPuesto.text.toString()
        var nombreEmpresa = binding.etEmpresa.text.toString()
        var dni = binding.etDni.text.toString()
        var sueldo = binding.etSueldo.text.toString()
        var numeroEmisor = binding.etNumeroEmisor.text.toString()

        var sexo = 0
        if (binding.radioButtonM.isChecked) {
            sexo = 0
        } else if (binding.radioButtonH.isChecked) {
            sexo = 1
        }

        val sizeLetter: Float = binding.spinnerSizeLetter.selectedItem.toString().toFloat()

        when {
            lugar.trim().isBlank() && visibilityEt.vCiudad -> {
                binding.textInputCiudad.error = getString(R.string.lugar_vacio)
                showDialogDatosVacios()
            }
            fechaInicio.trim().isBlank() && visibilityEt.vFechaInicio -> {
                binding.textInputFechaInicio.error = getString(R.string.fecha_inicio_vacio)
                showDialogDatosVacios()
            }
            fechaSalida.trim().isBlank() && visibilityEt.vFechaSalida -> {
                binding.textInputFechaSalida.error = getString(R.string.fecha_salida_vacio)
                showDialogDatosVacios()
            }
            puesto.trim().isBlank() && visibilityEt.vPuesto -> {
                binding.textInputPuesto.error = getString(R.string.puesto_vacio)
                showDialogDatosVacios()
            }
            nombreTrabajador.trim().isBlank() && visibilityEt.vNombreTrabajador -> {
                binding.textInputTrabajador.error = getString(R.string.nombre_recomendado_vacio)
                showDialogDatosVacios()
            }
            nombreFirma.trim().isBlank() && visibilityEt.vNombreFirma -> {
                binding.textInputNombreFirma.error = getString(R.string.nombre_recomendado_vacio)
                showDialogDatosVacios()
            }
            nombreEmpresa.trim().isBlank() && visibilityEt.vNombreEmpresa -> {
                binding.textInputEmpresa.error = getString(R.string.nombre_recomendado_vacio)
                showDialogDatosVacios()
            }
            dni.trim().isBlank() && visibilityEt.vDni -> {
                binding.textInputDni.error = getString(R.string.dni_vacio)
                showDialogDatosVacios()
            }
            sueldo.trim().isBlank() && visibilityEt.vSueldo -> {
                binding.textInputDni.error = getString(R.string.sueldo_vacio)
                showDialogDatosVacios()
            }
            else -> {
                val calendarIns = Calendar.getInstance().time
                val now = SimpleDateFormat("d 'de' MMMM 'del' yyyy").format(calendarIns)

                val outputDir = requireContext().cacheDir

                var outputFile: File? = null
                when (position) {
                    0 -> {
                        outputFile = File.createTempFile(
                            "$now",
                            ".pdf",
                            outputDir
                        )
                        outputFile.deleteOnExit()
                        val plantillaLaboral = PlantillaUno(
                            requireContext(),
                            lugar,
                            fecha,
                            nombreEmpresa,
                            nombreTrabajador,
                            nombreFirma,
                            fechaInicio,
                            fechaSalida,
                            puesto,
                            sexo,
                            numeroEmisor,
                            sizeLetter
                        )
                        plantillaLaboral.createPdf(outputFile, mUri)
                    }
                    1 -> {
                        outputFile = File.createTempFile(
                            "$now",
                            ".pdf",
                            outputDir
                        )
                        outputFile.deleteOnExit()
                        val plantillaLaboral = PlantillaDos(
                            requireContext(),
                            lugar,
                            fecha,
                            nombreEmpresa,
                            nombreTrabajador,
                            nombreFirma,
                            fechaInicio,
                            fechaSalida,
                            puesto,
                            sexo,
                            dni,
                            numeroEmisor,
                            sizeLetter
                        )
                        plantillaLaboral.createPdf(outputFile, mUri)
                    }
                    2 -> {
                        outputFile = File.createTempFile(
                            "$now",
                            ".pdf",
                            outputDir
                        )
                        outputFile.deleteOnExit()
                        val plantillaLaboral = PlantillaTres(
                            lugar,
                            fecha,
                            nombreTrabajador,
                            nombreFirma,
                            fechaInicio,
                            fechaSalida,
                            puesto,
                            sexo,
                            nombreEmpresa,
                            sizeLetter
                        )
                        plantillaLaboral.createPdf(outputFile)
                    }
                    3 -> {
                        outputFile = File.createTempFile(
                            "$now",
                            ".pdf",
                            outputDir
                        )
                        outputFile.deleteOnExit()
                        val plantillaLaboral = PlantillaCuatro(
                            lugar,
                            fecha,
                            nombreTrabajador,
                            nombreFirma,
                            fechaInicio,
                            puesto,
                            sueldo,
                            numeroEmisor,
                            nombreEmpresa,
                            sizeLetter
                        )
                        plantillaLaboral.createPdf(outputFile)
                    }
                    4 -> {
                        outputFile = File.createTempFile(
                            "$now",
                            ".pdf",
                            outputDir
                        )
                        outputFile.deleteOnExit()
                        val plantillaLaboral = PlantillaCinco(
                            lugar,
                            fecha,
                            nombreTrabajador,
                            nombreFirma,
                            fechaInicio,
                            fechaSalida,
                            dni,
                            puesto,
                            sexo,
                            nombreEmpresa,
                            numeroEmisor,
                            sueldo,
                            sizeLetter
                        )
                        plantillaLaboral.createPdf(outputFile)
                    }
                }

                val bundle = bundleOf("path" to outputFile!!.absolutePath)
                findNavController().navigate(
                    R.id.action_editPlantillaFragment_to_pdfPreviewFragment,
                    bundle
                )

                /*
                val i = Intent(this, PdfPreviewActivity::class.java)
                i.putExtra(Constantes.ABSOLUTE_PATH_PDFVIEW, outputFile!!.absolutePath)
                startActivity(i)

                 */
            }
        }
    }

    private fun showDialogDatosVacios() {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_AppCompat
        )
            .setMessage(resources.getString(R.string.faltan_datos))
            .setPositiveButton("Aceptar") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }


}