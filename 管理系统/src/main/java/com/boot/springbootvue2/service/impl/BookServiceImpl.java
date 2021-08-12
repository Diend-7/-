package com.boot.springbootvue2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.springbootvue2.entity.Book;
import com.boot.springbootvue2.mapper.BookMapper;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> {
}
