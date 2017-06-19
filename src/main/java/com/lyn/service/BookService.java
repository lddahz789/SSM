package com.lyn.service;

import java.util.List;

import com.lyn.dto.AppointExecution;
import com.lyn.entity.Book;

/**
 * ҵ��ӿڣ�վ��"ʹ����"�Ƕ���ƽӿ� �������棺�����������ȣ��������������ͣ�return ����/�쳣��
 */
public interface BookService {

	/**
	 * ��ѯһ��ͼ��
	 * 
	 * @param bookId
	 * @return
	 */
	Book getById(long bookId);

	/**
	 * ��ѯ����ͼ��
	 * 
	 * @return
	 */
	List<Book> getList();

	/**
	 * ԤԼͼ��
	 * 
	 * @param bookId
	 * @param studentId
	 * @return
	 */
	AppointExecution appoint(long bookId, long studentId);

}