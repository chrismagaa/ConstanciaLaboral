package com.camgapps.constancia_laboral.PlantillasConscantcias

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import java.io.File

class PlantillaTres(
    var ciudad: String,
    var fecha: String,
    var nombreTrabajor: String,
    var nombreFirma: String,
    var fechaInicio: String,
    var fechaSalida: String,
    var puesto: String,
    var sexo: Int,
    var nombreEmpresa: String,
) {

    companion object {
        fun getVisibility(): VisibilityViews {
            return VisibilityViews(
                vCiudad = true,
                vFecha = true,
                vNombreTrabajador = true,
                vNombreEmpresa = true,
                vNombreFirma = true,
                vFechaInicio = true,
                vFechaSalida = true,
                vPuesto = true,
                vSexo = true
            )
        }
    }

    val tamanoLetra = 16F

    var strTitulo = "GERENTE DE RECURSOS HUMANOS\n\nHACE CONSTAR:\n\n"

    var strContexto =
        "Que el [sr] [nombreTrabajador] trabajo a tiempo parcial en nuestra empresa en el periodo comprendido entre" +
                " [fechaInicio] hasta [fechaSalida], con un contrato bajo la modalida nómina con turnos rotativos, con el cargo de" +
                " [puesto].\n\nDurante su periodo laboral ha demostrado ser una persona con grandes cualidades, con capacidad de liderazgo," +
                " eficiente y responsable.\n\n Esta constancia se expide para trámite de constancia laboral."


    fun createPdf(file: File) {
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
        val arrayFecha = fecha.split("/")
        val nuevaFecha = "${arrayFecha[0]} de ${arrayFecha[1]} del  ${arrayFecha[2]}"

        val pLugarFecha =
            Paragraph("\n$ciudad, $nuevaFecha\n\n").setTextAlignment(TextAlignment.LEFT).setFontSize(tamanoLetra)


        //Titulo
        // val fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, tamanoLetra)
        var textBold = Text(strTitulo).setBold()
        val pTitulo = Paragraph(textBold).setTextAlignment(TextAlignment.CENTER).setFontSize(tamanoLetra)

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

        val pContexto = Paragraph(strContextoNuevo).setTextAlignment(TextAlignment.JUSTIFIED).setFontSize(tamanoLetra)


        //Firma
        val pFirma =
            Paragraph("\n\n\n\n$nombreFirma\nGerente de Recursos Humanos").setTextAlignment(
                TextAlignment.CENTER
            ).setFontSize(tamanoLetra)

        document.add(pLugarFecha)
        document.add(pTitulo)
        document.add(pContexto)
        document.add(pFirma)
        document.close()
    }

}