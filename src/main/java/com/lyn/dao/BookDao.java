package com.lyn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lyn.entity.Book;


public interface BookDao {

	Book queryById(long id);

	List<Book> queryAll(@Param("offset") int offset, @Param("limit") int limit);
	
	List<Book> queryAll();

	/**
	 * ���ٹݲ�����
	 * 
	 * @param bookId
	 * @return ���Ӱ����������>1����ʾ���µļ�¼����
	 */
	int reduceNumber(long bookId);
}
