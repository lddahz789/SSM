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
			// �����
			int update = bookDao.reduceNumber(bookId);
			if (update <= 0) {// ��治��
				// return new AppointExecution(bookId,
				// AppointStateEnum.NO_NUMBER);//����д��
				throw new NoNumberException("not enough stock");
			} else {
				// ִ��ԤԼ����
				int insert = appointmentDao.insertAppointment(bookId, studentId);
				if (insert <= 0) {// �ظ�ԤԼ
					// return new AppointExecution(bookId,
					// AppointStateEnum.REPEAT_APPOINT);//����д��
					throw new RepeatAppointException("repeat appoint");
				} else {// ԤԼ�ɹ�
					Appointment appointment = appointmentDao.queryByKeyWithBook(bookId, studentId);
					return new AppointExecution(bookId, AppointStateEnum.SUCCESS, appointment);
				}
			}
			// Ҫ����catch
			// Exception�쳣ǰ��catchס���׳�����Ȼ�Զ�����쳣Ҳ�ᱻת��ΪAppointException�����¿��Ʋ��޷�����ʶ�����ĸ��쳣
		} catch (NoNumberException e1) {
			throw e1;
		} catch (RepeatAppointException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// ���б������쳣ת��Ϊ�������쳣
			// return new AppointExecution(bookId,
			// AppointStateEnum.INNER_ERROR);//����д��
			throw new AppointException("appoint inner error:" + e.getMessage());
		}
	}

}
