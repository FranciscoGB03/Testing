package com.luv2code.springmvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.IMathGradesDao;
import com.luv2code.springmvc.repository.IStudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
class GradebookControllerTest {
    
    private static MockHttpServletRequest request;
    
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentCreateServiceMock;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private IStudentDao studentDao;

    @Autowired
    private IMathGradesDao mathGradesDao;

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

    @BeforeAll
    static void setUp(){
        request=new MockHttpServletRequest();
        request.setParameter("firstname", "Chad");
        request.setParameter("lastname", "Darby");
        request.setParameter("emailAddress", "chad.darby@pgonbo.com");
    }

    @BeforeEach
    void beforeEach(){
        /*jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
        "values (10001, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')");*/
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }

    @Test
    void getStudentsHttpRequest() throws Exception{
        CollegeStudent studentOne= new CollegeStudent("Eric", "Roby", "eric_roby@pgonbo.com");
        CollegeStudent studentTwo= new CollegeStudent("Chad", "Darby", "chad_darby@pgonbo.com");

        List<CollegeStudent> collegeStudent=new ArrayList<>(Arrays.asList(studentOne,studentTwo));

        when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudent);
        
        assertIterableEquals(collegeStudent,studentCreateServiceMock.getGradebook());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/"))
                                    .andExpect(status().isOk()).andReturn();
        
        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"index");
    }

    @Test
    void createStudentHttpRequest()throws Exception{

        CollegeStudent studentOne=new CollegeStudent("Eric", "Roby", "eric_roby@pgonbo.com");

        List<CollegeStudent> collegeStudentList=new ArrayList<>(Arrays.asList(studentOne));

        when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

        MvcResult mvcResult=this.mockMvc.perform(post("/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("firsname", request.getParameter("firsname"))
                    .queryParam("lastname", request.getParameter("lastname"))
                    .queryParam("emailAddress", request.getParameter("emailAddress")))
                    .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"index");

        CollegeStudent verifyStudent=studentDao.findByEmailAddress("chad.darby@pgonbo.com");

        assertNotNull(verifyStudent, "Student should be found");
    }

    @Test
    void deleteStudentHttpRequest() throws Exception{
        assertTrue(studentDao.findById(10001).isPresent());
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders
        .get("/delete/student/{id}", 10001))
        .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

        assertFalse(studentDao.findById(10001).isPresent());
    }
    
    @Test
    void deleteStudentHttpRequestErrorPage() throws Exception{
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders
        .get("/delete/student/{id}", 0))
        .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test 
    void studentInformationHttpRequest() throws Exception{
        
        assertTrue(studentDao.findById(10001).isPresent());
        
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",10001))
            .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"studentInformation");

    }

    @Test
    void studentInformationHttpStudentDoesNotExistRequest() throws Exception{
        
        assertFalse(studentDao.findById(0).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",0))
            .andExpect(status().isOk()).andReturn();
        
        ModelAndView mav= mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    void createValidGradeHttpRequest() throws Exception{

        assertTrue(studentDao.findById(10001).isPresent());

        GradebookCollegeStudent student= studentService.studentInformation(10001);

        assertEquals(1,student.getStudentGrades().getMathGradeResults().size());

        MvcResult mvcResult=this.mockMvc.perform(post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("grade","85.50")
                .queryParam("gradeType", "math")
                .queryParam("studentId", "10001"))
                .andExpect(status().isOk()).andReturn();
        
        ModelAndView mav= mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "studentInformation");

        student=studentService.studentInformation(10001);

        assertEquals(2,student.getStudentGrades().getMathGradeResults().size());
    }

    @Test
    void createValidGradeHttpRequestStudentDoesNotExistEmptyResponse() throws Exception{
        MvcResult mvcResult= this.mockMvc.perform(post("/grades")
        .contentType(MediaType.APPLICATION_JSON)
        .queryParam("grade", "85.00")
        .queryParam("gradeType", "history")
        .queryParam("studentId", "0"))
        .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    void createANonValidGrateHttpRequestGradeTypeDoesNotExistEmptyResponse() throws Exception{
        MvcResult mvcResult=this.mockMvc.perform(post("/grades")
        .contentType(MediaType.APPLICATION_JSON)
        .queryParam("grade", "85.00")
        .queryParam("gradeType", "literature")
        .queryParam("studentId", "10001"))
        .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    void deleteAValidGradeHttpRequest()throws Exception{

        Optional<MathGrade>mathGrade=mathGradesDao.findById(10);

        assertTrue(mathGrade.isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders
            .get("/grades/{id}/{gradeType}", 10, "math"))
            .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"studentInformation");

        mathGrade=mathGradesDao.findById(10);

        assertFalse(mathGrade.isPresent());
    }

    @Test
    void deleteAValidGradeHttpRequestStudentIdDoesNotExistEmptyResponse() throws Exception{

        Optional<MathGrade>mathGrade=mathGradesDao.findById(20);

        assertFalse(mathGrade.isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders
            .get("/grades/{id}/{gradeType}", 20, "math"))
            .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"error");
    }

    @Test
    void deleteANonValidGradeHttpRequest() throws Exception{
        MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders
        .get("/grades/{id}/{gradeType}",1,"literature"))
        .andExpect(status().isOk()).andReturn();

        ModelAndView mav= mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }



    @AfterEach
    void setupAfterTransaction(){
        /*jdbc.execute("DELETE FROM student");*/
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);

    }
    
}
