package media.toloka.rfa.radio.model;

//import jakarta.persistence.*;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.security.model.Users;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
//@Table
public class Clientdetail {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private String custname;
    @Expose
    private String custsurname;
    @Expose
    private String firmname;
    @Expose
    private String uuid;
    @Expose
    private Boolean confirminfo;
    @Expose
    private Date confirmDate;
    @Expose
    private Date createdate;
    @Expose
    private String comments;
    @Expose
    private Double account = 0.0;

//    @Expose
//    @ElementCollection
    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = {CascadeType.ALL}) //,cascade = {CascadeType.ALL},fetch= FetchType.EAGER)
    private  List<Clientaddress> clientaddressList = new ArrayList<>();

    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = {CascadeType.ALL}) //,cascade = {CascadeType.ALL},fetch= FetchType.EAGER)
    private  List<Contract> contractList = new ArrayList<>();

    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<History> historyList = new ArrayList<>();

    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private  List<Documents> documentslist = new ArrayList<>();

    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private  List<Station> stationList = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.ALL})
    private  Users user;
//    @ElementCollection
//    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Station> stationList;

    public Clientdetail() {
//        this.clientaddressList  = new ArrayList<Clientaddress>();
//        this.contractList       = new ArrayList<Contract>();
//        this.historyList        = new ArrayList<History>();
//        this.documentslist      = new ArrayList<Documents>();
//        this.stationList        = new ArrayList<Station>();
        this.uuid               = UUID.randomUUID().toString();
        this.createdate         = new Date();
        this.confirminfo        = false;
//        this.comments           = "";
//        this.custname           = "";
//        this.custsurname        = "";
//        this.firmname           = "";
    }

}
