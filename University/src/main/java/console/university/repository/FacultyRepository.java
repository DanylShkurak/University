package console.university.repository;

import console.university.model.Faculty;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @EntityGraph(attributePaths = {"head", "lecturers"})
    Optional<Faculty> findFacultyByTitleIgnoreCase(String title);

    @Query("SELECT COUNT(f) FROM Faculty f JOIN f.lecturers WHERE LOWER(f.title) = LOWER(:title)")
    Integer countAllByTitle(String title);

    @Query("SELECT AVG(l.salary) FROM Faculty f JOIN f.lecturers l WHERE LOWER(f.title) = LOWER(:title)")
    Optional<BigDecimal> getAverageSalaryByTitle(String title);

    @Query("SELECT CONCAT(l.firstName, ' ', l.lastName) FROM Lecturer l " +
            "WHERE LOWER(CONCAT(l.firstName, ' ', l.lastName)) LIKE LOWER(CONCAT('%', :template, '%')) " +
            "UNION " +
            "SELECT f.title FROM Faculty f WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :template, '%'))")
    List<String> searchGlobal(@Param("template") String template);
}
