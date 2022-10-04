package com.camgapps.constancia_laboral.PlantillasConscantcias

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import java.io.File

class PlantillaCuatro (
        var ciudad: String,
        var fecha: String,
        var nombreTrabajor: String,
        var nombreFirma: String,
        var fechaInicio: String,
        var puesto: String,
        var sueldo: String,
        var numeroEmisor: String,
        var nombreEmpresa: String,
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
                    vSueldo = true,
                vPuesto = true,
                    vNumeroEmisor = true
            )
        }
    }



    var strContexto = "A quien corresponda: \n\n" +
        "Por medio de la presente, hago constar que [nombreTrabajador] labora en [nombreEmpresa] desde el [fechaInicio], desempeñando el puesto de [puesto]," +
            " con un sueldo de [salario].\n\nSin más por el momento, se extiende la presente para los fines que al interesado convengan.\n\nQuedo a sus órdenes" +
            " en caso de que requieran alguna información adicional."

    fun createPdf(file: File){
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.LETTER
        val document = Document(pdfDocument)
        document.setMargins(20F, 40F, 10F, 40F)

        //val font = FontFactory.getFont(FontFactory.HELVETICA, tamanoLetra)

        val empresa = Text("\n$nombreEmpresa\n").setBold().setFontSize(22f)
        val pEmpresa = Paragraph(empresa).setTextAlignment(TextAlignment.CENTER)
        document.add(pEmpresa)

        //Ciudad y Fecha

        val pLugarFecha = Paragraph("\n$ciudad, $fecha\n\n").setTextAlignment(TextAlignment.RIGHT).setFontSize(tamanoLetra)

        //Cuerpo

        var strContextoNuevo = strContexto
                .replace("[nombreTrabajador]", nombreTrabajor)
                .replace("[puesto]", puesto)
                .replace("[fechaInicio]", fechaInicio)
                .replace("[nombreEmpresa]", nombreEmpresa)
                .replace("[salario]", sueldo)

        val pContexto = Paragraph(strContextoNuevo).setTextAlignment(TextAlignment.JUSTIFIED).setFontSize(tamanoLetra)



        //Firma
        //val fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, tamanoLetra)
        var textBol = Text("\n\nATENTAMENTE\n\n\n\n$nombreFirma\n$numeroEmisor").setBold()
        val pFirma = Paragraph(textBol).setTextAlignment(TextAlignment.CENTER).setFontSize(tamanoLetra)

        document.add(pLugarFecha)
        document.add(pContexto)
        document.add(pFirma)
        document.close()
    }

}