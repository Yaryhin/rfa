package media.toloka.rfa.radio.contract;
// куерування контрактами користувача
// створення, оплата, закриття, пауза, зняття з паузи.

import lombok.extern.slf4j.Slf4j;
import media.toloka.rfa.radio.contract.model.Contract;
import media.toloka.rfa.radio.contract.model.EContractStatus;
import media.toloka.rfa.radio.contract.service.ContractService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import media.toloka.rfa.radio.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static media.toloka.rfa.radio.history.model.EHistoryType.History_UserCreateContract;


@Slf4j
@Controller
public class ClientHomeContractController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ContractService contractService;

//    @Autowired
//    private IUserService iuserService;

    final Logger logger = LoggerFactory.getLogger(ClientHomeContractController.class);

    @GetMapping(value = "/user/contract")
    public String ContractHome(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            logger.warn("User not found. Redirect to main page");
            return "redirect:/";
        }
        // дивимося його групи
        // відповідним чином виводимо пункти меню
        // Заповнюємо поля для форми
        List<Contract> contracts = contractService.listContractByUser(user);
        //==========================================================
        // Бавимося
        //==========================================================
        model.addAttribute("contracts",  contracts);
        model.addAttribute("userID",    user.getId());
        model.addAttribute("userName",  user.getEmail());
        return "/user/contract";
    }

    @GetMapping(value = "/user/contractedit")
    public String getEditContract(
            @RequestParam(value = "id", required = true) Long id,
            Model model ) {
        // Редагуємо контракт
        // передали id контракту
        Contract contract = contractService.GetContractById(id);
        if (contract == null) {
            logger.info("Контракт з id={} не знайдено", id.toString());
            // TODO Вивести в форму повідомлення, що контракт не знайдено
            return "redirect:/user/contract";
        }
        List<String> options = new ArrayList<String>();
        options.add(EContractStatus.CONTRACT_FREE.label);
        options.add(EContractStatus.CONTRACT_PAY.label);
        model.addAttribute("options", options);
        model.addAttribute("contract",  contract);
        model.addAttribute("info", "======= message ======");
        return "/user/contractedit";
    }




    @PostMapping(value = "/user/contractedit")
    public String postEditContract(
            @ModelAttribute ("contract") Contract contract,
            Model model ) {
        // Редагуємо контракт
        // передали id контракту
//        Contract contract = contractService.GetContractById(id);
        if (contract == null) {
            logger.info("Контракт з id={} збережено", contract.getId().toString());
            // TODO Вивести в форму повідомлення, що контракт збережено
            return "redirect:/user/contract";
        }
//        List<String> options = new ArrayList<String>();
//        options.add(EContractStatus.CONTRACT_FREE.label);
//        options.add(EContractStatus.CONTRACT_PAY.label);
//        model.addAttribute("options", options);
//
//
//
//        model.addAttribute("contract",  contract);
//        model.addAttribute("info", "======= message ======");
        logger.info("Контракт з UUID={} збережено", contract.getUuid());
        return "redirect:/user/contract";
    }

    @GetMapping(value = "/user/createcontract")
    public String getuserCreateContract(
            @ModelAttribute Contract contract,
            Model model ) {
        // перевірити чи увійшли
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            logger.warn("User not found. Redirect to main page");
            return "redirect:/";
        }
        Contract ncontract = new Contract();
        ncontract.setContractStatus(EContractStatus.CONTRACT_FREE);
        ncontract.setNumber( UUID.randomUUID().toString());
        ncontract.setClientdetail(clientService.getClientDetail(clientService.GetCurrentUser()));
        model.addAttribute("contract",  ncontract);
        return "/user/createcontract";
    }

    @PostMapping(value = "/user/createcontract")
    public String postuserCreateContract(
            @ModelAttribute Contract contract,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            logger.warn("User not found. Redirect to main page");
            return "redirect:/";
        }
        // Працюємо з новим контрактом.
        Contract ncontract = new Contract();
        ncontract.setContractStatus(EContractStatus.CONTRACT_FREE);
        ncontract.setNumber(UUID.randomUUID().toString());
        ncontract.setUuid(ncontract.getNumber());
        ncontract.setCreateDate(LocalDateTime.now());
        ncontract.setLastPayDate(null);
        ncontract.setClientdetail(clientService.getClientDetail(clientService.GetCurrentUser()));
        ncontract.setUsercomment(contract.getUsercomment());
        contractService.saveContract(ncontract);
        historyService.saveHistory(History_UserCreateContract, " Новий контракт: "+ncontract.getNumber().toString(), user);

        return "redirect:/user/contract";
    } // END postuserCreateContract
}
