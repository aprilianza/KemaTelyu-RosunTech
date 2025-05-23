package com.kematelyu.kematelyu.util;

import com.kematelyu.kematelyu.model.Certificate;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Staff;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CertificatePdfGenerator {

    private static final String TEMPLATE_PATH = "/static/certificate/template.jpg";

    private static final float PAGE_WIDTH = 960; // 1280 px
    private static final float PAGE_HEIGHT = 679; // 905 px

    private static final PDType1Font FONT = PDType1Font.HELVETICA_BOLD;

    private static final float FONT_SIZE_BIG = 36;
    private static final float FONT_SIZE_MED = 18;
    private static final float FONT_SIZE_SMALL = 14;

    public static byte[] generate(Certificate cert) throws IOException {

        Event event = cert.getEvent();
        Staff staff = event.getCreatedBy();

        String namaMhs = cert.getMahasiswa().getName();
        String namaEvent = event.getTitle();
        String tanggal = event.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID")));
        String namaStaff = staff.getName();
        String divisiStaff = staff.getDivisi();

        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage(new PDRectangle(PAGE_WIDTH, PAGE_HEIGHT));
            doc.addPage(page);

            ClassPathResource res = new ClassPathResource(TEMPLATE_PATH);
            BufferedImage img = ImageIO.read(res.getInputStream());
            PDImageXObject bg = PDImageXObject.createFromByteArray(doc, toBytes(img, "jpg"), "template");

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                cs.drawImage(bg, 0, 0, PAGE_WIDTH, PAGE_HEIGHT);
                float centerX = PAGE_WIDTH / 2;

                // merah: #B30202 → (179,2,2)
                float r = 179 / 255f;
                float g = 2 / 255f;
                float b = 2 / 255f;

                writeCentered(cs, FONT, FONT_SIZE_BIG, centerX, 395, namaMhs, r, g, b);
                writeCentered(cs, FONT, FONT_SIZE_MED, centerX, 290, namaEvent);
                writeCentered(cs, FONT, FONT_SIZE_SMALL, centerX + 120, 238, tanggal);
                writeCentered(cs, FONT, FONT_SIZE_SMALL, centerX, 105, namaStaff, r, g, b);
                writeCentered(cs, FONT, FONT_SIZE_SMALL, centerX, 80, divisiStaff);
            }

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                doc.save(baos);
                return baos.toByteArray();
            }
        }
    }

    private static void writeCentered(PDPageContentStream cs,
            PDType1Font font, float fontSize,
            float centerX, float y,
            String text) throws IOException {
        writeCentered(cs, font, fontSize, centerX, y, text, 0, 0, 0); // default hitam
    }

    private static void writeCentered(PDPageContentStream cs,
            PDType1Font font, float fontSize,
            float centerX, float y,
            String text, float r, float g, float b) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float startX = centerX - textWidth / 2;

        cs.setNonStrokingColor(r, g, b);
        cs.beginText();
        cs.setFont(font, fontSize);
        cs.newLineAtOffset(startX, y);
        cs.showText(text);
        cs.endText();
    }

    private static byte[] toBytes(BufferedImage img, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, format, baos);
            return baos.toByteArray();
        }
    }
}
