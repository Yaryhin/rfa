package media.toloka.rfa.radio.creater.fileupload;

import lombok.extern.slf4j.Slf4j;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.document.service.DocumentService;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.history.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static media.toloka.rfa.radio.model.enumerate.EHistoryType.History_DocumentCreate;


@Slf4j
@RestController
//@RequestMapping("/uploadfile")
public class CreaterDropPostFileController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private FilesService filesService;

    final Logger logger = LoggerFactory.getLogger(CreaterDropPostFileController.class);

    @PostMapping(path = "/creater/uploadfile" ) // , produces = MediaType.APPLICATION_JSON_VALUE
    public void upload(@RequestParam("file") MultipartFile file) {

        log.info("uploaded file " + file.getOriginalFilename());
        if (file.isEmpty()) {
//                throw new ExecutionControl.UserException("Empty file");
            logger.info("Завантаження файлу: Файл порожній");
        }
//        Path destination = Paths.get("/home/ysv/rfa/upload").resolve(file.getOriginalFilename()).normalize().toAbsolutePath();
        Path destination = Paths.get(filesService.GetClientDirectory()).resolve(file.getOriginalFilename()).normalize().toAbsolutePath();
//        Path destination = Paths.get("upload").resolve(file.getOriginalFilename()).normalize().toAbsolutePath();
        try {
            Files.createDirectories(destination.getParent());
            Files.copy(file.getInputStream(), destination);
            // Зберігаємо інформацію о файлі та привʼязуємо до користувача.
            Random random = new Random();
//            long time = System.currentTimeMillis();
            long difference = random.nextInt(1000);
            logger.info("Завантаження файлу: Випадкова затримка {}",difference);
            try {
                Thread.sleep(difference);
                documentService.saveDocumentUploadInfo(destination);
                historyService.saveHistory(History_DocumentCreate, " Завантажено файл: " + file.getOriginalFilename(), clientService.GetCurrentUser());
            }
            catch(InterruptedException e)
            {
                logger.info("--------- catch(InterruptedException e)");
            }
        } catch (IOException e) {
            logger.info("Завантаження файлу: Проблема збереження");
            e.printStackTrace();
        }
        log.info("uploaded file " + file.getOriginalFilename());
    }

//    public void uploadFile(MultipartFile file) throws ExecutionControl.UserException {
//        try {
//            if(file.isEmpty()) {
////                throw new ExecutionControl.UserException("Empty file");
//                logger.debug("Завантаження файлу: Файл порожній");
//            }
//            Path destination = Paths.get("upload").resolve(file.getOriginalFilename()).normalize().toAbsolutePath();
//            Files.copy(file.getInputStream(), destination);
//        } catch(IOException e) {
////            throw new ExecutionControl.UserException("Store exception");
//            logger.debug("Завантаження файлу: Проблема збереження");
//        }
//    }
}
