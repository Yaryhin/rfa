package media.toloka.rfa.radio.store.Service;


import media.toloka.rfa.radio.store.Reposirore.StoreRepositorePagination;
import media.toloka.rfa.radio.store.implementation.StoreFileImplementation;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.store.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService extends StoreFileImplementation {

    final Logger logger = LoggerFactory.getLogger(StoreService.class);

    @Autowired
    private StoreRepositorePagination storeRepositore;

    @Autowired
    private FilesService filesService;

    public List<Store> GetAllByClientId(Clientdetail cd) {
        return storeRepositore.findAllByClientdetail(cd);
    }

    public Page GetStorePage(int pageNumber, int pageCount) {
        Pageable storePage = PageRequest.of(pageNumber, pageCount);
        Page page = storeRepositore.findAll(storePage);
        return page;
    }

    public Page GetStorePageByClientDetail(int pageNumber, int pageCount, Clientdetail cd) {
        Pageable storePage = PageRequest.of(pageNumber, pageCount);
        Page page = storeRepositore.findByClientdetail(storePage,cd);
        return page;

    }

//    public Store SaveStoreItemInfo(Store storeitem) {
//        storeRepositore.save(storeitem);
//        return storeitem;
//    }




}