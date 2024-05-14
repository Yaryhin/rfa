package media.toloka.rfa.radio.podcast.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.store.model.Store;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(indexes = @Index(columnList = "uuid"))
public class PodcastItem {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private String uuid = UUID.randomUUID().toString();
    @Expose
    private String title;
    @Expose
    private String link;
    @Expose
    private String pubDate;
    @Expose
    private String comments;
    @Expose
    private String category;
    @Expose
    @Column(columnDefinition = "TEXT")
    private String description;
    @Expose
    private String enclosure;
    @Expose
    @DateTimeFormat(pattern = "dd-MM-yy")
    private Date date = new Date();
    @Expose
    private String storeuuid;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "store_id")
    private Store storeitem;


    @ElementCollection
//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "podcast_channel_id")
    private PodcastChannel chanel;
}
