package mn.edu.must.sqat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mn.edu.must.sqat.CourseRegistrationService;
import mn.edu.must.sqat.EmailSender;
import mn.edu.must.sqat.Student;
import mn.edu.must.sqat.StudentRepository;

import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

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

    @Test
    @DisplayName("A5: И-мэйл илгээхэд алдаа гарсан ч бүртгэл хадгалагдана")
    void register_emailSenderFails_returnsOkAndSavesRegistration() {
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

        doThrow(new RuntimeException("SMTP unavailable"))
                .when(emailSender)
                .send(anyString(), anyString(), anyString());

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
    @DisplayName("A6: Аль нэг ID null бол exception шидэж, хамаарал дуудахгүй")
    void register_eitherIdIsNull_throwsWithoutDependencyInteractions() {
        // Arrange
        String validStudentId = STUDENT_ID;
        String validCourseId = COURSE_ID;

        // Act + Assert: studentId null
        assertThrows(
                IllegalArgumentException.class,
                () -> service.register(null, validCourseId)
        );

        // Act + Assert: courseId null
        assertThrows(
                IllegalArgumentException.class,
                () -> service.register(validStudentId, null)
        );

        verifyNoInteractions(repository, emailSender);
    }

    @Test
    @DisplayName("A7: Илгээсэн и-мэйлийн body-д хичээлийн код байна")
    void register_successfulRegistration_emailBodyContainsCourseCode() {
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

        ArgumentCaptor<String> bodyCaptor =
                ArgumentCaptor.forClass(String.class);

        // Act
        String result = service.register(STUDENT_ID, COURSE_ID);

        // Assert
        assertEquals("OK", result);

        verify(emailSender).send(
                eq(EMAIL),
                anyString(),
                bodyCaptor.capture()
        );

        String capturedBody = bodyCaptor.getValue();
        assertTrue(capturedBody.contains(COURSE_ID));
    }

    @ParameterizedTest(name = "A8 [{index}] {0} → {1}")
    @CsvSource({
            "MISSING, ERROR_NO_STUDENT",
            "INACTIVE, ERROR_INACTIVE_STUDENT",
            "NO_COURSE, ERROR_NO_COURSE"
    })
    @DisplayName("A8: Алдааны ангид тохирох үр дүн буцаана")
    void register_invalidScenario_returnsExpectedError(String scenario, String expectedResult) {
        // Arrange
        Student student = switch (scenario) {
            case "MISSING" -> null;

            case "INACTIVE" -> new Student(
                    STUDENT_ID,
                    EMAIL,
                    false,
                    List.of("CS201")
            );

            case "NO_COURSE" -> new Student(
                    STUDENT_ID,
                    EMAIL,
                    true,
                    List.of("CS201")
            );

            default -> throw new IllegalArgumentException(
                    "Unknown test scenario: " + scenario
            );
        };

        when(repository.findStudent(STUDENT_ID)).thenReturn(student);

        lenient().when(repository.courseExists(COURSE_ID))
                .thenReturn(false);

        // Act
        String result = service.register(STUDENT_ID, COURSE_ID);

        // Assert
        assertEquals(expectedResult, result);

        verify(repository, never())
                .saveRegistration(anyString(), anyString());

        verifyNoInteractions(emailSender);
    }
}