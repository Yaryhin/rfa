package media.toloka.rfa.radio.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.EServerState;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table
public class Station {

    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private EServerState serverState;
    @Expose
    private String name;
    @Expose
    private String dbname;
    @Expose
    private boolean enable = false;
    @Expose
    private Date startdate;
    @Expose
    private Date createdate;
    @Expose
    private Date lastpaydate;
    @Expose
    private Date lastchangedate;
    @Expose
    public Integer guiport;
    @Expose
    public String guiserver;
    @Expose
    public Integer main;
    @Expose
    public Integer show;
    @Expose
    private String uuid;
    @Expose
    private String icecastname;
    @Expose
    private String icecastdescription;
    @Expose
    private String icecastsite;
    @Expose
    private String icecastgenre;
//    private transient Contract contract;


    @ToString.Exclude
    @ManyToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

    @ToString.Exclude
    @ManyToOne(optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ToString.Exclude
    @OneToMany(mappedBy = "station", fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<Poolport> ports = new ArrayList<>();


    public Station() {
        this.uuid= UUID.randomUUID().toString();
        this.name=this.uuid;
        this.createdate=new Date();
        this.lastchangedate=new Date();
    }
}
