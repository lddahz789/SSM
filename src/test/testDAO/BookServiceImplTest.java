package testDAO;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lyn.dto.AppointExecution;
import com.lyn.service.BookService;

public class BookServiceImplTest extends BaseTest {

	@Autowired
	private BookService bookService;

	@Test
	public void testAppoint() throws Exception {
		long bookId = 1000;
		long studentId = 12345678910L;
		AppointExecution execution = bookService.appoint(bookId, studentId);
		System.out.println(execution);
	}

}