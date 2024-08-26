package console.university.service;

import console.university.exception.RecordNotFoundException;
import console.university.model.Faculty;
import console.university.model.Lecturer;
import console.university.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityService {
    private final FacultyRepository facultyRepository;

    public String getFacultyHead(String title) {
        Faculty faculty = facultyRepository.findFacultyByTitleIgnoreCase(title).orElseThrow(
                () -> new RecordNotFoundException("Faculty with title not found: " + title));
        return faculty.getHead().getFirstName() + " " + faculty.getHead().getLastName();
    }

    public String getFacultyStatistics(String title) {
        Faculty faculty = facultyRepository.findFacultyByTitleIgnoreCase(title).orElseThrow(
                () -> new RecordNotFoundException("Faculty with title not found: " + title));
        Map<String, Long> statistics = faculty.getLecturers()
                .stream()
                .map(Lecturer::getRank)
                .collect(Collectors.groupingBy(rank ->
                                rank.toString().replace("_", " ").toLowerCase(),
                        Collectors.counting()));

        List<String> result = new ArrayList<>();
        for (var entry : statistics.entrySet()) {
            result.add(entry.getKey() + "s - " + entry.getValue());
        }
        return String.join(System.lineSeparator(), result);
    }

    public BigDecimal getAverageSalary(String title) {
        return facultyRepository.getAverageSalaryByTitle(title).orElseThrow(
                () -> new RecordNotFoundException("Faculty with title not found: " + title));
    }

    public Integer getEmployeeCount(String title) {
        return facultyRepository.countAllByTitle(title);
    }

    public String searchAll(String template) {
        return String.join(", ", facultyRepository.searchGlobal(template));
    }
}