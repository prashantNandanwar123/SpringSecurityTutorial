package com.security.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.security.entity.Post;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QrCodeGenerator {
	private static final Logger logger = LogManager.getLogger(QrCodeGenerator.class);

	public static byte[] generateQRCode(Post task, String additionalData) {
		if (task == null) {
			logger.error("Post is null. Cannot generate QR code.");
			return null;
		}
		String combinedData = task.getId() + task.getTitle() + task.getContent() + task.getDescription() + ":"
				+ additionalData;
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(combinedData, BarcodeFormat.QR_CODE, 200, 200, hints);
		} catch (WriterException e) {
			logger.error("Error generating QR code: WriterException - {}", e.getMessage());
		}
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
			return outputStream.toByteArray();
		} catch (IOException e) {
			logger.error("Error writing QR code to stream: IOException - {}", e.getMessage());
		}
		return null;
	}
}