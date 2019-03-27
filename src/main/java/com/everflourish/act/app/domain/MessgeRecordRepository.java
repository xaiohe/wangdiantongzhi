package com.everflourish.act.app.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.everflourish.act.app.domain.bean.MessageRecord;

/**
 * demo jpa操作类
 * @author hzbin
 * @date 2017-6-22 上午11:09:06
 * @version 0.1.0
 */
@Repository
public interface MessgeRecordRepository extends JpaRepository<MessageRecord,String> {

}
