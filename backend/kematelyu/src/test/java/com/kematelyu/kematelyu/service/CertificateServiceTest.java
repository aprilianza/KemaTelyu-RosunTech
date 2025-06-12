package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.dto.CertificateRequest;
import com.kematelyu.kematelyu.model.*;
import com.kematelyu.kematelyu.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CertificateServiceTest {

    @InjectMocks private CertificateService service;
    @Mock private CertificateRepository certRepo;
    @Mock private MahasiswaRepository mhsRepo;
    @Mock private EventRepository eventRepo;

    private final Mahasiswa mhs = new Mahasiswa();
    private final Event event   = new Event();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mhs.setId(1L);  mhs.setName("Athila");
        event.setId(3L); event.setTitle("Seminar");

        when(mhsRepo.findById(1L)).thenReturn(java.util.Optional.of(mhs));
        when(eventRepo.findById(3L)).thenReturn(java.util.Optional.of(event));
        when(certRepo.save(any(Certificate.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void createCertificate_success() {
        CertificateRequest req = new CertificateRequest(1L, 3L, LocalDate.now());

        Certificate saved = service.createCertificate(req);

        assertEquals(mhs,   saved.getMahasiswa());
        assertEquals(event, saved.getEvent());
        verify(certRepo).save(saved);
    }
}
