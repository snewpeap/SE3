package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.manage;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAffiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAuthor;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAuthorDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminServiceImplTest {
    private DuplicateAuthorDao duplicateAuthorDao;
    private DuplicateAffiliationDao duplicateAffiliationDao;
    private AuthorDao authorDao;
    private AffiliationDao affiliationDao;
    private EntityMsg msg;

    private AdminServiceImpl service;

    @Before
    public void setUp() {
        msg = mock(EntityMsg.class);
        when(msg.getAuthorType()).thenReturn(1);
        when(msg.getAffiliationType()).thenReturn(2);
        duplicateAuthorDao = mock(DuplicateAuthorDao.class);
        when(duplicateAuthorDao.findByClear(eq(true), any(Pageable.class))).thenReturn(getClearAuthor());
        when(duplicateAuthorDao.findByClear(eq(false), any(Pageable.class))).thenReturn(getUnclearAuthor());
        duplicateAffiliationDao = mock(DuplicateAffiliationDao.class);
        when(duplicateAffiliationDao.findByClear(eq(true), any(Pageable.class))).thenReturn(getClearAffi());
        when(duplicateAffiliationDao.findByClear(eq(false), any(Pageable.class))).thenReturn(getUnclearAffi());
    }

    @Test
    public void getDataError() {
        service = new AdminServiceImpl(duplicateAuthorDao, duplicateAffiliationDao, msg, authorDao, affiliationDao);
        List<AliasVO> vos = service.getDataError(0, msg.getAuthorType());
        assertEquals(2, vos.size());
        for (AliasVO vo : vos) {
            assertEquals(msg.getAuthorType(), vo.getType());
            assertEquals(1, vo.getFathers().size());
            AliasItem item = vo.getFathers().get(0);
            assertEquals(1L, item.getFatherId());
            assertEquals("a1", item.getAliasName());
        }
    }

    @Test
    public void getDataError_affi() {
        service = new AdminServiceImpl(duplicateAuthorDao, duplicateAffiliationDao, msg, authorDao, affiliationDao);
        List<AliasVO> vos = service.getDataError(0, msg.getAffiliationType());
        assertEquals(1, vos.size());
        for (AliasVO vo : vos) {
            assertEquals(msg.getAffiliationType(), vo.getType());
            assertEquals(1, vo.getFathers().size());
            AliasItem item = vo.getFathers().get(0);
            assertEquals(2L, item.getFatherId());
            assertEquals("a2", item.getAliasName());
        }
    }

    @Test
    public void getDataOperated() {
        service = new AdminServiceImpl(duplicateAuthorDao, duplicateAffiliationDao, msg, authorDao, affiliationDao);
        List<AliasVO> vos = service.getDataOperated(0, 1);
        assertEquals(2, vos.size());
        for (AliasVO vo : vos) {
            assertEquals(msg.getAuthorType(), vo.getType());
            assertEquals(1, vo.getFathers().size());
            AliasItem item = vo.getFathers().get(0);
            assertEquals(1L, item.getFatherId());
            assertEquals("a1", item.getAliasName());
        }
    }

    @Test
    public void operateDataAlias() {
    }

    @Test
    public void undoOperate() {
    }

    private Streamable<DuplicateAuthor> getClearAuthor() {
        Author a1 = new Author();
        a1.setId(1L);
        a1.setName("a1");
        a1.setAlias(a1);
        Author a2 = new Author();
        a2.setId(2L);
        a2.setName("a2");
        a2.setAlias(a1);
        Author a3 = new Author();
        a3.setId(3L);
        a3.setName("a3");
        a3.setAlias(a2);
        DuplicateAuthor d1 = new DuplicateAuthor(a1, a2);
        d1.setId(1L);
        d1.setClear(true);
        DuplicateAuthor d2 = new DuplicateAuthor(a2, a3);
        d2.setId(2L);
        d2.setClear(true);
        return Streamable.of(d1, d2);
    }

    private Streamable<DuplicateAuthor> getUnclearAuthor() {
        Author a1 = new Author();
        a1.setId(1L);
        a1.setName("a1");
        Author a2 = new Author();
        a2.setId(2L);
        a2.setName("a2");
        Author a3 = new Author();
        a3.setId(3L);
        a3.setName("a3");
        DuplicateAuthor d1 = new DuplicateAuthor(a1, a2);
        d1.setId(1L);
        DuplicateAuthor d2 = new DuplicateAuthor(a1, a3);
        d2.setId(2L);
        return Streamable.of(d1, d2);
    }

    private Streamable<DuplicateAffiliation> getClearAffi() {
        Affiliation a1 = new Affiliation();
        a1.setId(1L);
        a1.setName("a1");
        a1.setAlias(a1);
        Affiliation a2 = new Affiliation();
        a2.setId(2L);
        a2.setName("a2");
        a2.setAlias(a1);
        Affiliation a3 = new Affiliation();
        a3.setId(3L);
        a3.setName("a3");
        a3.setAlias(a2);
        DuplicateAffiliation d1 = new DuplicateAffiliation(a1, a2);
        d1.setId(1L);
        d1.setClear(true);
        DuplicateAffiliation d2 = new DuplicateAffiliation(a2, a3);
        d2.setId(2L);
        d2.setClear(true);
        return Streamable.of(d1, d2);
    }

    private Streamable<DuplicateAffiliation> getUnclearAffi() {
        Affiliation a1 = new Affiliation();
        a1.setId(1L);
        a1.setName("a1");
        a1.setAlias(null);
        Affiliation a2 = new Affiliation();
        a2.setId(2L);
        a2.setName("a2");
        a2.setAlias(null);
        DuplicateAffiliation d = new DuplicateAffiliation(a2, a1);
        d.setId(1L);
        return Streamable.of(d);
    }
}