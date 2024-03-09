package media.toloka.rfa.radio.root;

//import media.toloka.rfa.radio.model.MessageFromSite;
//import media.toloka.rfa.service.ServiceMessageFromSite;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.model.MessageFromSite;
import media.toloka.rfa.radio.model.Post;
import media.toloka.rfa.radio.model.Track;
import media.toloka.rfa.radio.root.service.ServiceMessageFromSite;
import media.toloka.rfa.media.store.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;
import java.util.List;

@Controller
@EnableWebMvc
public class RootController {

    Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private ServiceMessageFromSite smfs; // = new ServiceMessageFromSite();

    @Autowired
    private CreaterService createrService;

    @GetMapping(value = "/admin/root")
    public String adminroot(Model model) {

        List<Post> posts = createrService.GetAllPostsByApruve(true);
        List<Track> trackList = createrService.GetLastUploadTracks();

//        Page page = storeService.GetStorePageItemType(0,5, STORE_TRACK);
        Page pageTrack = createrService.GetTrackPage(0,10);
        List<Store> storeTrackList = pageTrack.stream().toList();

        Page pagePost = createrService.GetPostPage(0,12);
        List<Store> storePostList = pagePost.stream().toList();

//        model.addAttribute("trackList", trackList );
        model.addAttribute("trackList", storeTrackList );
        model.addAttribute("postList", storePostList );
        model.addAttribute("posts", posts );
//        model.addAttribute("stations",  stationService.GetListStationByUser(user));

        MessageFromSite QuestionForm = new MessageFromSite();
        model.addAttribute("question", QuestionForm);
        return "/admin/root";
    }


    @GetMapping(value = "/")
    public String index(Model model) {

        List<Post> posts = createrService.GetAllPostsByApruve(true);
        List<Track> trackList = createrService.GetLastUploadTracks();

//        Page page = storeService.GetStorePageItemType(0,5, STORE_TRACK);
        Page pageTrack = createrService.GetTrackPage(0,10);
        List<Store> storeTrackList = pageTrack.stream().toList();

        Page pagePost = createrService.GetPostPage(0,12);
        List<Store> storePostList = pagePost.stream().toList();

//        model.addAttribute("trackList", trackList );
        model.addAttribute("trackList", storeTrackList );
        model.addAttribute("postList", storePostList );
        model.addAttribute("posts", posts );
//        model.addAttribute("stations",  stationService.GetListStationByUser(user));

        MessageFromSite QuestionForm = new MessageFromSite();
        model.addAttribute("question", QuestionForm);
        return "/root";
    }


    @GetMapping(value = "/about")
    public String about(Model model) {

        List<Post> posts = createrService.GetAllPostsByApruve(true);
        List<Track> trackList = createrService.GetLastUploadTracks();

//        Page page = storeService.GetStorePageItemType(0,5, STORE_TRACK);
        Page pageTrack = createrService.GetTrackPage(0,10);
        List<Store> storeTrackList = pageTrack.stream().toList();

//        model.addAttribute("trackList", trackList );
        model.addAttribute("trackList", storeTrackList );
        model.addAttribute("posts", posts );
//        model.addAttribute("stations",  stationService.GetListStationByUser(user));

        MessageFromSite QuestionForm = new MessageFromSite();
        model.addAttribute("question", QuestionForm);
        return "/about";
    }

    @GetMapping(value="/saveform/root")
    public String submitquestion (Model model, @ModelAttribute("question") MessageFromSite rootQuestionForm) {
        MessageFromSite mfs = new MessageFromSite();
        mfs.setName(rootQuestionForm.getName());
        mfs.setEmail(rootQuestionForm.getEmail());
        mfs.setPhone(rootQuestionForm.getPhone());
        mfs.setMessage(rootQuestionForm.getMessage());
        mfs.setContact_datetime(new Date());
        smfs.save(mfs);

        return "redirect:/#contact";
    }

}
