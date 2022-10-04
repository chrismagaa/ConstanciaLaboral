package com.camgapps.constancia_laboral.PlantillasConscantcias

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import java.io.File

class PlantillaCinco(
    var ciudad: String,
    var fecha: String,
    var nombreTrabajor: String,
    var nombreFirma: String,
    var fechaInicio: String,
    var fechaSalida: String,
    var dni: String,
    var puesto: String,
    var sexo: Int,
    var nombreEmpresa: String,
    var numero: String,
    var sueldo: String,
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
                vNumeroEmisor = true,
                vSueldo = true
            )
        }
    }



    var strContexto =
        "Por medio de la presente se hace constar que [ciudadano] [nombreTrabajador], portador de la cédula de identidad N°.- [dni]," +
                " prestó servicios en esta empresa como: [puesto], con una renumeración de [salario].\n\nFecha de ingreso: [fechaInicio]\nFecha de egreso: [fechaSalida]\n\n" +
                "Constancia que se expide a petición de parte interesada en [ciudad], [fecha].\n\n"


    fun createPdf(file: File) {
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.LETTER
        val document = Document(pdfDocument)
        document.setMargins(20F, 40F, 10F, 40F)

     //   val font = FontFactory.getFont(FontFactory.HELVETICA, tamanoLetra)

        val empresa = Text("\n$nombreEmpresa\n").setBold().setFontSize(22f)
        val pEmpresa = Paragraph(empresa).setTextAlignment(TextAlignment.CENTER)
        document.add(pEmpresa)

        //Titulo
      //  val fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, tamanoLetra)
        val textBold = Text("\n\nCONSTANCIA DE TRABAJO").setBold()
        val pTitulo = Paragraph(textBold).setTextAlignment(TextAlignment.CENTER).setFontSize(tamanoLetra)

        //Ciudad y Fecha



        //Cuerpo
        var ciudadano = ""
        if (sexo == 1) {
            ciudadano = "el ciudadano"
        } else {
            ciudadano = "la ciudadana"
        }


        var strContextoNuevo = strContexto.replace("[ciudadano]", ciudadano)
            .replace("[nombreTrabajador]", nombreTrabajor)
            .replace("[puesto]", puesto)
            .replace("[dni]", dni)
            .replace("[fechaInicio]", fechaInicio)
            .replace("[fechaSalida]", fechaSalida)
            .replace("[nombreEmpresa]", nombreEmpresa)
            .replace("[ciudad]", ciudad)
            .replace("[fecha]", fecha)
            .replace("[salario]", sueldo)

        val pContexto = Paragraph(strContextoNuevo).setTextAlignment(TextAlignment.JUSTIFIED).setFontSize(tamanoLetra)

        //Firma
        val pFirma = Paragraph("Atentamente\n\n\n\n$nombreFirma\n$numero").setTextAlignment(TextAlignment.CENTER).setFontSize(tamanoLetra)

        document.add(pTitulo)
        document.add(pContexto)
        document.add(pFirma)
        document.close()
    }

}