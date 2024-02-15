package media.toloka.rfa.radio.creater;


import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Post;
import media.toloka.rfa.radio.post.service.PostService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CreaterInfo {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private PostService postService;

    final Logger logger = LoggerFactory.getLogger(CreaterInfo.class);


    @GetMapping(value = "/creater/info")
    public String getUserInfo(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(user);
        model.addAttribute("clientdetail", cd );

        return "/creater/info";
    }

    @PostMapping(value="/creater/info")
    public String postCreaterEditPost(
//            @PathVariable Long idcd,
            @ModelAttribute Clientdetail fcd,
            Model model )

    {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(user);
        //
        cd.setComments(fcd.getComments());
        cd.setFirmname(fcd.getFirmname());
        cd.setCustname(fcd.getCustname());
        cd.setCustsurname(fcd.getCustsurname());

        clientService.SaveClientDetail(cd);

        model.addAttribute("clientdetail", cd );
        return "/creater/info";
    }
}