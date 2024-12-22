package com.immobiliere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.immobiliere.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByHouseId(Long houseId);

	List<Message> findBySenderId(Long senderId);

	List<Message> findByRecipientId(Long recipientId);
}
