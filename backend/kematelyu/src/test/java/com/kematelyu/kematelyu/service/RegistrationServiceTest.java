package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.model.*;
import com.kematelyu.kematelyu.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @InjectMocks private RegistrationService service;
    @Mock private RegistrationRepository regRepo;
    @Mock private CertificateRepository certRepo;

    private Registration reg;
    private Event event;
    private Mahasiswa mhs;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        event = new Event();   event.setId(7L);
        mhs   = new Mahasiswa(); mhs.setId(1L);
        reg   = new Registration(mhs, event); reg.setId(5L);
    }

    @Test
    void approveParticipant_first_time_issues_certificate() {
        when(regRepo.findById(5L)).thenReturn(Optional.of(reg));
        when(certRepo.existsByEventAndMahasiswa(event, mhs)).thenReturn(false);
        when(regRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(certRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Registration result = service.approveParticipant(5L);

        assertEquals(Registration.Status.APPROVED, result.getStatus());
        verify(certRepo).save(any(Certificate.class));
    }
}
