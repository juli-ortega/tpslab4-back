package com.tp4lab4.instrumentos.Service;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tp4lab4.instrumentos.Model.Instrumento;
import com.tp4lab4.instrumentos.Model.Dto.InstrumentoDto;
import com.tp4lab4.instrumentos.Repository.InstrumentosRepository;

@Service
public class InstrumentoService {

    @Autowired
    private InstrumentosRepository instrumentosRepository;

    public List<Instrumento> getAllInstrumentos() {
        return instrumentosRepository.findAll();
    }

    public Instrumento getInstrumentoById(Long id) {
        return instrumentosRepository.findById(id).orElse(null);
    }

    public Instrumento saveInstrumento(Instrumento instrumento) {
        return instrumentosRepository.save(instrumento);
    }

    public void deleteInstrumento(Long id) {
        instrumentosRepository.deleteById(id);
    }

    public List<Instrumento> saveAll(List<Instrumento> instrumentos) {
        return instrumentosRepository.saveAll(instrumentos);
    }

    public Instrumento updateInstrumento(Long id, Instrumento instrumento) {
        Instrumento existingInstrumento = getInstrumentoById(id);

        existingInstrumento.setInstrumento(instrumento.getInstrumento());
        existingInstrumento.setMarca(instrumento.getMarca());
        existingInstrumento.setModelo(instrumento.getModelo());
        existingInstrumento.setImagen(instrumento.getImagen());
        existingInstrumento.setPrecio(instrumento.getPrecio());
        existingInstrumento.setCostoEnvio(instrumento.getCostoEnvio());
        existingInstrumento.setCantidadVendida(instrumento.getCantidadVendida());
        existingInstrumento.setDescripcion(instrumento.getDescripcion());
        existingInstrumento.setCategoria(instrumento.getCategoria());

        return instrumentosRepository.save(existingInstrumento);
    }

    public String saveImage(MultipartFile file) throws IOException {
        // Límite de tamaño: 5 MB (5 * 1024 * 1024 bytes)
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IOException("El archivo supera el tamaño máximo permitido de 5 MB.");
        }

        // Leer los primeros 8 bytes para verificar magic bytes
        byte[] header = new byte[8];
        InputStream is = file.getInputStream();
        is.read(header);
        if (!isImage(header)) {
            throw new IOException("El archivo no es una imagen válida.");
        }

        // Renombrar el archivo usando UUID para hacerlo único
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Guardar imagen en uploads/
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = uploadDir + uniqueFilename;
        file.transferTo(new File(filePath));

        // Devolver URL pública
        return "http://localhost:8080/uploads/" + uniqueFilename;
    }

    // Validación de magic bytes
    private boolean isImage(byte[] header) {
        // JPG/JPEG
        if (header[0] == (byte) 0xFF && header[1] == (byte) 0xD8) {
            return true;
        }
        // PNG
        if (header[0] == (byte) 0x89 && header[1] == (byte) 0x50 &&
                header[2] == (byte) 0x4E && header[3] == (byte) 0x47) {
            return true;
        }
        // GIF
        if (header[0] == (byte) 0x47 && header[1] == (byte) 0x49 &&
                header[2] == (byte) 0x46) {
            return true;
        }
        // BMP
        if (header[0] == (byte) 0x42 && header[1] == (byte) 0x4D) {
            return true;
        }

        return false;
    }

    public Instrumento mapDtoToEntity(InstrumentoDto dto) {
        return Instrumento.builder()
                .instrumento(dto.getInstrumento())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .imagen(dto.getImagen())
                .precio(dto.getPrecio())
                .costoEnvio(dto.getCostoEnvio())
                .cantidadVendida(dto.getCantidadVendida())
                .descripcion(dto.getDescripcion())
                .categoria(dto.getCategoria())
                .build();
    }

   public byte[] generarInstrumentoPdf(Instrumento instrumento) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, baos);
        document.open();

        // Cantidad vendida
        document.add(new Paragraph(instrumento.getCantidadVendida() + " vendidos", 
            FontFactory.getFont(FontFactory.HELVETICA, 12)));

        // Nombre del instrumento
        document.add(new Paragraph(instrumento.getInstrumento(), 
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26)));

        // Precio
        document.add(new Paragraph("$ " + instrumento.getPrecio(), 
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 30)));

        // Marca y modelo
        document.add(new Paragraph("Marca: " + instrumento.getMarca(), 
            FontFactory.getFont(FontFactory.HELVETICA, 14)));
        document.add(new Paragraph("Modelo: " + instrumento.getModelo(), 
            FontFactory.getFont(FontFactory.HELVETICA, 14)));

        document.add(new Paragraph("\n"));

        // Costo de envío
        if ("G".equalsIgnoreCase(instrumento.getCostoEnvio())) {
            document.add(new Paragraph("🚚 Envío gratis a todo el país",
                FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, Color.green)));
        } else {
            document.add(new Paragraph("Costo de envío interior de Argentina: $" + instrumento.getCostoEnvio(),
                FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, Color.orange)));
        }

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("\n"));

        // Imagen
        try {
            Image img = Image.getInstance(new URL(instrumento.getImagen()));
            img.scaleToFit(400, 400);
            img.setAlignment(Image.ALIGN_CENTER);
            document.add(img);
        } catch (Exception e) {
            document.add(new Paragraph("Imagen no disponible"));
        }

        document.add(new Paragraph("\n"));

        // Descripción
        document.add(new Paragraph("Descripción:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph(instrumento.getDescripcion(), FontFactory.getFont(FontFactory.HELVETICA, 12)));

        document.close();
        return baos.toByteArray();
    }



}
