package media.toloka.rfa.radio.messanger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import media.toloka.rfa.radio.client.service.ClientService;
//import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.messanger.model.ChatListElement;
import media.toloka.rfa.radio.messanger.model.ChatMessage;
import media.toloka.rfa.radio.messanger.service.ChatReferenceSingleton;
import media.toloka.rfa.radio.messanger.service.MessangerService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.messanger.model.MessageRoom;
import media.toloka.rfa.radio.model.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;


@Controller
public class ChatController {
// https://kiberstender.github.io/miscelaneous-spring-websocket-stomp-specific-user/
// https://medium.com/@liakh-aliaksandr/websockets-with-spring-part-3-stomp-over-websocket-3dab4a21f397
// https://stomp-js.github.io/guide/stompjs/using-stompjs-v5.html

    @Autowired
    private ClientService clientService;

    private SimpMessagingTemplate template;

//    @Autowired
//    private MessageService messageService;

    @Autowired
    private MessangerService messangerService;

    @Autowired
    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    final Logger logger = LoggerFactory.getLogger(ChatController.class);

        @MessageMapping("/hello")
        public void GetChatMessage( ChatMessage inmsg) {
            // add user to userlist for room
            try {
            messangerService.AddChatUser(inmsg);
            Clientdetail cd = clientService.GetClientDetailByUUID(inmsg.getUuid());

            List<ChatMessage> publicMessageList = messangerService.GetChatPublicRoomList(inmsg.getRoomuuid());
//            ChatMessage cmsg = new ChatMessage();
            Iterator<ChatMessage> iterator = publicMessageList.iterator();
            while (iterator.hasNext()) {
                ChatMessage imsg = iterator.next();
                this.template.convertAndSend("/public/"+imsg.getUuid(), imsg);
            }
            List<ChatMessage> privateMessageList = messangerService.GetMessagesAsc(inmsg.getUuid());
            Iterator<ChatMessage> iteratorp = privateMessageList.iterator();
            //відправляємо приватні повідомлення
            while (iteratorp.hasNext()) {
                ChatMessage imsg = iteratorp.next();

                if (imsg.getReading() != true) {
                    imsg.setReading(true);
                    imsg.setRead(new Date());
                    messangerService.SaveMessage(imsg);
                }
                this.template.convertAndSend("/private/"+cd.getUuid(), imsg);
            }
        } catch (Exception e) {
        logger.info("PutChatPullInitMessage Exception");
                e.printStackTrace();
    }
        }

    @MessageMapping("/userslist")
    public void GetRoomUserList( ChatMessage inmsg) {
        // add user to userlist for room
        try {
            // get Instance ChatReferenceSingleton
            ChatReferenceSingleton chatReference = ChatReferenceSingleton.getInstance();
            ChatMessage cmsg = new ChatMessage();
            List<String > usersListMap =  new ArrayList<>();
            chatReference.GetUserLastLiveTime().put(inmsg.getUuid(),new Date());
            messangerService.CheckUserLastLiveTime();
            Map<String, String> usersMap =  chatReference.GetUsersMap();
            Gson gson = new GsonBuilder().create();
            ChatListElement chatListElement = new ChatListElement();
            for (Map.Entry<String, String> entry : usersMap.entrySet()) {
                chatListElement.setUuid(entry.getKey());
                chatListElement.setName(entry.getValue());
                String jcl = gson.toJson(chatListElement);
                usersListMap.add(jcl);
//                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
            String json = gson.toJson(usersListMap);
//            System.out.println(json);
            cmsg.setBody(json);
            cmsg.setUuid(inmsg.getUuid());
            this.template.convertAndSend("/userslist/"+cmsg.getUuid(), cmsg);
        } catch (Exception e) {
            logger.info("GetRoomUserList Exception");
            e.printStackTrace();
        }
    }

    @MessageMapping("/roomslist")
    public void GetRoomList( ChatMessage inmsg) {
        // add user to userlist for room
        try {
            // get Instance ChatReferenceSingleton
            ChatReferenceSingleton chatReference = ChatReferenceSingleton.getInstance();
            ChatMessage cmsg = new ChatMessage();
            List<String > usersListMap =  new ArrayList<>();

            Map<String, String> roomList =  chatReference.GetRoomsMap();
            if (roomList.size() == 0) {
                // Init list rooms
                List<MessageRoom> messageRoomList = messangerService.GetChatRoomList();
                for (MessageRoom entry : messageRoomList) {
                    roomList.put(entry.getUuid(), entry.getRoomname());
                }
            }
            Gson gson = new GsonBuilder().create();
            ChatListElement chatListElement = new ChatListElement();
            for (Map.Entry<String, String> entry : roomList.entrySet()) {
                chatListElement.setUuid(entry.getKey());
                chatListElement.setName(entry.getValue());
                String jcl = gson.toJson(chatListElement);
                usersListMap.add(jcl);
//                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
            String json = gson.toJson(usersListMap);
//            System.out.println(json);
            cmsg.setBody(json);
            cmsg.setUuid(inmsg.getUuid());
            this.template.convertAndSend("/roomslist/"+inmsg.getUuid(), cmsg);
        } catch (Exception e) {
            logger.info("GetRoomUserList Exception");
            e.printStackTrace();
        }
    }

    @MessageMapping("/public")
    public void GetChatPublicmessage( ChatMessage inmsg) {
        try {
//            MessageRoom mr = messangerService.GetRoomNameByUuid(inmsg.getRoomuuid());
            PutChatPublicMessage(inmsg);
        } catch (Exception e) {
            logger.info("PutChatPrivateMessage Exception");
            e.printStackTrace();
        }
    }

    @MessageMapping("/private")
    public void GetChatPrivateMessage( ChatMessage inmsg) {
        try {
            PutChatPrivateMessage(inmsg);
        } catch (Exception e) {
            logger.info("PutChatPrivateMessage Exception");
            e.printStackTrace();
        }
    }

    @SendTo("/topic")
    public void PutChatPublicMessage(ChatMessage message) throws Exception {
            this.template.convertAndSend("/public/"+message.getRoomuuid(), message);
            messangerService.SaveMessageFromChat(message);
    }

    public void PutChatPrivateMessage(ChatMessage message) throws Exception {
        Clientdetail cd = clientService.GetClientDetailByUuid(message.getTouuid());
        message.setSend(new Date());
        if (cd != null) {
            // todo при ініціалізації сеансу в чаті передбачити надсилання тільки одному адресату
            this.template.convertAndSend("/private/"+cd.getUuid(), message);
            this.template.convertAndSend("/private/"+message.getFromuuid(), message);
            messangerService.SaveMessageFromChat(message);
        }
    }

}
