//package com.bunch.of.ideas.doctorapi.service;
//
//import com.bunch.of.ideas.doctorapi.entity.Message;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
//@Service
//public class MessageService {
//
//    @Autowired
//    private MessageRepository messageRepository;
//
//    public List<Message> getMessagesForUser(String userEmail) {
//        return messageRepository.findByUserEmailOrderByTimestampAsc(userEmail);
//    }
//}
