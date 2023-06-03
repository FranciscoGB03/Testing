package com.luv2code.springmvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.IHistoryGradesDao;
import com.luv2code.springmvc.repository.IMathGradesDao;
import com.luv2code.springmvc.repository.IScienceGradesDao;
import com.luv2code.springmvc.repository.IStudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;

@TestPropertySource("/application-test.properties")
@SpringBootTest
class StudentAndGradeServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    StudentAndGradeService studentService;

    @Autowired
    private IStudentDao studentDao;

    @Autowired
    private IMathGradesDao mathGradesDao;

    @Autowired
    private IScienceGradesDao scienceGradesDao;

    @Autowired
    private IHistoryGradesDao historyGradesDao;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    @BeforeEach
    void setupDatabase(){
        /*jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
            "values (10001, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')");
        
        jdbc.execute("insert into math_grade(id, student_id, grade) values(10,10001,100.00)");

        jdbc.execute("insert into science_grade(id, student_id, grade) values(10,10001,100.00)");

        jdbc.execute("insert into history_grade(id, student_id, grade) values(10,10001,100.00)");*/
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);

    }

    @Test
    void createStudentService(){
        studentService.createStudent("Chad", "Darby", "chad.darby@luv2code.com");

        CollegeStudent student= studentDao.findByEmailAddress("chad.darby@luv2code.com");

        assertEquals("chad.darby@luv2code.com", student.getEmailAddress(), "find by email");
    }

    @Test
    void isStudentNullCheck(){
        assertTrue(studentService.checkIfStudentIsNull(10001));

        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    void deleteStudentService(){
       
       Optional<CollegeStudent> deletedCollegeStudent= studentDao.findById(10001);
       Optional<MathGrade> deleteMathGrade=mathGradesDao.findById(10);
       Optional<HistoryGrade> deleteHistoryGrade=historyGradesDao.findById(10);
       Optional<ScienceGrade> deleteScienceGrade=scienceGradesDao.findById(10);
       
       assertTrue(deletedCollegeStudent.isPresent(),"Return true");
       assertTrue(deleteMathGrade.isPresent());
       assertTrue(deleteHistoryGrade.isPresent());
       assertTrue(deleteScienceGrade.isPresent());

       studentService.deleteStudent(10001);

       deletedCollegeStudent=studentDao.findById(10001);
       deleteMathGrade=mathGradesDao.findById(10);
       deleteHistoryGrade=historyGradesDao.findById(10);
       deleteScienceGrade=scienceGradesDao.findById(10);

       assertFalse(deletedCollegeStudent.isPresent(),"Return False");
       assertFalse(deleteMathGrade.isPresent());
       assertFalse(deleteHistoryGrade.isPresent());
       assertFalse(deleteScienceGrade.isPresent());
       
    }

    @Sql("/insertData.sql")
    @Test
    void getGradebookService(){
        Iterable<CollegeStudent> iterableCollegeStudents=studentService.getGradebook();
        List<CollegeStudent> collegeStudents=new ArrayList<>();
        
        for(CollegeStudent collegeStudent: iterableCollegeStudents){
            collegeStudents.add(collegeStudent);            
        }

        assertEquals(6, collegeStudents.size());
        
    }

    @Test
    void createGradeService(){

        //create the grade
        assertTrue(studentService.createGrade(80.50,10001,"math"));        
        assertTrue(studentService.createGrade(80.50, 10001, "science"));
        assertTrue(studentService.createGrade(80.50, 10001, "history"));

        //Get all grades with studentId
        Iterable<MathGrade> mathGrades=mathGradesDao.findGradeByStudentId(10001);
        Iterable<ScienceGrade> scienceGrades=scienceGradesDao.findGradeByStudentId(10001);
        Iterable<HistoryGrade> historyGrades=historyGradesDao.findGradeByStudentId(10001);

        //Verify there is grades
        //assertTrue(mathGrades.iterator().hasNext(), "Student has math grades");
        //assertTrue(scienceGrades.iterator().hasNext());
        //assertTrue(historyGrades.iterator().hasNext());
        assertTrue(((Collection<MathGrade>)mathGrades).size() == 2, "Student has math grades");
        assertTrue(((Collection<ScienceGrade>)scienceGrades).size() == 2);
        assertTrue(((Collection<HistoryGrade>)historyGrades).size() == 2);
        
    }

    @Test
    void createGradeServiceReturnFalse(){
        assertFalse(studentService.createGrade(105, 10001, "math"));
        assertFalse(studentService.createGrade(-5, 10001, "math"));
        assertFalse(studentService.createGrade(80.50, 2, "math"));
        assertFalse(studentService.createGrade(80.50, 10001, "literature"));
    }

    @Test
    void deleteGradeService(){
        assertEquals(10001,studentService.deleteGrade(10,"math"),"Returns student id after delete");
        assertEquals(10001,studentService.deleteGrade(10,"science"),"Returns student id after delete");
        assertEquals(10001,studentService.deleteGrade(10,"history"),"Returns student id after delete");
    }

    @Test
    void deleteGradeServiceReturnStudentIdOfZero(){
        assertEquals(0,studentService.deleteGrade(0,"science"),"No student should have 0 id");
        assertEquals(0,studentService.deleteGrade(1, "literature"), "No student should have a literature class");
    }

    @Test
    void studentInformation(){
        GradebookCollegeStudent gradebookCollegeStudent=studentService.studentInformation(10001);
        assertNotNull(gradebookCollegeStudent);
        assertEquals(10001,gradebookCollegeStudent.getId());
        assertEquals("Eric",gradebookCollegeStudent.getFirstname());
        assertEquals("Roby", gradebookCollegeStudent.getLastname());
        assertEquals("eric.roby@luv2code_school.com", gradebookCollegeStudent.getEmailAddress());
        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
        
    }

    @Test
    void studentInformationServiceReturnNull(){
        GradebookCollegeStudent gradebookCollegeStudent=studentService.studentInformation(0);

        assertNull(gradebookCollegeStudent);
    }

    @AfterEach
    void setupAfterTransaction(){ 
        /*jdbc.execute("DELETE FROM student");
        jdbc.execute("DELETE FROM math_grade");
        jdbc.execute("DELETE FROM science_grade");
        jdbc.execute("DELETE FROM history_grade");*/
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);

    }
}
