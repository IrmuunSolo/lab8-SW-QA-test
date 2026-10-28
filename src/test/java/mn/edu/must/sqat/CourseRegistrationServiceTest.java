package mn.edu.must.sqat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class CourseRegistrationServiceTest {

    private static final String STUDENT_ID = "S100";
    private static final String COURSE_ID = "F.CSA313";
    private static final String EMAIL = "s100@must.edu.mn";

    @Mock
    private StudentRepository repository;

    @Mock
    private EmailSender emailSender;

    private CourseRegistrationService service;

    @BeforeEach
    void setUp() {
        service = new CourseRegistrationService(repository, emailSender);
    }

    @Test
    @DisplayName("A1: Идэвхтэй оюутны бүртгэлийг хадгалж, и-мэйл илгээнэ")
    void register_activeStudent_savesRegistrationAndSendsEmail() {
        // Arrange
        Student student = new Student(
                STUDENT_ID,
                EMAIL,
                true,
                List.of("CS201")
        );

        when(repository.findStudent(STUDENT_ID)).thenReturn(student);
        when(repository.courseExists(COURSE_ID)).thenReturn(true);
        when(repository.prerequisites(COURSE_ID))
                .thenReturn(List.of("CS201"));

        // Act
        String result = service.register(STUDENT_ID, COURSE_ID);

        // Assert
        assertEquals("OK", result);

        verify(repository).saveRegistration(STUDENT_ID, COURSE_ID);

        verify(emailSender).send(
                eq(EMAIL),
                anyString(),
                contains(COURSE_ID)
        );
    }

    @Test
    @DisplayName("A2: Байхгүй оюутны бүртгэлийг хадгалахгүй, и-мэйл илгээхгүй")
    void register_missingStudent_returnsErrorWithoutSavingOrEmail() {
        // Arrange
        when(repository.findStudent(STUDENT_ID)).thenReturn(null);

        // Act
        String result = service.register(STUDENT_ID, COURSE_ID);

        // Assert
        assertEquals("ERROR_NO_STUDENT", result);

        verify(repository, never())
                .saveRegistration(anyString(), anyString());

        verify(emailSender, never())
                .send(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("A3: Идэвхгүй оюутанд алдаа буцааж, и-мэйлтэй харьцахгүй")
    void register_inactiveStudent_returnsErrorWithoutEmailInteraction() {
        // Arrange
        Student student = new Student(
                STUDENT_ID,
                EMAIL,
                false,
                List.of("CS201")
        );

        when(repository.findStudent(STUDENT_ID)).thenReturn(student);

        // Act
        String result = service.register(STUDENT_ID, COURSE_ID);

        // Assert
        assertEquals("ERROR_INACTIVE_STUDENT", result);

        verifyNoInteractions(emailSender);

        verify(repository, never())
                .saveRegistration(anyString(), anyString());
    }

    @Test
    @DisplayName("A4: Урьдач хичээл дутвал бүртгэлийг хадгалахгүй")
    void register_missingPrerequisites_returnsErrorWithoutSaving() {
        // Arrange
        Student student = new Student(
                STUDENT_ID,
                EMAIL,
                true,
                List.of()
        );

        when(repository.findStudent(STUDENT_ID)).thenReturn(student);
        when(repository.courseExists(COURSE_ID)).thenReturn(true);
        when(repository.prerequisites(COURSE_ID))
                .thenReturn(List.of("CS201"));

        // Act
        String result = service.register(STUDENT_ID, COURSE_ID);

        // Assert
        assertEquals("ERROR_PREREQUISITES", result);

        verify(repository, never())
                .saveRegistration(anyString(), anyString());

        verifyNoInteractions(emailSender);
    }
}