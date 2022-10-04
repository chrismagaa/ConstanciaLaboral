package com.camgapps.constancia_laboral.PlantillasConscantcias

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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

class PlantillaUno(
    val ctx: Context,
    var ciudad: String,
    var fecha: String,
    var nombreEmpresa: String,
    var nombreTrabajor: String,
    var nombreFirma: String,
    var fechaInicio: String,
    var fechaSalida: String,
    var puesto: String,
    var sexo: Int,
    var numero: String
) {

    companion object {
        fun getVisibility(): VisibilityViews {
            return VisibilityViews(
                vCiudad = true,
                vFecha = true,
                vNombreEmpresa = true,
                vNombreTrabajador = true,
                vPuesto = true,
                vNombreFirma = true,
                vFechaInicio = true,
                vFechaSalida = true,
                vSexo = true,
                vLogotipo = true,
                vNumeroEmisor = true
            )
        }
    }

    val tamanoLetra = 16F


    var strContexto = "A quien corresponda: \n\n" +
            "Por medio de la presente hago de su conocimiento que [sr] [nombreTrabajador] laboró como" +
            " [puesto] del [fechaInicio] al [fechaSalida] en la empresa [nombreEmpresa], cumpliendo con un" +
            " desempeño satisfactorio.\n\nDurante su estancia en la empresa demostró ser una persona responsable.\n\n" +
            "Extiendo la presente para los fines que convenga el interesado.\n\n\n Atentamente,"


    fun createPdf(file: File, uri: Uri?) {

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.LETTER
        val document = Document(pdfDocument)
        document.setMargins(20F, 40F, 10F, 40F)

        if (uri != null) {
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

        //Ciudad y Fecha
        val arrayFecha = fecha.split("/")
        val nuevaFecha = "${arrayFecha[0]} de ${arrayFecha[1]} del  ${arrayFecha[2]}"

        val pLugarFecha =
            Paragraph("\n$ciudad, $nuevaFecha\n\n").setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(tamanoLetra)

        //Cuerpo
        var sr = ""
        if (sexo == 1) {
            sr = "el Sr."
        } else {
            sr = "la Srta."
        }

        val arrayFechaInicio = fechaInicio.split("/")
        val nuevaFechaInicio = "${arrayFechaInicio[1]} del ${arrayFechaInicio[2]}"
        val arrayFechaSalida = fechaSalida.split("/")
        var nuevaFechaSalida = "${arrayFechaSalida[1]} del ${arrayFechaSalida[2]}"
        var strContextoNuevo = strContexto.replace("[sr]", sr)
            .replace("[nombreTrabajador]", nombreTrabajor)
            .replace("[puesto]", puesto)
            .replace("[fechaInicio]", nuevaFechaInicio)
            .replace("[fechaSalida]", nuevaFechaSalida)
            .replace("[nombreEmpresa]", nombreEmpresa)

        val pContexto = Paragraph(strContextoNuevo).setTextAlignment(TextAlignment.JUSTIFIED)
            .setFontSize(tamanoLetra)

        //Firma
        val pFirma = Paragraph("\n\n\n\n$nombreFirma\n$numero").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(tamanoLetra)

        document.add(pLugarFecha)
        document.add(pContexto)
        document.add(pFirma)
        document.close()
    }

}