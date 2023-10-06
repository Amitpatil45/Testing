package com.root32.dentalproductservice.serviceImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.root32.dto.PdfAttachment;
import com.root32.entity.AdminCase;
import com.root32.entity.ProductBarcode;
import com.root32.entity.RetailerCase;

@Service
public class BarcodePdfGenerate {

	public List<PdfAttachment> generateProductBarCode(String pOCODE, String productName, Long numbersOfbarcodes,
			List<ProductBarcode> proProductBarcodes)
			throws MessagingException, DocumentException, MalformedURLException, IOException {

		String POCode = pOCODE;
		List<byte[]> pdfDataLists = new ArrayList<>();
		pdfDataLists.add(generateBarcodePdfDocument(proProductBarcodes, POCode, productName, numbersOfbarcodes));

		List<PdfAttachment> pdfAttachments = new ArrayList<>();
		int i = 0;
		for (byte[] pdfData : pdfDataLists) {
			String name = POCode + "attachment" + i + ".pdf"; // You can customize the name here

			// Create a PdfAttachment object
			PdfAttachment attachment = new PdfAttachment();
			attachment.setData(pdfData);
			attachment.setName(name);
			pdfAttachments.add(attachment);
			i = i + 1;
		}

		return pdfAttachments;
	}

	private byte[] generateBarcodePdfDocument(List<ProductBarcode> proProductBarcodes, String POCode,
			String productName, Long numbersOfbarcodes) throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);

		document.open();

		// PDF discription for details of products
		Paragraph ProjectName = new Paragraph("Root-32");
		ProjectName.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(ProjectName);
		Paragraph poCode = new Paragraph("Perchase Order Code:- " + POCode);
		poCode.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(poCode);
		Paragraph proName = new Paragraph("Product Name:- " + productName);
		proName.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(proName);
		Paragraph quantityPro = new Paragraph("Quantity Of the Product:-  " + numbersOfbarcodes);
		quantityPro.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(quantityPro);

		document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space

		// Create a line separator
		LineSeparator lineSeparator = new LineSeparator();
		lineSeparator.setLineColor(BaseColor.BLACK);
		lineSeparator.setLineWidth(1); // Set line width
		document.add(lineSeparator);

		document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space

		List<Image> objects = new ArrayList<>();

		for (ProductBarcode productBarcode : proProductBarcodes) {
			String porProductBarcode = productBarcode.getProductBarcode();
			Image image = createBarcodeImage(porProductBarcode);
			objects.add(image);
		}

		int totalObjects = objects.size();

		for (int i = 0; i < totalObjects; i += 2) {
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100); // Table width is 100% of the page width

			Image image1 = Image.getInstance(objects.get(i));
			PdfPCell cell1 = new PdfPCell(image1);
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell1);

			if (i + 1 < totalObjects) {
				Image image2 = Image.getInstance(objects.get(i + 1));
				PdfPCell cell2 = new PdfPCell(image2);
				cell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				cell2.setBorder(PdfPCell.NO_BORDER);
				table.addCell(cell2);
			} else {
				// Add an empty cell if there's only one image left
				PdfPCell emptyCell = new PdfPCell();
				emptyCell.setBorder(PdfPCell.NO_BORDER);
				table.addCell(emptyCell);
			}

			document.add(table);
			document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space
		}

		document.close();

		return baos.toByteArray();
	}

	private com.itextpdf.text.Image createBarcodeImage(String data)
			throws BadElementException, MalformedURLException, IOException {
		BarcodeFormat format = BarcodeFormat.CODE_128;
		Code128Writer writer = new Code128Writer();
		// BitMatrix bitMatrix = writer.encode(data, format, 40, 40);
		BitMatrix bitMatrix = writer.encode(data, format, 96, 48);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(bitMatrixToByteArray(bitMatrix));

		return image;
	}

	private byte[] bitMatrixToByteArray(BitMatrix bitMatrix) throws IOException {
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
			}
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		return baos.toByteArray();
	}

	/////// batch code pdf

	byte[] generateBatchBarcodePdfDocument(List<AdminCase> adminCase, String createdBy, Date createdDate)
			throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);

		document.open();

		// PDF discription for details of products
		Paragraph ProjectName = new Paragraph("Root-32");
		ProjectName.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(ProjectName);
		Paragraph creater = new Paragraph("Case created By:- " + createdBy);
		creater.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(creater);
		Paragraph createddate = new Paragraph("Case created Date:- " + createdDate);
		createddate.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(createddate);

		document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space

		// Create a line separator
		LineSeparator lineSeparator = new LineSeparator();
		lineSeparator.setLineColor(BaseColor.BLACK);
		lineSeparator.setLineWidth(1); // Set line width
		document.add(lineSeparator);

		document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space

		List<Image> objects = new ArrayList<>();

		for (AdminCase adminBarcodeCase : adminCase) {
			String porbatchBarcode = adminBarcodeCase.getCaseCode();
			Image image = createBarcodeImage(porbatchBarcode);
			objects.add(image);
		}

		int totalObjects = objects.size();

		for (int i = 0; i < totalObjects; i += 2) {
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100); // Table width is 100% of the page width

			Image image1 = Image.getInstance(objects.get(i));
			PdfPCell cell1 = new PdfPCell(image1);
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell1);

			if (i + 1 < totalObjects) {
				Image image2 = Image.getInstance(objects.get(i + 1));
				PdfPCell cell2 = new PdfPCell(image2);
				cell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				cell2.setBorder(PdfPCell.NO_BORDER);
				table.addCell(cell2);
			} else {
				// Add an empty cell if there's only one image left
				PdfPCell emptyCell = new PdfPCell();
				emptyCell.setBorder(PdfPCell.NO_BORDER);
				table.addCell(emptyCell);
			}

			document.add(table);
			document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space
		}

		document.close();

		return baos.toByteArray();
	}

	byte[] generateRetailerCaseBarcodePdfDocument(List<RetailerCase> retailerCaseList, String createdBy,
			Date createdDate) throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);

		document.open();

		// PDF discription for details of products
		Paragraph ProjectName = new Paragraph("Root-32");
		ProjectName.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(ProjectName);
		Paragraph creater = new Paragraph("Case created By:- " + createdBy);
		creater.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(creater);
		Paragraph createddate = new Paragraph("Case created Date:- " + createdDate);
		createddate.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(createddate);

		document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space

		// Create a line separator
		LineSeparator lineSeparator = new LineSeparator();
		lineSeparator.setLineColor(BaseColor.BLACK);
		lineSeparator.setLineWidth(1); // Set line width
		document.add(lineSeparator);

		document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space

		List<Image> objects = new ArrayList<>();

		for (RetailerCase adminBarcodeCase : retailerCaseList) {
			String porbatchBarcode = adminBarcodeCase.getCode();
			Image image = createBarcodeImage(porbatchBarcode);
			objects.add(image);
		}

		int totalObjects = objects.size();

		for (int i = 0; i < totalObjects; i += 2) {
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100); // Table width is 100% of the page width

			Image image1 = Image.getInstance(objects.get(i));
			PdfPCell cell1 = new PdfPCell(image1);
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell1);

			if (i + 1 < totalObjects) {
				Image image2 = Image.getInstance(objects.get(i + 1));
				PdfPCell cell2 = new PdfPCell(image2);
				cell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				cell2.setBorder(PdfPCell.NO_BORDER);
				table.addCell(cell2);
			} else {
				// Add an empty cell if there's only one image left
				PdfPCell emptyCell = new PdfPCell();
				emptyCell.setBorder(PdfPCell.NO_BORDER);
				table.addCell(emptyCell);
			}

			document.add(table);
			document.add(new com.itextpdf.text.Paragraph("\n")); // Add some space
		}

		document.close();

		return baos.toByteArray();
	}

}
