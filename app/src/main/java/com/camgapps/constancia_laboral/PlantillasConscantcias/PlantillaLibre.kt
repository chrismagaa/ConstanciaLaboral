package com.camgapps.constancia_laboral.PlantillasConscantcias

import android.graphics.Bitmap
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment
import java.io.File

class PlantillaLibre(var ciudad: String,
                     var fecha: String,
                     var cuerpo: String,
                     var nombreFirma: String
) {

    val tamanoLetra = 16F


    fun createPdf(file: File, bitmapSign: Bitmap? = null){
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.LETTER
        val document = Document(pdfDocument)
        document.setMargins(20F, 40F, 10F, 40F)

        //Ciudad y Fecha

        var arrayFecha = fecha.split("/")
        var nuevaFecha = if(arrayFecha.size > 1) "${arrayFecha[0]} de ${arrayFecha[1]} del  ${arrayFecha[2]}" else ""
        val pLugarFecha = Paragraph("\n$ciudad, $nuevaFecha\n\n").setTextAlignment(TextAlignment.RIGHT)
            .setFontSize(tamanoLetra)

        //Cuerpo
        var pContexto = Paragraph("$cuerpo\n").setTextAlignment(TextAlignment.JUSTIFIED)
            .setFontSize(tamanoLetra)


        val pFirma = Paragraph("\n\n\n\n$nombreFirma").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(tamanoLetra)



        document.add(pLugarFecha)
        document.add(pContexto)
        document.add(pFirma)
        document.close()
    }
}