package com.example.mining_algorithms.tools

import android.os.Build
import com.example.mining_algorithms.service.MiningService
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream

fun generateExcelFile(): ByteArrayOutputStream {
    val workbook: Workbook = XSSFWorkbook()
    val sheet: Sheet = workbook.createSheet("Blockchain")
    sheet.setColumnWidth(0, 6000)
    sheet.setColumnWidth(1, 4000)

    createHeader(workbook, sheet)
    writeData(workbook, sheet)
    writeResults(workbook)
    writeMetadata(workbook)

    val baos = ByteArrayOutputStream()
    workbook.write(baos)
    workbook.close()
    return baos
}

private fun createHeader(workbook: Workbook, sheet: Sheet) {
    val header: Row = sheet.createRow(0)

    val headerStyle = workbook.createCellStyle()
    headerStyle.fillForegroundColor = IndexedColors.LIGHT_BLUE.getIndex()
    headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

    val font = (workbook as XSSFWorkbook).createFont()
    font.fontName = "Arial"
    font.fontHeightInPoints = 14.toShort()
    font.bold = true
    headerStyle.setFont(font)

    var headerCell: Cell = header.createCell(0)
    headerCell.setCellValue("Hash")
    headerCell.cellStyle = headerStyle

    headerCell = header.createCell(1)
    headerCell.setCellValue("Time")
    headerCell.cellStyle = headerStyle
}

private fun writeData(workbook: Workbook, sheet: Sheet) {
    var rawNum = 1

    val style = workbook.createCellStyle()
    style.wrapText = true

    val blockchain = MiningService.blockchain
    blockchain.forEach {
        val row = sheet.createRow(rawNum)

        var cell = row.createCell(0)
        cell.setCellValue(it.hash)
        cell.cellStyle = style

        cell = row.createCell(1)
        cell.setCellValue(it.timeSpent.toString())
        cell.cellStyle = style

        rawNum++
    }
}

private fun writeMetadata(workbook: Workbook) {
    val sheet: Sheet = workbook.createSheet("Metadata")
    sheet.setColumnWidth(0, 4000)
    sheet.setColumnWidth(1, 4000)

    val headerStyle = workbook.createCellStyle()

    val font = (workbook as XSSFWorkbook).createFont()
    font.fontName = "Arial"
    font.fontHeightInPoints = 12.toShort()
    headerStyle.setFont(font)

    var header: Row = sheet.createRow(0)
    var headerCell: Cell = header.createCell(0)
    headerCell.setCellValue("Model")
    headerCell.cellStyle = headerStyle

    headerCell = header.createCell(1)
    headerCell.setCellValue(Build.MODEL)
    headerCell.cellStyle = headerStyle

    header = sheet.createRow(1)

    headerCell = header.createCell(0)
    headerCell.setCellValue("Manufacturer")
    headerCell.cellStyle = headerStyle

    headerCell = header.createCell(1)
    headerCell.setCellValue(Build.MANUFACTURER)
    headerCell.cellStyle = headerStyle

    header = sheet.createRow(2)

    headerCell = header.createCell(0)
    headerCell.setCellValue("Android SDK")
    headerCell.cellStyle = headerStyle

    headerCell = header.createCell(1)
    headerCell.setCellValue(Build.VERSION.SDK_INT.toString())
    headerCell.cellStyle = headerStyle
}

private fun writeResults(workbook: Workbook) {
    val blockchain = MiningService.blockchain
    val sheet: Sheet = workbook.createSheet("Results")
    sheet.setColumnWidth(0, 6000)
    sheet.setColumnWidth(1, 4000)

    val headerStyle = workbook.createCellStyle()

    val font = (workbook as XSSFWorkbook).createFont()
    font.fontName = "Arial"
    font.fontHeightInPoints = 12.toShort()
    headerStyle.setFont(font)

    var header: Row = sheet.createRow(0)
    var headerCell: Cell = header.createCell(0)
    headerCell.setCellValue("Blocks count")
    headerCell.cellStyle = headerStyle

    headerCell = header.createCell(1)
    headerCell.setCellValue(blockchain.size.toString())
    headerCell.cellStyle = headerStyle

    header = sheet.createRow(1)

    headerCell = header.createCell(0)
    headerCell.setCellValue("Total time spent")
    headerCell.cellStyle = headerStyle

    headerCell = header.createCell(1)
    headerCell.setCellValue(MiningService.getTotalSpentTime().toString())
    headerCell.cellStyle = headerStyle

    header = sheet.createRow(2)

    headerCell = header.createCell(0)
    headerCell.setCellValue("Total hashes count")
    headerCell.cellStyle = headerStyle

    headerCell = header.createCell(1)
    headerCell.setCellValue(MiningService.totalHashCount.toString())
    headerCell.cellStyle = headerStyle
}