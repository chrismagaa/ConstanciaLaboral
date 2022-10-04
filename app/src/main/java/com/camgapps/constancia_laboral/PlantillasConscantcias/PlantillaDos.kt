package com.camgapps.constancia_laboral.PlantillasConscantcias

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.camgapps.constancia_laboral.R
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import java.io.ByteArrayOutputStream
import java.io.File


class PlantillaDos(
    var ctx: Context,
    var ciudad: String,
    var fecha: String,
    var nombreEmpresa: String,
    var nombreTrabajor: String,
    var nombreFirma: String,
    var fechaInicio: String,
    var fechaSalida: String,
    var puesto: String,
    var sexo: Int,
    var dni: String,
    var numero: String,
    var tamanoLetra: Float
) {

    companion object {
        fun getVisibility(): VisibilityViews {
            return VisibilityViews(
                vCiudad = true,
                vFecha = true,
                vNombreEmpresa = true,
                vNombreTrabajador = true,
                vNombreFirma = true,
                vFechaInicio = true,
                vFechaSalida = true,
                vPuesto = true,
                vSexo = true,
                vDni = true,
                vLogotipo = true,
                vNumeroEmisor = true
            )
        }
    }



    var strContexto = "Departamento de RRHH de [nombreEmpresa]\n\n" +
            "Por medio de la presente, se hace saber que, [sr] [nombreTrabajador]" +
            " con DNI [dni] ha prestado sus servicios en [nombreEmpresa], desde" +
            " [fechaInicio] hasta [fechaSalida] en el cargo de [puesto].\n\n" +
            "Se expide este documento a favor de la parte interesada en [ciudad], [fecha]\n\n\n" +
            "Atentamente,"


    fun createPdf(file: File, uri: Uri?) {
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.LETTER
        val document = Document(pdfDocument)
        document.setMargins(20F, 40F, 10F, 40F)

        // val font = FontFactory.getFont(FontFactory.HELVETICA, tamanoLetra)

       // val drw = ContextCompat.getDrawable(ctx, R.drawable.cuper)
        if(uri != null){

        val bit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(ctx.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(ctx.contentResolver, uri)
        }

        val stream = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        image.setHeight(100F)
        image.setWidth(100F)
            document.add(image)
        }else{
            val empresa = Text("\n$nombreEmpresa\n").setBold().setFontSize(22f)
            val pEmpresa = Paragraph(empresa).setTextAlignment(TextAlignment.CENTER)
            document.add(pEmpresa)
        }

        //Titulo
        //val fontTitle = FontFactory.getFont(FontFactory.HELVETICA, 30F)
        val textBold = Text("\nConstancia Laboral\n\n").setBold().setFontSize(22F)
        val pTitle = Paragraph(textBold).setTextAlignment(TextAlignment.CENTER)


        //Cuerpo
        var sr = ""
        if (sexo == 1) {
            sr = "el Sr."
        } else {
            sr = "la Srta."
        }
        var strContextoNuevo = strContexto.replace("[sr]", sr)
            .replace("[ciudad]", ciudad)
            .replace("[fecha]", fecha)
            .replace("[dni]", dni)
            .replace("[nombreTrabajador]", nombreTrabajor)
            .replace("[puesto]", puesto)
            .replace("[fechaInicio]", fechaInicio)
            .replace("[fechaSalida]", fechaSalida)
            .replace("[nombreEmpresa]", nombreEmpresa)

        val pContexto = Paragraph(strContextoNuevo).setTextAlignment(TextAlignment.JUSTIFIED).setFontSize(
            tamanoLetra
        )

        //Firma
        val pFirma = Paragraph("\n\n\n\n$nombreFirma\n$numero").setTextAlignment(TextAlignment.LEFT).setFontSize(
            tamanoLetra
        )


        document.add(pTitle)
        document.add(pContexto)
        document.add(pFirma)
        document.close()
    }


}