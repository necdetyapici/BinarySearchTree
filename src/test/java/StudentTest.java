import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.foreks.feed.model.Student;

public class StudentTest {
    @Test
    public void createStudent() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        final Student student = (Student) Class.forName("com.foreks.feed.model.Student").newInstance();
        assertNotNull(student);
        student.setId(12541213);
        student.setName("Necdet");
        student.setLastName("Yapici");

        assertEquals("Necdet", student.getName());
        assertEquals("Yapici", student.getLastName());
        assertEquals((Integer) 12541213, student.getId());

    }
}
