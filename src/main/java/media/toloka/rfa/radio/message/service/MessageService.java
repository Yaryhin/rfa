package media.toloka.rfa.radio.message.service;

import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.history.repository.RepoHistory;
import media.toloka.rfa.radio.message.model.Messages;
import media.toloka.rfa.radio.message.repo.RepoMessages;
import media.toloka.rfa.radio.message.repo.RepoRooms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ListIterator;

@Service
@Transactional
public class MessageService {
    @Autowired
    private RepoMessages repoMessages;
    @Autowired
    private RepoRooms repoRooms;
    @Autowired
    private RepoHistory repoHistory;

    public List<Messages> GetMessages(Clientdetail clientdetail) {
        return repoMessages.findMessagesByFromOrTom(clientdetail, clientdetail);
    }
    public List<Messages> GetMessagesDesc(Clientdetail clientdetail) {
        return repoMessages.findMessagesByFromOrTomOrderBySendDesc(clientdetail, clientdetail);
    }

    // отримали загальну кількість повідомлень для клієнта.
    public Long GetQuantityAllMessage(Clientdetail clientdetail) {
        return GetMessages(clientdetail).size();
    }
    // отримали загальну кількість нових повідомлень для клієнта.
    public Long GetQuantityNewMessage(Clientdetail clientdetail) {
        return GetNewMessages(clientdetail).size();
    }

    public List<Messages> GetNewMessages(Clientdetail clientdetail) {
//            return repoMessages.findMessagesByReadingAndTom(true,clientdetail);
        List<Messages> msg = repoMessages.findMessagesByReadAndTom(null, clientdetail);
        return msg;
    }

    public void SendMessageToUser(Clientdetail from, Clientdetail to, String message) //from to message
    {
        // формуємо повідомлення
        Messages um = new Messages();
        um.setBody(message);
        um.setSend(LocalDateTime.now());
        um.setReading(true);
        um.setFrom(from);
        um.setTom(to);
        // Надсилаємо повідомлення
        SaveMessage(um);
    }

    public void SaveMessage(Messages message) {
        repoMessages.save(message);
    }

    public void SetReadingAllMessages(Clientdetail cd) {

        List<Messages> listNewMessages = repoMessages.findMessagesByReadingAndTom(true,cd);
        ListIterator<Messages> listIterator = listNewMessages.listIterator();

        while(listIterator.hasNext()) {
            Messages msg = listIterator.next();
            if (msg.getRead() != null) {
                msg.setReading(false);
            }
            msg.setRead(LocalDateTime.now());
            SaveMessage(msg);
        }
    }
}
