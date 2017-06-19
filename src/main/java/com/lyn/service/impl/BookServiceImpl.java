package com.lyn.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lyn.dao.AppointmentDao;
import com.lyn.dao.BookDao;
import com.lyn.dto.AppointExecution;
import com.lyn.entity.Appointment;
import com.lyn.entity.Book;
import com.lyn.enums.AppointStateEnum;
import com.lyn.exception.AppointException;
import com.lyn.exception.NoNumberException;
import com.lyn.exception.RepeatAppointException;
import com.lyn.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BookDao bookDao;
	@Autowired
	private AppointmentDao appointmentDao;

	public Book getById(long bookId) {
		return bookDao.queryById(bookId);
	}

	public List<Book> getList() {
		return bookDao.queryAll(0, 100);
	}

	@Transactional
	public AppointExecution appoint(long bookId, long studentId) {
		try {
			// 减库存
			int update = bookDao.reduceNumber(bookId);
			if (update <= 0) {// 库存不足
				// return new AppointExecution(bookId,
				// AppointStateEnum.NO_NUMBER);//错误写法
				throw new NoNumberException("not enough stock");
			} else {
				// 执行预约操作
				int insert = appointmentDao.insertAppointment(bookId, studentId);
				if (insert <= 0) {// 重复预约
					// return new AppointExecution(bookId,
					// AppointStateEnum.REPEAT_APPOINT);//错误写法
					throw new RepeatAppointException("repeat appoint");
				} else {// 预约成功
					Appointment appointment = appointmentDao.queryByKeyWithBook(bookId, studentId);
					return new AppointExecution(bookId, AppointStateEnum.SUCCESS, appointment);
				}
			}
			// 要先于catch
			// Exception异常前先catch住再抛出，不然自定义的异常也会被转换为AppointException，导致控制层无法具体识别是哪个异常
		} catch (NoNumberException e1) {
			throw e1;
		} catch (RepeatAppointException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 所有编译期异常转换为运行期异常
			// return new AppointExecution(bookId,
			// AppointStateEnum.INNER_ERROR);//错误写法
			throw new AppointException("appoint inner error:" + e.getMessage());
		}
	}

}
